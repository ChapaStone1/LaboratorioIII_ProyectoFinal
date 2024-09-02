package ar.edu.utn.frbb.tup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciasDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistException;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.model.exception.TipoDeCuentasException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNotSupportedException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;

@Service
public class TransferenciaService {

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private BanelcoService banelcoService;

    public RespuestaDto realizarTransferencia(TransferenciasDto transferenciaDto) throws NotExistCuentaException, NotExistCuentaException, TipoDeCuentasException, TipoMonedaNotSupportedException{
        // Obtener las cuentas
        Cuenta cuentaOrigen = cuentaDao.findByNumeroCuenta(transferenciaDto.getCuentaOrigen());
        if (cuentaOrigen == null) {
            throw new NotExistCuentaException("La cuenta " + transferenciaDto.getCuentaOrigen() + " (cuenta de origen) no existe.");
        }

        Cuenta cuentaDestino = cuentaDao.findByNumeroCuenta(transferenciaDto.getCuentaDestino());
        if (cuentaDestino == null) {
            if (banelcoService.servicioDeBanelco(transferenciaDto)) {
                return transferirOtroBanco(transferenciaDto, cuentaOrigen, cuentaDestino);
                //RespuestaDto respuestaDto = new RespuestaDto();
                //respuestaDto.setEstado("EXITOSA");
                //respuestaDto.setMensaje("Se realizo la transferencia de cuenta " + cuentaOrigen + " a cuenta " + cuentaDestino + " por el monto de " + transferenciaDto.getMonto() + " " + transferenciaDto.getMoneda() + ".");
            } else {
                RespuestaDto respuestaDto = new RespuestaDto();
                respuestaDto.setEstado("FALLIDA");
                respuestaDto.setMensaje("No es posible realizar la transferencia, los bancos son diferentes.");
                return respuestaDto;
            }
        }
        if (!cuentaOrigen.getMoneda().equals(cuentaDestino.getMoneda()) || !(String.valueOf(cuentaOrigen.getMoneda())).equals(transferenciaDto.getMoneda()) || !(String.valueOf(cuentaDestino.getMoneda())).equals(transferenciaDto.getMoneda())) {
            throw new TipoMonedaNotSupportedException("Las monedas de las cuentas no coinciden.");
        }
        return transferirMismoBanco(transferenciaDto, cuentaOrigen, cuentaDestino);

    }
   
    private RespuestaDto transferirMismoBanco(TransferenciasDto transferenciaDto, Cuenta cuentaOrigen, Cuenta cuentaDestino) {
        RespuestaDto respuesta = new RespuestaDto();
        Movimiento movimientoOrigen = new Movimiento();
        Movimiento movimientoDestino = new Movimiento();

        if (cuentaOrigen.getBalance() >= transferenciaDto.getMonto()) {
            double comision = calcularComision(transferenciaDto);
            double balanceCuentaOrigen = balanceCuentaOrigen(comision, comision, comision);
            double balanceCuentaDestino = balanceCuentaDestino(balanceCuentaOrigen, balanceCuentaOrigen, comision);
            cuentaService.actualizarBalance(cuentaOrigen, balanceCuentaOrigen, TipoMovimiento.TRANSFERENCIA);
            cuentaService.actualizarBalance(cuentaDestino, balanceCuentaDestino, TipoMovimiento.TRANSFERENCIA);

            // Creo y agrego los movimientos
            movimientoOrigen.guardarMovimiento(cuentaOrigen, TipoMovimiento.TRANSFERENCIA, balanceCuentaOrigen, cuentaOrigen.getNumeroCuenta(), cuentaDestino.getNumeroCuenta());
            movimientoDestino.guardarMovimiento(cuentaDestino, TipoMovimiento.TRANSFERENCIA, balanceCuentaDestino, cuentaDestino.getNumeroCuenta(), cuentaOrigen.getNumeroCuenta());

            // Actualizo las cuentas en la base de datos
            cuentaDao.actualizarCuenta(cuentaOrigen);
            cuentaDao.actualizarCuenta(cuentaDestino);

            // Preparo una respuestaDto
            respuesta.setEstado("EXITOSA");
            respuesta.setMensaje("Transferencia Exitosa. Número de transferencia: " + movimientoOrigen.getNumMovimiento() + ". Realizado el " + movimientoOrigen.getFecha());
            return respuesta;
        } else {
            respuesta.setEstado("FALLIDA");
            respuesta.setMensaje("Saldo insuficiente para realizar la transferencia.");
            return respuesta;
        }
    }

    private double calcularComision(TransferenciasDto transferenciaDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calcularComision'");
    }

    private double balanceCuentaOrigen(double balance, double monto, double comision){
        return balance - monto - comision;
    }
    private double balanceCuentaDestino(double balance, double monto, double comision){
        return balance + monto;
    }

    private double calcularNuevoBalance(double balance, double monto, double comision, TipoMovimiento movimiento){
        return balance - monto - comision;
    }
    private RespuestaDto transferirOtroBanco(TransferenciasDto transferenciaDto, Cuenta cuentaOrigen, Cuenta cuentaDestino) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'transferirOtroBanco'");
    }        
}
