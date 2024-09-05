package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.DepositosDto;
import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.*;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DepositosServiceTest {
    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private DepositosService depositosService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRealizarDepositoExitoso() throws Exception {
        DepositosDto depositoDto = mockDepositoDto();
        Cuenta cuenta = cuentaMock();
        RespuestaDto respuestaEsperada = mockRespuesta();

        when(cuentaService.buscarCuentaPorNumeroCuenta(depositoDto.getCuenta())).thenReturn(cuenta);
        when(cuentaService.agregarMovimientoDeposito(cuenta, depositoDto.getMonto(), cuenta.getNumeroCuenta())).thenReturn(respuestaEsperada);

        RespuestaDto respuesta = depositosService.realizarDeposito(depositoDto);

        assertEquals(respuestaEsperada, respuesta);

        verify(cuentaService, times(1)).agregarMovimientoDeposito(cuenta, depositoDto.getMonto(), cuenta.getNumeroCuenta());
    }

    @Test
    public void testRealizarDepositoCuentaNoExiste() {
        DepositosDto depositoDto = mockDepositoDto();

        // Configurar el mock
        when(cuentaService.buscarCuentaPorNumeroCuenta(depositoDto.getCuenta())).thenReturn(null);

        // Ejecutar el método y verificar excepción
        assertThrows(NotExistCuentaException.class, () -> depositosService.realizarDeposito(depositoDto));
    }

    @Test
    public void testRealizarDepositoTipoMonedaInvalido() {
        DepositosDto depositoDto = mockDepositoDto();
        depositoDto.setMoneda("D");
        Cuenta cuenta = cuentaMock();
        // Le paso la cuenta con tipomoneda Pesos y deposito Dolares
        when(cuentaService.buscarCuentaPorNumeroCuenta(depositoDto.getCuenta())).thenReturn(cuenta);
        //que me retorne la cuenta
        assertThrows(TipoMonedaInvalidoException.class, () -> depositosService.realizarDeposito(depositoDto));
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
    private RespuestaDto mockRespuesta(){
        RespuestaDto respuesta = new RespuestaDto();
        return respuesta;
    }
    private DepositosDto mockDepositoDto() {
        DepositosDto depositosDto = new DepositosDto();
        depositosDto.setCuenta(213213213);
        depositosDto.setMoneda("P");
        depositosDto.setMonto(10000);
        return depositosDto;
    }
}
