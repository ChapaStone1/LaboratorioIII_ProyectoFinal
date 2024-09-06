package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.MovimientosDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.*;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MovimientosServiceTest {
    @Mock
    private CuentaDao cuentaDao;

    @InjectMocks
    private MovimientosService movimientosService;

    @Test
    public void testBuscarMovimientosPorCuenta() throws NotExistCuentaException {
        Cuenta cuenta = cuentaMock();
        long cuentaId = cuenta.getNumeroCuenta();
        when(cuentaDao.findByNumeroCuenta(cuentaId)).thenReturn(cuenta);

        // Llamar al método de servicio
        MovimientosDto movimientosDto = movimientosService.buscarMovimientosPorCuentaId(cuentaId);

        // Verificar que el DTO contenga los movimientos correctos
        assertNotNull(movimientosDto);
        assertEquals(cuentaId, movimientosDto.getNumeroCuenta());
        assertEquals(1, movimientosDto.getMovimientosCuenta().size()); // Verificar que haya un movimiento

        Movimiento mov = movimientosDto.getMovimientosCuenta().iterator().next();
        assertEquals(TipoMovimiento.DEPOSITO, mov.getTipo());
        assertEquals(1000, mov.getMonto());
    }

    @Test
    public void testBuscarMovimientosPorCuentaNoExistCuenta(){
        when(cuentaDao.findByNumeroCuenta(37389808)).thenReturn(null);
        NotExistCuentaException thrown = assertThrows(NotExistCuentaException.class, () -> movimientosService.buscarMovimientosPorCuentaId(37389808));
        assertNotNull(thrown.getMessage());
    }

    private Cuenta cuentaMock(){
        Cliente cliente = new Cliente();
        cliente = new Cliente();
        Cuenta cuenta = new Cuenta();
        cuenta.setTitular(cliente);
        cuenta.setNumeroCuenta(213165641);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setBalance(0.0);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setFechaCreacion(LocalDateTime.now());
        cuenta.setMovimientos(movimientoMock());
        return cuenta;
    }
    private Set<Movimiento> movimientoMock() {
        Set<Movimiento> movimientos = new HashSet<>();
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha();
        movimiento.setMonto(1000);
        movimiento.setTipo(TipoMovimiento.DEPOSITO);
        movimiento.setCuentaOrigen(213165641);
        movimiento.setCuentaDestino(213165641);
        movimiento.setDescripcion("Deposito");
        movimientos.add(movimiento);
        return movimientos;
    }
}
