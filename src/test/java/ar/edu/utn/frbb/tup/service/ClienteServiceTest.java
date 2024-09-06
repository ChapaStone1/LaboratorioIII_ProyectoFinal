package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.CapturaInternacionalException;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.DeudorVerazException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;
    @Mock
    private InterpolService interpolService;
    @Mock
    private VerazService verazService;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testClienteMenor18Años() {
        ClienteDto clienteMenor = new ClienteDto();
        clienteMenor = clienteDtoMock();
        clienteMenor.setFechaNacimiento("2010-01-01");
        ClienteDto finalClienteMenor = clienteMenor;
        assertThrows(IllegalArgumentException.class, () -> clienteService.darDeAltaCliente(finalClienteMenor));
    }

    @Test
    public void testClienteSuccess() throws ClienteAlreadyExistsException, DeudorVerazException, CapturaInternacionalException {
        ClienteDto cliente = clienteDtoMock();

        when(clienteDao.find(cliente.getDni(), false)).thenReturn(null);  // Simula que el cliente no existe
        when(verazService.servicioDeVeraz(cliente.getDni())).thenReturn(false); // Simula que no tiene deudas
        when(interpolService.pedidoCapturaInternacional(cliente.getDni())).thenReturn(false); // Simula que no está en captura
        clienteService.darDeAltaCliente(cliente);

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);

        // Verificamos que el método save() haya sido llamado con algún Cliente y capturamos el objeto
        verify(clienteDao, times(1)).save(captor.capture());

        // Obtenemos el cliente guardado
        Cliente clienteGuardado = captor.getValue();

        // Verificamos que los valores sean los esperados
        assertEquals(cliente.getDni(), clienteGuardado.getDni());
        assertEquals(cliente.getNombre(), clienteGuardado.getNombre());
        assertEquals(cliente.getApellido(), clienteGuardado.getApellido());
    }

    @Test
    public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
        ClienteDto cliente = clienteDtoMock();

        when(clienteDao.find(cliente.getDni(), false)).thenReturn(new Cliente());

        ClienteDto finalCliente = cliente;
        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(finalCliente));
    }

    @Test
    public void testClienteCapturaInternacionalExcepcion() throws CapturaInternacionalException{
        ClienteDto cliente = clienteDtoMock();
        //Puse al profe con pedido de captura nomas
        cliente.setDni(26456437);

        when(clienteDao.find(cliente.getDni(), false)).thenReturn(null);
        when(interpolService.pedidoCapturaInternacional(26456437)).thenReturn(true);
        ClienteDto finalCliente = cliente;

        assertThrows(CapturaInternacionalException.class, () -> clienteService.darDeAltaCliente(finalCliente));
    }

    @Test
    public void testClienteTieneDeudaVeraz() throws DeudorVerazException{
        ClienteDto cliente = clienteDtoMock();
        cliente.setDni(10000019);
        // Para ser deudor de veraz el DNI debe ser numero primo
        when(clienteDao.find(cliente.getDni(), false)).thenReturn(null);
        when(verazService.servicioDeVeraz(cliente.getDni())).thenReturn(true);
        ClienteDto finalCliente = cliente;

        assertThrows(DeudorVerazException.class, () -> clienteService.darDeAltaCliente(finalCliente));
    }

    @Test
    public void testAgregarCuentaSuccess() throws TipoCuentaAlreadyExistsException{
        Cuenta cuenta = cuentaMock();
        Cliente cliente = clienteMock();

        when(clienteDao.find(37389808, true)).thenReturn(cliente);  // Simular búsqueda exitosa del cliente

        // Llamar al método que se está probando
        clienteService.agregarCuenta(cuenta, 37389808);

        // Verificar que se llamó a guardar el cliente actualizado en la base de datos
        verify(clienteDao, times(1)).save(cliente);
    }

    @Test
    public void testClienteNoExisteAlAgregarleCuenta() throws IllegalArgumentException{
        Cuenta cuenta = cuentaMock();
        Cliente cliente = clienteMock();

        when(clienteDao.find(37389808, true)).thenReturn(null);  // Simular búsqueda nula del cliente

        assertThrows(IllegalArgumentException.class, () -> clienteService.agregarCuenta(cuenta, clienteMock().getDni()));
    }

    //clientes y cuentas Mock
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
    private ClienteDto clienteDtoMock(){
        ClienteDto cliente = new ClienteDto();
        cliente.setDni(37389808);
        cliente.setFechaNacimiento("1993-01-16");
        cliente.setNombre("Juan"); 
        cliente.setApellido("Chaparro");
        cliente.setTipoPersona("F");
        return cliente;
    }
    private Cuenta cuentaMock(){
        Cliente cliente = new Cliente();
        cliente = clienteMock();
        Cuenta cuenta = new Cuenta();
        cuenta.setTitular(cliente);
        cuenta.setNumeroCuenta(231341546);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setBalance(0.0);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setFechaCreacion(LocalDateTime.now());
        cuenta.setMovimientos(new HashSet<>());
        return cuenta;
    }
}