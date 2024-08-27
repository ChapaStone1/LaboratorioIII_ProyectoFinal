package ar.edu.utn.frbb.tup.controller.validator;
import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciasDto;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaInvalidoException;

@Component
public class TransferenciaValidator {
    public void validar(TransferenciasDto transferenciaDto) throws TipoMonedaInvalidoException {
        validarNumeroCuenta(transferenciaDto.getCuentaOrigen());
        validarNumeroCuenta(transferenciaDto.getCuentaDestino());
        validarMonto(transferenciaDto.getMonto());
        validarTipoMoneda(transferenciaDto.getMoneda());
    }
    public void validarNumeroCuenta(long numeroCuenta){

        String nroCuentaString = String.valueOf(numeroCuenta);

        if(nroCuentaString.length() > 8) {
            throw new IllegalArgumentException("El NUMERO DE CUENTA ingresado no es valido.");

        }

        if (!nroCuentaString.matches("\\d{1,8}")) {
            throw new IllegalArgumentException("El NUMERO DE CUENTA ingresado no es valido.");
        }
    }
    public void validarMonto(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
    }
    public void validarTipoMoneda(String moneda) throws TipoMonedaInvalidoException {
        if (!"P".equalsIgnoreCase(moneda) && !"D".equalsIgnoreCase(moneda)) {
            throw new TipoMonedaInvalidoException("El tipo de moneda ingresado no es valido.");
        }
    }


}
