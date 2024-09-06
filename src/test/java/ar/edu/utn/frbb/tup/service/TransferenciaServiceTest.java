package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciasDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferenciaServiceTest {
    @Mock
    private CuentaService cuentaService;
    @Mock
    private BanelcoService banelcoService;

    @InjectMocks
    private TransferenciaService transferenciaService;

    @Test
    public void testRealizarTransferenciaMismoBancoExitoso() throws Exception {
        TransferenciasDto transferenciaDto = mockTransferenciaDto();
        Cuenta cuentaOrigen = cuentaOrigenMock();
        Cuenta cuentaDestino = cuentaDestinoMock();
        RespuestaDto respuestaEsperada = mockRespuesta();
        //Retornos de cuentas
        when(cuentaService.buscarCuentaPorNumeroCuenta(transferenciaDto.getCuentaOrigen())).thenReturn(cuentaOrigen);
        when(cuentaService.buscarCuentaPorNumeroCuenta(transferenciaDto.getCuentaDestino())).thenReturn(cuentaDestino);
        //Comisiones
        when(cuentaService.calcularComision(cuentaOrigen.getMoneda(), transferenciaDto.getMonto())).thenReturn(0.0);
        //Balances
        when(cuentaService.balanceCuentaOrigen(cuentaOrigen.getBalance(), transferenciaDto.getMonto(), 0.0)).thenReturn(4000.0);
        when(cuentaService.balanceCuentaDestino(cuentaDestino.getBalance(), transferenciaDto.getMonto(), 0.0)).thenReturn(1000.0);
        //Que retorne un movimiento mock al final
        when(cuentaService.agregarMovimientoTransferencia(any(Cuenta.class), anyDouble(), anyLong(), anyLong())).thenReturn(respuestaEsperada);

        // Ejecutar el método
        RespuestaDto respuesta = transferenciaService.realizarTransferencia(transferenciaDto);

        // Verificar resultados
        assertEquals(respuestaEsperada, respuesta);
        verify(cuentaService, times(1)).actualizarBalance(cuentaOrigen, 4000.0, TipoMovimiento.TRANSFERENCIA);
        verify(cuentaService, times(1)).actualizarBalance(cuentaDestino, 1000.0, TipoMovimiento.TRANSFERENCIA);
        verify(cuentaService, times(1)).agregarMovimientoTransferencia(cuentaOrigen, 1000.0, cuentaOrigen.getNumeroCuenta(), cuentaDestino.getNumeroCuenta());
        verify(cuentaService, times(1)).agregarMovimientoTransferencia(cuentaDestino, 1000.0, cuentaOrigen.getNumeroCuenta(), cuentaDestino.getNumeroCuenta());
    }

    @Test
    public void testRealizarTransferenciaBanelcoExitoso() throws Exception {
        TransferenciasDto transferenciaDto = mockTransferenciaDto();
        Cuenta cuentaOrigen = cuentaOrigenMock();
        RespuestaDto respuestaEsperada = mockRespuesta();
        //Retornos de cuentas
        when(cuentaService.buscarCuentaPorNumeroCuenta(transferenciaDto.getCuentaOrigen())).thenReturn(cuentaOrigen);
        when(cuentaService.buscarCuentaPorNumeroCuenta(transferenciaDto.getCuentaDestino())).thenReturn(null);
        when(banelcoService.servicioDeBanelco(transferenciaDto)).thenReturn(true);
        when(cuentaService.calcularComision(cuentaOrigen.getMoneda(), transferenciaDto.getMonto())).thenReturn(0.0);
        when(cuentaService.balanceCuentaOrigen(cuentaOrigen.getBalance(), transferenciaDto.getMonto(), 0.0)).thenReturn(4000.0);

        // Configurar movimiento
        when(cuentaService.agregarMovimientoTransferencia(any(Cuenta.class), anyDouble(), anyLong(), anyLong())).thenReturn(respuestaEsperada);

        RespuestaDto respuesta = transferenciaService.realizarTransferencia(transferenciaDto);

        // Verifico que los resultados sean los esperados
        assertEquals(respuestaEsperada, respuesta);
        verify(cuentaService, times(1)).actualizarBalance(cuentaOrigen, 4000.0, TipoMovimiento.TRANSFERENCIA);
        verify(cuentaService, times(1)).agregarMovimientoTransferencia(cuentaOrigen, 1000.0, cuentaOrigen.getNumeroCuenta(), transferenciaDto.getCuentaDestino());
    }

    @Test
    public void testRealizarTransferenciaBanelcoFail() throws Exception {
        TransferenciasDto transferenciaDto = mockTransferenciaDto();
        Cuenta cuentaOrigen = cuentaOrigenMock();
        RespuestaDto respuestaEsperada = mockRespuesta();
        respuestaEsperada.setEstado("FALLIDA");
        respuestaEsperada.setMensaje("No es posible realizar la transferencia, banco de destino no se encuentra en Red Banelco.");

        //Retornos de cuentas
        when(cuentaService.buscarCuentaPorNumeroCuenta(transferenciaDto.getCuentaOrigen())).thenReturn(cuentaOrigen);
        when(cuentaService.buscarCuentaPorNumeroCuenta(transferenciaDto.getCuentaDestino())).thenReturn(null);
        when(banelcoService.servicioDeBanelco(transferenciaDto)).thenReturn(false);

        RespuestaDto respuesta = transferenciaService.realizarTransferencia(transferenciaDto);

        // Verifico que los campos de respuesta sean iguales
        assertEquals(respuestaEsperada.getEstado(), respuesta.getEstado());
        assertEquals(respuestaEsperada.getMensaje(), respuesta.getMensaje());
        // Verifico que no se invoque nunca a los metodos de actualizar balance ni agregar movimiento
        verify(cuentaService, times(0)).actualizarBalance(any(Cuenta.class), anyDouble(), any(TipoMovimiento.class));
        verify(cuentaService, times(0)).agregarMovimientoTransferencia(any(Cuenta.class), anyDouble(), anyLong(), anyLong());
    }


    @Test
    public void testRealizarTransferenciaCuentaNoExiste() {
        TransferenciasDto transferenciaDto = mockTransferenciaDto();
        when(cuentaService.buscarCuentaPorNumeroCuenta(transferenciaDto.getCuentaOrigen())).thenReturn(null);

        // Ejecutar el método y verificar excepción
        assertThrows(NotExistCuentaException.class, () -> transferenciaService.realizarTransferencia(transferenciaDto));
    }

    @Test
    public void testMonedasNoCoinciden() {
        TransferenciasDto transferenciaDto = mockTransferenciaDto();
        Cuenta cuentaOrigen = cuentaOrigenMock();
        Cuenta cuentaDestino = cuentaDestinoMock();
        cuentaDestino.setMoneda(TipoMoneda.DOLARES);
        when(cuentaService.buscarCuentaPorNumeroCuenta(transferenciaDto.getCuentaOrigen())).thenReturn(cuentaOrigen);
        when(cuentaService.buscarCuentaPorNumeroCuenta(transferenciaDto.getCuentaDestino())).thenReturn(cuentaDestino);

        // Ejecutar el método y verificar excepción
        assertThrows(TipoMonedaNotSupportedException.class, () -> transferenciaService.realizarTransferencia(transferenciaDto));
    }

    @Test
    public void testTransferenciaNoAlcanzaDineroCuenta() throws Exception {
        TransferenciasDto transferenciaDto = mockTransferenciaDto();
        Cuenta cuentaOrigen = cuentaOrigenMock();
        Cuenta cuentaDestino = cuentaDestinoMock();
        // Le hago un set de balance en 50, sabiendo que el monto de transferenciasDto es 1000
        cuentaOrigen.setBalance(50.0);
        RespuestaDto respuestaEsperada = mockRespuesta();
        respuestaEsperada.setEstado("FALLIDA");
        respuestaEsperada.setMensaje("Saldo insuficiente para realizar la transferencia.");
        //Retornos de cuentas
        when(cuentaService.buscarCuentaPorNumeroCuenta(transferenciaDto.getCuentaOrigen())).thenReturn(cuentaOrigen);
        when(cuentaService.buscarCuentaPorNumeroCuenta(transferenciaDto.getCuentaDestino())).thenReturn(cuentaDestino);

        RespuestaDto respuesta = transferenciaService.realizarTransferencia(transferenciaDto);

        // Verifico que los campos de respuesta sean iguales
        assertEquals(respuestaEsperada.getEstado(), respuesta.getEstado());
        assertEquals(respuestaEsperada.getMensaje(), respuesta.getMensaje());
        // Verifico que no se invoque nunca a los metodos de actualizar balance ni agregar movimiento
        verify(cuentaService, times(0)).actualizarBalance(any(Cuenta.class), anyDouble(), any(TipoMovimiento.class));
        verify(cuentaService, times(0)).agregarMovimientoTransferencia(any(Cuenta.class), anyDouble(), anyLong(), anyLong());
    }

    // Clientes y cuentas Mock
    private Cuenta cuentaOrigenMock(){
        Cliente cliente = new Cliente();
        cliente = clienteMock();
        Cuenta cuenta = new Cuenta();
        cuenta.setTitular(cliente);
        cuenta.setNumeroCuenta(231341546);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setBalance(5000);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setFechaCreacion(LocalDateTime.now());
        cuenta.setMovimientos(new HashSet<>());
        return cuenta;
    }

    private Cuenta cuentaDestinoMock(){
        Cliente cliente = new Cliente();
        cliente = clienteMock();
        Cuenta cuenta = new Cuenta();
        cuenta.setTitular(cliente);
        cuenta.setNumeroCuenta(213165641);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setBalance(0.0);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setFechaCreacion(LocalDateTime.now());
        cuenta.setMovimientos(new HashSet<>());
        return cuenta;
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
    private RespuestaDto mockRespuesta(){
        RespuestaDto respuesta = new RespuestaDto();
        return respuesta;
    }
    private TransferenciasDto mockTransferenciaDto(){
        TransferenciasDto transferenciasDto = new TransferenciasDto();
        transferenciasDto.setMonto(1000);
        transferenciasDto.setCuentaDestino(213165641L);
        transferenciasDto.setCuentaOrigen(231341546L);
        transferenciasDto.setMoneda("P");
        return transferenciasDto;
    }
}
