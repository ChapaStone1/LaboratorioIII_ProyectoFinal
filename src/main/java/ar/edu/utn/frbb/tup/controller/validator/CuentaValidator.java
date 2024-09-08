package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaInvalidoException;

@Component
public class CuentaValidator {
    public void validar(CuentaDto cuentaDto) {

        // Verifica que el tipo de cuenta sea "CC" o "CA" y no sea null
        if (cuentaDto.getTipoCuenta() == null || 
            !(cuentaDto.getTipoCuenta().equals("CC") || cuentaDto.getTipoCuenta().equals("CA"))) {
            throw new TipoCuentaNotSupportedException("El tipo de cuenta no es correcto");
        }

        // Verifica que la moneda sea "P" o "D" y no sea null
        if (cuentaDto.getMoneda() == null || 
            !(cuentaDto.getMoneda().equals("P") || cuentaDto.getMoneda().equals("D"))) {
            throw new TipoMonedaInvalidoException("El tipo de moneda no es correcto");
        }
    }
}
