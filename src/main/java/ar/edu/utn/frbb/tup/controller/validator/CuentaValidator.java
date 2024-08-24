package ar.edu.utn.frbb.tup.controller.validator;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;

@Component
public class CuentaValidator {
    public void validate(CuentaDto cuentaDto) {
        List<String> tiposCuentaValidos = Arrays.asList("CC", "CA");
        List<String> monedasValidas = Arrays.asList("P", "D");

        if (!tiposCuentaValidos.contains(cuentaDto.getTipoCuenta())) {
            throw new IllegalArgumentException("El tipo de cuenta no es correcto");
        }
        if (!monedasValidas.contains(cuentaDto.getMoneda())) {
            throw new IllegalArgumentException("El tipo de moneda no es correcto");
        }
    }
}
