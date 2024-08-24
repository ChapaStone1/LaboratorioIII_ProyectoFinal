package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.TipoPersona;

import java.time.LocalDate;

@Component
public class ClienteValidator {


    public void validate(ClienteDto clienteDto) {
       if (clienteDto.getTipoPersona() == null) {
        throw new IllegalArgumentException("El tipo de persona no puede ser nulo");
    }
    TipoPersona tipoPersona;
    try {
        tipoPersona = TipoPersona.fromString(clienteDto.getTipoPersona());
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("El tipo de persona no es correcto");
    }
        try {
            LocalDate.parse(clienteDto.getFechaNacimiento());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error en el formato de fecha");
        }
    }
    
    public void validateDNI(long dni) {
        if (dni <= 0) {
            throw new IllegalArgumentException("El DNI no es correcto");
        }
        if (dni > 99999999) {
            throw new IllegalArgumentException("El DNI no es correcto");
        }
        
    }
}
