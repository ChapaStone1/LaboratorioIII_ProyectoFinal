package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.*;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaServiceTest {

    @Mock
    private CuentaDao cuentaDao;
    @Mock
    private Movimiento movimientoMock;
    @Mock
    private ClienteDao clienteDao;
    @Mock
    ClienteService clienteService;

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAltaCuentaSuccess(){
        Cliente clienteMock = clienteMock();
        CuentaDto cuentaDtoMock = cuentaDtoMock();

        when(clienteService.buscarClientePorDni(cuentaDtoMock.getDni())).thenReturn(clienteMock);
        // Simular que el cliente no tiene cuentas previas del mismo tipo
        when(cuentaDao.getCuentasByCliente(cuentaDtoMock.getDni())).thenReturn(new HashSet<>());

        // Llamar al método de servicio para dar de alta la cuenta
        Cuenta cuentaCreada = cuentaService.darDeAltaCuenta(cuentaDtoMock);

        // Verificar que el método save del DAO se haya llamado
        verify(cuentaDao, times(1)).save(cuentaCreada);

        // Asegurarse de que la cuenta creada no sea nula
        assertNotNull(cuentaCreada);
        // Verificar otros aspectos como el tipo de cuenta, moneda, y balance inicial
        assertEquals(TipoCuenta.CAJA_AHORRO, cuentaCreada.getTipoCuenta());
        assertEquals(TipoMoneda.PESOS, cuentaCreada.getMoneda());
        assertEquals(0.0, cuentaCreada.getBalance());
    }
    @Test
    public void testTipoCuentaAlreadyExist() throws TipoCuentaAlreadyExistsException {
        Cliente clienteMock = clienteMock();
        CuentaDto cuentaDtoMock = cuentaDtoMock();
        Cuenta cuentaExistente = cuentaMock(); // Mock de una cuenta existente para el cliente
        Set <Cuenta> cuentas = new HashSet<>();
        cuentas.add(cuentaExistente);
        // Simulación: El cliente existe y ya tiene una cuenta del mismo tipo
        when(clienteService.buscarClientePorDni(cuentaDtoMock.getDni())).thenReturn(clienteMock);
        when(cuentaDao.getCuentasByCliente(clienteMock.getDni())).thenReturn(cuentas);
        // El cliente ya tiene una cuenta CA en PESOS

        // Verifica que se lanza la excepción "TipoCuentaAlreadyExistsException"
        assertThrows(TipoCuentaAlreadyExistsException.class, () -> { cuentaService.darDeAltaCuenta(cuentaDtoMock);});

        // Verifica que se llamó al método para obtener las cuentas del cliente
        verify(cuentaDao, times(1)).getCuentasByCliente(clienteMock.getDni());
    }

    @Test
    public void testCuentasDeClienteConCuentas() {
        long dni = 37389808L;
        Set<Cuenta> cuentasMock = new HashSet<>();
        cuentasMock.add(cuentaMock());

        // Simulación: El cliente tiene cuentas
        when(cuentaDao.getCuentasByCliente(dni)).thenReturn(cuentasMock);

        // Llamar al método cuentasdeCliente
        Set<Cuenta> resultado = cuentaService.cuentasdeCliente(dni);

        // Verificar que el resultado contiene las cuentas mock
        assertEquals(cuentasMock, resultado);
        verify(cuentaDao, times(1)).getCuentasByCliente(dni);
    }

    @Test
    public void testCuentasDeClienteSinCuentas() {
        long dni = 37389808L;

        // Simulación: El cliente no tiene cuentas (se devuelve null)
        when(cuentaDao.getCuentasByCliente(dni)).thenReturn(null);

        // Llamar al método cuentasdeCliente
        Set<Cuenta> resultado = cuentaService.cuentasdeCliente(dni);

        // Verificar que el resultado es un conjunto vacío
        assertTrue(resultado.isEmpty());
        verify(cuentaDao, times(1)).getCuentasByCliente(dni);
    }
    @Test
    public void testBuscarCuentaPorNumeroCuentaSuccess() throws NotExistCuentaException {
        long numeroCuenta = 231341546L;
        Cuenta cuentaMock = cuentaMock(); // Usa un mock de la cuenta para el test

        // Simulación: El número de cuenta existe en la base de datos
        when(cuentaDao.findByNumeroCuenta(numeroCuenta)).thenReturn(cuentaMock);

        // Llamar al método
        Cuenta cuentaResultado = cuentaService.buscarCuentaPorNumeroCuenta(numeroCuenta);

        // Verificar que el resultado no sea null y sea igual a la cuenta mock
        assertNotNull(cuentaResultado);
        assertEquals(cuentaMock, cuentaResultado);

        // Verificar que se llamó al método findByNumeroCuenta exactamente dos veces
        verify(cuentaDao, times(2)).findByNumeroCuenta(numeroCuenta);
    }

    @Test
    public void testCalcularComisionPesosMontoMayorA1000000() {
        // Llamar al método
        double comision = cuentaService.calcularComision(TipoMoneda.PESOS, 10000000.0);

        // Calcular la comisión esperada
        double comisionEsperada = 0.02 * 10000000.0;

        // Verificar que la comisión calculada es igual a la esperada
        assertEquals(comisionEsperada, comision, 0.01); // Utiliza un margen de error adecuado
    }

    @Test
    public void testActualizarBalanceSuccess() throws ClienteNotExistException {
        // Datos de prueba
        Cuenta cuenta = cuentaMock();
        double nuevoBalance = 5000.0;
        TipoMovimiento movimiento = TipoMovimiento.DEPOSITO;
        Cliente clienteMock = clienteMock();

        // Simulación de comportamiento esperado
        when(clienteDao.find(clienteMock.getDni(), true)).thenReturn(clienteMock);
        doNothing().when(cuentaDao).actualizarCuenta(cuenta);

        // Llamar al método
        cuentaService.actualizarBalance(cuenta, nuevoBalance, movimiento);

        // Verificar las interacciones con los mocks
        verify(cuentaDao, times(1)).actualizarCuenta(cuenta);
        // Verificar que la cuenta ha sido actualizada con el nuevo balance
        assertEquals(nuevoBalance, cuenta.getBalance());
    }

    @Test
    public void testAgregarMovimientoTransferencia() {
        Cuenta cuenta = cuentaMock();
        TipoMovimiento tipoMovimiento = TipoMovimiento.TRANSFERENCIA;
        double monto = 1000.0;
        long numCuentaOrigen = 231341546;
        long numCuentaDestino = 987654321;

        // Ejecutar el método a probar
        RespuestaDto respuesta = cuentaService.agregarMovimientoTransferencia(cuenta, tipoMovimiento, monto, numCuentaOrigen, numCuentaDestino);

        // Verificar resultados
        assertEquals("EXITOSA", respuesta.getEstado()); // Ajusta según la respuesta esperada
        assertNotNull(respuesta);
        assertNotNull(respuesta.getMensaje());
    }
    @Test
    public void testAgregarMovimientoRetiro() {
        Cuenta cuenta = cuentaMock();
        double monto = 1000.0;
        long numCuentaOrigen = 231341546;
        long numCuentaDestino = 987654321;

        // Ejecutar el método a probar
        RespuestaDto respuesta = cuentaService.agregarMovimientoRetiro(cuenta, monto, numCuentaOrigen);

        // Verificar resultados
        assertEquals("EXITOSA", respuesta.getEstado()); // Ajusta según la respuesta esperada
        assertNotNull(respuesta);
        assertNotNull(respuesta.getMensaje());
    }
    @Test
    public void testAgregarMovimientoDeposito() {
        Cuenta cuenta = cuentaMock();
        double monto = 1000.0;
        long numCuentaOrigen = 231341546;
        long numCuentaDestino = 987654321;

        // Ejecutar el método a probar
        RespuestaDto respuesta = cuentaService.agregarMovimientoDeposito(cuenta, monto, numCuentaOrigen);

        // Verificar resultados
        assertEquals("EXITOSA", respuesta.getEstado()); // Ajusta según la respuesta esperada
        assertNotNull(respuesta);
        assertNotNull(respuesta.getMensaje());
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
    private CuentaDto cuentaDtoMock(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("CA");
        cuentaDto.setDni(37389808);
        cuentaDto.setMoneda("P");
        return cuentaDto;
    }

    
}
