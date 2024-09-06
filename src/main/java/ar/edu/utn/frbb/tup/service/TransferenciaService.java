package ar.edu.utn.frbb.tup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciasDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistException;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.model.exception.TipoDeCuentasException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNotSupportedException;

@Service
public class TransferenciaService {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private BanelcoService banelcoService;

    public RespuestaDto realizarTransferencia(TransferenciasDto transferenciaDto) throws NotExistCuentaException, NotExistCuentaException, TipoDeCuentasException, TipoMonedaNotSupportedException, ClienteNotExistException{
        // Obtener las cuentas
        Cuenta cuentaOrigen = cuentaService.buscarCuentaPorNumeroCuenta(transferenciaDto.getCuentaOrigen());
        if (cuentaOrigen == null) {
            throw new NotExistCuentaException("La cuenta " + transferenciaDto.getCuentaOrigen() + " (cuenta de origen) no existe.");
        }

        Cuenta cuentaDestino = cuentaService.buscarCuentaPorNumeroCuenta(transferenciaDto.getCuentaDestino());
        if (cuentaDestino == null) {
            if (banelcoService.servicioDeBanelco(transferenciaDto)) {
                return transferirOtroBanco(transferenciaDto, cuentaOrigen);
            } else {
                RespuestaDto respuestaDto = new RespuestaDto();
                respuestaDto.setEstado("FALLIDA");
                respuestaDto.setMensaje("No es posible realizar la transferencia, banco de destino no se encuentra en Red Banelco.");
                return respuestaDto;
            }
        }
        System.out.println("Cuenta de origen: " + cuentaOrigen.getNumeroCuenta() + ", Moneda: " + cuentaOrigen.getMoneda());
        if (!cuentaOrigen.getMoneda().equals(cuentaDestino.getMoneda()) || !(cuentaOrigen.getMoneda()).equals(cuentaDestino.getMoneda()) || !(cuentaDestino.getMoneda()).equals(cuentaDestino.getMoneda())) {
            throw new TipoMonedaNotSupportedException("Las monedas de las cuentas no coinciden.");
        }
        return transferirMismoBanco(transferenciaDto, cuentaOrigen, cuentaDestino);

    }
   
    private RespuestaDto transferirMismoBanco(TransferenciasDto transferenciaDto, Cuenta cuentaOrigen, Cuenta cuentaDestino) throws ClienteNotExistException {
        RespuestaDto respuesta = new RespuestaDto();

        if (cuentaOrigen.getBalance() >= transferenciaDto.getMonto()) {

            // calculo la comision en cuentaservice
            double comision = cuentaService.calcularComision(cuentaOrigen.getMoneda(), transferenciaDto.getMonto());

            //calculo los balances en cuentaService
            double balanceCuentaOrigen = cuentaService.balanceCuentaOrigen(cuentaOrigen.getBalance(), transferenciaDto.getMonto(), comision);
            double balanceCuentaDestino = cuentaService.balanceCuentaDestino(cuentaDestino.getBalance(), transferenciaDto.getMonto(), comision);

            // actualizo los balances en cuentaService
            cuentaService.actualizarBalance(cuentaOrigen, balanceCuentaOrigen, TipoMovimiento.TRANSFERENCIA);
            cuentaService.actualizarBalance(cuentaDestino, balanceCuentaDestino, TipoMovimiento.TRANSFERENCIA);

            // Creo y agrego los movimientos para ambas cuentas
            respuesta = cuentaService.agregarMovimientoTransferencia(cuentaOrigen, transferenciaDto.getMonto()-comision, cuentaOrigen.getNumeroCuenta(), cuentaDestino.getNumeroCuenta());
            cuentaService.agregarMovimientoTransferencia(cuentaDestino, transferenciaDto.getMonto()-comision, cuentaOrigen.getNumeroCuenta(), cuentaDestino.getNumeroCuenta());

            // Actualizo las cuentas en la base de datos
            cuentaService.actualizarCuenta(cuentaOrigen);
            cuentaService.actualizarCuenta(cuentaDestino);
            
            // retorno la respuesta
            return respuesta;
        } 
        else {
            respuesta.setEstado("FALLIDA");
            respuesta.setMensaje("Saldo insuficiente para realizar la transferencia.");
            return respuesta;
        }
    } 

    private RespuestaDto transferirOtroBanco(TransferenciasDto transferenciaDto, Cuenta cuentaOrigen) throws ClienteNotExistException {
        RespuestaDto respuesta = new RespuestaDto();
        if (cuentaOrigen.getBalance() >= transferenciaDto.getMonto()) {
            // calculo la comision en cuentaservice
            double comision = cuentaService.calcularComision(cuentaOrigen.getMoneda(), transferenciaDto.getMonto());
            //calculo el nuevo balance en cuentaService
            double balanceCuentaOrigen = cuentaService.balanceCuentaOrigen(cuentaOrigen.getBalance(), transferenciaDto.getMonto(), comision);
            // actualizo los balances en cuentaService
            cuentaService.actualizarBalance(cuentaOrigen, balanceCuentaOrigen, TipoMovimiento.TRANSFERENCIA);
            // Creo y agrego los movimientos para ambas cuentas
            respuesta = cuentaService.agregarMovimientoTransferencia(cuentaOrigen, transferenciaDto.getMonto()-comision, cuentaOrigen.getNumeroCuenta(), transferenciaDto.getCuentaDestino());
            // Actualizo las cuentas en la base de datos
            cuentaService.actualizarCuenta(cuentaOrigen);
            // retorno la respuesta
            return respuesta;
        }
        else{
            respuesta.setEstado("FALLIDA");
            respuesta.setMensaje("Saldo insuficiente para realizar la transferencia.");
            return respuesta;
        }
    }
}
