package ar.edu.utn.frbb.tup.controller.validator;
import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;

import java.time.LocalDate;
public class CuentaValidator {
    public void validate(CuentaDto cuentaDto) {
        if (!"C".equals(cuentaDto.getTipoCuenta()) || !"A".equals(cuentaDto.getTipoCuenta())) {
            throw new IllegalArgumentException("El tipo de cuenta no es correcto");
        }
        if (!"P".equals(cuentaDto.getMoneda()) || !"D".equals(cuentaDto.getMoneda())) {
            throw new IllegalArgumentException("El tipo de moneda no es correcto");
        }
        
    }


}
