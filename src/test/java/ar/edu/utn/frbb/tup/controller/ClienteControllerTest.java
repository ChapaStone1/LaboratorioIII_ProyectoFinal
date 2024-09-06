package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.CapturaInternacionalException;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.DeudorVerazException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteControllerTest {
    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteValidator clienteValidator;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    public void testCrearClienteSuccess() throws ClienteAlreadyExistsException, DeudorVerazException, CapturaInternacionalException {
        ClienteDto clienteDto = clienteDtoMock(); // Creamos un ClienteDto de prueba
        Cliente cliente = new Cliente(); // Creamos un Cliente de prueba

        when(clienteService.darDeAltaCliente(clienteDto)).thenReturn(cliente);

        Cliente result = clienteController.crearCliente(clienteDto);

        assertNotNull(result); // Verificamos que el cliente no sea nulo
        assertEquals(cliente.getDni(), result.getDni()); // Verificamos que el cliente retornado es el mismo que el de mock
        verify(clienteValidator).validar(clienteDto); // Verificamos que el validador fue llamado
        verify(clienteService).darDeAltaCliente(clienteDto); // Verificamos que el servicio fue llamado
    }

    @Test
    public void testGetClienteSuccess() {
        Cliente cliente = clienteMock(); // Cliente de prueba

        doNothing().when(clienteValidator).validateDni(cliente.getDni());

        when(clienteService.buscarClientePorDni(cliente.getDni())).thenReturn(cliente);

        Cliente result = clienteController.getCliente(cliente.getDni());

        assertNotNull(result); // Verificamos que el cliente no sea nulo
        assertEquals(cliente.getDni(), result.getDni()); // Verificamos que el cliente retornado es el mismo que el de mock
        verify(clienteValidator).validateDni(cliente.getDni()); // Verificamos que el validador fue llamado
        verify(clienteService).buscarClientePorDni(cliente.getDni()); // Verificamos que el servicio fue llamado
    }

    // Objetos mock de test
    private ClienteDto clienteDtoMock(){
        ClienteDto cliente = new ClienteDto();
        cliente.setDni(37389808);
        cliente.setFechaNacimiento("1993-01-16");
        cliente.setNombre("Juan");
        cliente.setApellido("Chaparro");
        cliente.setTipoPersona("F");
        return cliente;
    }
    
    private Cliente clienteMock(){
        Cliente cliente = new Cliente();
        cliente.setDni(37389808);
        LocalDate fechanacimiento = LocalDate.of(1993, 1, 16);
        cliente.setFechaNacimiento(fechanacimiento);
        cliente.setNombre("Juan");
        cliente.setApellido("Chaparro");
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);
        cliente.setFechaAlta(LocalDate.now());
        cliente.setBanco("Banco UTN - TUP");
        return cliente;
    }
    
}
