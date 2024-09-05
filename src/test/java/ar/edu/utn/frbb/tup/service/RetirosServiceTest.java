package ar.edu.utn.frbb.tup.service;
import ar.edu.utn.frbb.tup.controller.dto.DepositosDto;
import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.controller.dto.RetirosDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaInvalidoException;
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
public class RetirosServiceTest {
    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private RetirosService retirosService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRealizarRetiroExitoso() throws Exception {
        RetirosDto retiroDto = mockRetiroDto();
        Cuenta cuenta = cuentaMock();
        RespuestaDto respuestaEsperada = mockRespuesta();

        when(cuentaService.buscarCuentaPorNumeroCuenta(retiroDto.getCuenta())).thenReturn(cuenta);
        when(cuentaService.agregarMovimientoRetiro(cuenta, retiroDto.getMonto(), cuenta.getNumeroCuenta())).thenReturn(respuestaEsperada);

        RespuestaDto respuesta = retirosService.realizarRetiro(retiroDto);

        assertEquals(respuestaEsperada, respuesta);

        verify(cuentaService, times(1)).agregarMovimientoRetiro(cuenta, retiroDto.getMonto(), cuenta.getNumeroCuenta());
    }
    @Test
    public void testNoAlcalzaPlataRetiro() throws Exception {
        RetirosDto retiroDto = mockRetiroDto();
        Cuenta cuenta = cuentaMock();
        // cuenta tiene 50000 de balance
        retiroDto.setMonto(1000000);

        when(cuentaService.buscarCuentaPorNumeroCuenta(retiroDto.getCuenta())).thenReturn(cuenta);
        assertThrows(NoAlcanzaException.class, () -> retirosService.realizarRetiro(retiroDto));
    }
    @Test
    public void testRealizarRetiroCuentaNoExiste() {
        RetirosDto retiroDto = mockRetiroDto();

        // Configurar el mock
        when(cuentaService.buscarCuentaPorNumeroCuenta(retiroDto.getCuenta())).thenReturn(null);

        // Ejecutar el método y verificar excepción
        assertThrows(NotExistCuentaException.class, () -> retirosService.realizarRetiro(retiroDto));
    }

    @Test
    public void testRealizarRetiroTipoMonedaInvalido() {
        RetirosDto retiroDto = mockRetiroDto();
        retiroDto.setMoneda("D");
        Cuenta cuenta = cuentaMock();
        // Le paso la cuenta con tipomoneda Pesos y deposito Dolares
        when(cuentaService.buscarCuentaPorNumeroCuenta(retiroDto.getCuenta())).thenReturn(cuenta);
        //que me retorne la cuenta
        assertThrows(TipoMonedaInvalidoException.class, () -> retirosService.realizarRetiro(retiroDto));
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
        cuenta.setBalance(50000);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setFechaCreacion(LocalDateTime.now());
        cuenta.setMovimientos(new HashSet<>());
        return cuenta;
    }
    private RespuestaDto mockRespuesta(){
        RespuestaDto respuesta = new RespuestaDto();
        return respuesta;
    }
    private RetirosDto mockRetiroDto() {
        RetirosDto retirosDto = new RetirosDto();
        retirosDto.setCuenta(213213213);
        retirosDto.setMoneda("P");
        retirosDto.setMonto(10000);
        return retirosDto;
    }
}
