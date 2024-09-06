package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.dto.MovimientosDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.MovimientosService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaControllerTest {
    @Mock
    private CuentaService cuentaService;
    @Mock
    private MovimientosService movimientosService;

    @Mock
    private CuentaValidator cuentaValidator;

    @InjectMocks
    private CuentaController cuentaController;

    @Test
    public void testCrearCuentaSuccess() {
        CuentaDto cuentaDto = cuentaDtoMock(); // Mock de CuentaDto
        Cuenta cuenta = cuentaMock(); // Mock de Cuenta

        doNothing().when(cuentaValidator).validar(cuentaDto);
        when(cuentaService.darDeAltaCuenta(cuentaDto)).thenReturn(cuenta);

        Cuenta response = cuentaController.crearCuenta(cuentaDto, null); // Ahora retorna directamente una Cuenta

        assertNotNull(response); // Verificamos que la respuesta no sea nula
        assertEquals(cuenta, response); // Verificamos que la cuenta devuelta sea la misma que el mock
        verify(cuentaValidator).validar(cuentaDto); // Verificamos que el validador fue llamado
        verify(cuentaService).darDeAltaCuenta(cuentaDto); // Verificamos que el servicio fue llamado
    }

    @Test
    public void testMostrarCuentaSuccess() {
        Cuenta cuenta = cuentaMock(); // Mock de Cuenta

        when(cuentaService.buscarCuentaPorNumeroCuenta(cuenta.getNumeroCuenta())).thenReturn(cuenta);

        Cuenta result = cuentaController.mostrarCuenta(cuenta.getNumeroCuenta(), null);

        assertNotNull(result); // Verificamos que la cuenta no sea nula
        assertEquals(cuenta.getNumeroCuenta(), result.getNumeroCuenta()); // Verificamos que el número de cuenta es correcto
        verify(cuentaService).buscarCuentaPorNumeroCuenta(cuenta.getNumeroCuenta()); // Verificamos que el servicio fue llamado correctamente
    }

    @Test
    public void testGetCuentasClienteSuccess() {
        long idCliente = 37389808L;
        Set<Cuenta> cuentas = new HashSet<>();
        Cuenta cuenta = cuentaMock();
        cuentas.add(cuenta);

        when(cuentaService.cuentasdeCliente(idCliente)).thenReturn(cuentas);

        Set<Cuenta> result = cuentaController.getCuentasCliente(idCliente, null);

        assertNotNull(result); // Verificamos que el conjunto de cuentas no sea nulo
        assertEquals(cuentas.size(), result.size()); // Verificamos que el tamaño del conjunto es correcto
        verify(cuentaService).cuentasdeCliente(idCliente); // Verificamos que el servicio fue llamado correctamente
    }

    @Test
    public void testGetMovimientosSuccess() {
        MovimientosDto movimientosDto = movimientoDtoMock();
        when(movimientosService.buscarMovimientosPorCuentaId(movimientosDto.getNumeroCuenta())).thenReturn(movimientosDto);

        ResponseEntity<MovimientosDto> response = cuentaController.getMovimientos(movimientosDto.getNumeroCuenta());

        assertNotNull(response); // Verificamos que la respuesta no sea nula
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Verificamos que el código de respuesta sea OK
        assertEquals(movimientosDto, response.getBody()); // Verificamos que el cuerpo de la respuesta sea correcto
        verify(movimientosService).buscarMovimientosPorCuentaId(movimientosDto.getNumeroCuenta()); // Verificamos que el servicio fue llamado correctamente
    }

    // Mocks de objetos para test
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

    private MovimientosDto movimientoDtoMock() {
        MovimientosDto movimientosDto = new MovimientosDto();
        movimientosDto.setNumeroCuenta(231341546);
        Set<Movimiento> movimientos = new HashSet<>();

        Movimiento movimiento = new Movimiento();
        movimiento.setFecha();
        movimiento.setMonto(1000);
        movimiento.setTipo(TipoMovimiento.DEPOSITO);
        movimiento.setCuentaOrigen(213165641);
        movimiento.setCuentaDestino(213165641);
        movimiento.setDescripcion("Deposito");
        movimientos.add(movimiento);

        movimientosDto.setRespuestaMovimientos(movimientos);
        return movimientosDto;
    }
}
