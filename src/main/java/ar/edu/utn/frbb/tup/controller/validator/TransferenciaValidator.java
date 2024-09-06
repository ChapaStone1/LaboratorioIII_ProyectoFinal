package ar.edu.utn.frbb.tup.controller.validator;
import ar.edu.utn.frbb.tup.model.exception.InputErrorException;
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
        validarCuentasIguales(transferenciaDto.getCuentaOrigen(), transferenciaDto.getCuentaDestino());
    }

    public void validarNumeroCuenta(long numeroCuenta){
        String nroCuentaString = String.valueOf(numeroCuenta);
        if(nroCuentaString.length() > 8) {
            throw new IllegalArgumentException("El número de cuenta no es valido.");

        }
        if (!nroCuentaString.matches("\\d{1,8}")) {
            throw new IllegalArgumentException("El número de cuenta no es valido.");
        }
    }
    public void validarCuentasIguales(long cuentaOrigen, long cuentaDestino) {
        try {
            if (cuentaDestino == cuentaOrigen) {
                throw new IllegalArgumentException("Las cuentas de origen y destino no pueden ser iguales.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Número de cuenta inválido.", e);
        }
    }

    public void validarMonto(double monto) {
        if (monto <= 0) {
            throw new InputErrorException("Monto ingresado no es valido.");
        }
        try{
            Double.parseDouble(String.valueOf(monto));
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Monto ingresado no es valido.");
        }
    }

    public void validarTipoMoneda(String moneda) throws TipoMonedaInvalidoException {
        if (!"P".equalsIgnoreCase(moneda) && !"D".equalsIgnoreCase(moneda)) {
            throw new TipoMonedaInvalidoException("Tipo de moneda ingresado no es valido.");
        }
    }
}
