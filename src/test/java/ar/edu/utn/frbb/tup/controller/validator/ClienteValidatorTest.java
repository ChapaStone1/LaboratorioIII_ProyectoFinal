package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;

import ar.edu.utn.frbb.tup.model.exception.TipoPersonaException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteValidatorTest {
    @InjectMocks
    private ClienteValidator clienteValidator;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidarClienteSuccess() {
        ClienteDto clienteDto = clienteDtoMock(); 

        assertDoesNotThrow(() -> clienteValidator.validar(clienteDto)); // No debe lanzar ninguna excepción
    }

    @Test
    public void testValidarNombreFail() {
        ClienteDto clienteDto = clienteDtoMock();
        clienteDto.setNombre(""); // Asignamos un nombre vacío

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteValidator.validar(clienteDto);
        });
        assertEquals("El nombre no puede ser nulo o vacío", exception.getMessage());
    }

    @Test
    public void testValidarTipoPersonaFail() {
        ClienteDto clienteDto = clienteDtoMock();
        clienteDto.setTipoPersona("X"); // Asignamos un tipo de persona inválido

        TipoPersonaException exception = assertThrows(TipoPersonaException.class, () -> {
            clienteValidator.validar(clienteDto);
        });
        assertEquals("El tipo de persona no es correcto", exception.getMessage());
    }

    @Test
    public void testValidarFechaNacimientoFail() {
        ClienteDto clienteDto = clienteDtoMock();
        clienteDto.setFechaNacimiento("2050-01-01"); // Asignamos una fecha de nacimiento futura

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteValidator.validar(clienteDto);
        });
        assertEquals("Fecha de nacimiento inválida, debe tener el formato: yyyy-MM-dd.", exception.getMessage());
    }

    // Mocks de objetos
    private ClienteDto clienteDtoMock(){
        ClienteDto cliente = new ClienteDto();
        cliente.setDni(37389808);
        cliente.setFechaNacimiento("1993-01-16");
        cliente.setNombre("Juan");
        cliente.setApellido("Chaparro");
        cliente.setTipoPersona("F");
        return cliente;
    }
}
