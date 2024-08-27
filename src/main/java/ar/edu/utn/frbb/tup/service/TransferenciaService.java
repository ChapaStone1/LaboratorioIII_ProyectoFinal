package ar.edu.utn.frbb.tup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciasDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
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
            throw new NotExistCuentaException("La cuenta " + transferenciaDto.getCuentaDestino() + " (cuenta de destino) no existe.");
        }
        if (!cuentaOrigen.getMoneda().equals(cuentaDestino.getMoneda()) || !(String.valueOf(cuentaOrigen.getMoneda())).equals(transferenciaDto.getMoneda()) || !(String.valueOf(cuentaDestino.getMoneda())).equals(transferenciaDto.getMoneda())) {
            throw new TipoMonedaNotSupportedException("Las monedas de las cuentas no coinciden.");
        }
        if ((cuentaOrigen.getTitular().getBanco()).equals(cuentaDestino.getTitular().getBanco())) {
            return realizarTransferenciaYActualizarBalance(transferenciaDto, cuentaOrigen, cuentaDestino);

        } else {
            if (banelcoService.servicioDeBanelco(transferenciaDto)) {
                return realizarTransferenciaYActualizarBalance(transferenciaDto, cuentaOrigen, cuentaDestino);
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
    }
    private RespuestaDto realizarTransferenciaYActualizarBalance(TransferenciasDto transferenciaDto, Cuenta cuentaOrigen, Cuenta cuentaDestino) {
        RespuestaDto respuesta = new RespuestaDto();

        Transferencia transferencia = toTransferencia(transferenciaDto);

        if (cuentaOrigen.getBalance() >= transferenciaDto.getMonto()) {
            double comision = transaccion.calcularComision(transferenciaDto);

            cuentaService.actualizarBalance(cuentaOrigen, transferenciaDto.getMonto(), comision, TipoMovimiento.TRANSFERENCIA_SALIDA);
            cuentaService.actualizarBalance(cuentaDestino, transferenciaDto.getMonto(), comision, TipoMovimiento.TRANSFERENCIA_ENTRADA);

            // Creo y agrego las transacciones al historial
            transaccion.save(cuentaOrigen, transferencia, TipoMovimiento.TRANSFERENCIA_SALIDA, "Transferencia a cuenta " + cuentaDestino.getNumeroCuenta());
            transaccion.save(cuentaDestino, transferencia, TipoMovimiento.TRANSFERENCIA_ENTRADA, "Transferencia desde cuenta " + cuentaOrigen.getNumeroCuenta());

            // Actualizo las cuentas en la base de datos
            cuentaDao.update(cuentaOrigen);
            cuentaDao.update(cuentaDestino);

            // Preparo una respuestaDto
            respuesta.setEstado("EXITOSA");
            respuesta.setMensaje("Se realizó la transferencia exitosamente. Número de transferencia: " + transferencia.getNumeroTransaccion() + ". Realizado el " + transferencia.getFecha() + ". Saldo actual: " + (cuentaOrigen.getBalance() > 0 ? (TipoMoneda.valueOf(transferenciaDto.getMoneda()) == TipoMoneda.PESOS ? "ARG $ " : "USD $ ") + cuentaOrigen.getBalance() : "Usted tiene una deuda con el Banco."));
            return respuesta;
        } else {
            respuesta.setEstado("FALLIDA");
            respuesta.setMensaje("Saldo insuficiente para realizar la transferencia.");
            return respuesta;
        }
    }

    private Transferencia toTransferencia(TransferenciaDto transferenciaDto) {
        Transferencia transferencia = new Transferencia();
        transferencia.setMonto(transferenciaDto.getMonto());
        transferencia.setCuentaOrigen(transferenciaDto.getCuentaOrigen());
        transferencia.setCuentaDestino(transferenciaDto.getCuentaDestino());
        transferencia.setMoneda(transferenciaDto.getMoneda());
        return transferencia;
    }
    
}
