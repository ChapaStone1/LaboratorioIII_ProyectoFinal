package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.InputErrorException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class ClienteValidator {

    public void validar(ClienteDto clienteDto) {    
        validateNombre(clienteDto.getNombre());
        validateApellido(clienteDto.getApellido());
        validateFechaNacimiento(clienteDto);
        validateTipoPersona(clienteDto);
        validateDni(clienteDto.getDni());        
    }

    public void validateNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
    }
    public void validateApellido(String apellido) {
        if (apellido == null || apellido.isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede ser nulo o vacío");
        }
    }

    public void validateFechaNacimiento(ClienteDto clienteDto) {
        String fechaNacimientoString = clienteDto.getFechaNacimiento();
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
            LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoString, DATE_FORMATTER);
            LocalDate fechaActual = LocalDate.now();

            if (fechaNacimiento.isAfter(fechaActual)) {
                throw new InputErrorException("Fecha de nacimiento inválida, está en el futuro.");
            }


        }  catch (DateTimeParseException | InputErrorException e) {
            throw new IllegalArgumentException("Fecha de nacimiento inválida, debe tener el formato: yyyy-MM-dd.");
        }
    }

    public void validateTipoPersona(ClienteDto clienteDto) {
        if (clienteDto.getTipoPersona() == null) {
            throw new IllegalArgumentException("El tipo de persona no puede ser nulo");
        }
        try {
            TipoPersona.fromString(clienteDto.getTipoPersona());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El tipo de persona no es correcto");
        }
    }
    
    public void validateDni(long dni) {
        if (dni <= 0) {
            throw new IllegalArgumentException("El DNI no es correcto");
        }
        if (dni > 99999999) {
            throw new IllegalArgumentException("El DNI no es correcto");
        }
        
    }
}
