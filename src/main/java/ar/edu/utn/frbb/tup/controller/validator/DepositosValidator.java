package ar.edu.utn.frbb.tup.controller.validator;
import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.dto.DepositosDto;
import ar.edu.utn.frbb.tup.model.exception.InputErrorException;

@Component
public class DepositosValidator {
    public void validar(DepositosDto depositoDto) throws InputErrorException {
        validarNumeroCuenta(depositoDto.getCuenta());
        validarMonto(depositoDto.getMonto());
        validarMoneda(depositoDto.getMoneda());
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

    public void validarMonto(double monto) throws InputErrorException {
        if (monto <= 0) {
            throw new InputErrorException("El MONTO ingresado no es valido.");
        }

        try{
            Double.parseDouble(String.valueOf(monto));
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("El MONTO ingresado no es valido.");
        }
    }

    public void validarMoneda(String moneda) throws InputErrorException {
        if (!"PESOS".equalsIgnoreCase(moneda) && !"DOLARES".equalsIgnoreCase(moneda)) {
            throw new InputErrorException("El TIPO DE MONEDA ingresado no es valido.");
        }
    }


}
