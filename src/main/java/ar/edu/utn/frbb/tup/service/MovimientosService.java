package ar.edu.utn.frbb.tup.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.controller.dto.MovimientosDto;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.stereotype.Service;

@Service
public class MovimientosService {
    @Autowired
    private CuentaDao cuentaDao;

    public MovimientosDto buscarMovimientosPorCuentaId(long cuentaId) throws NotExistCuentaException {
        Cuenta cuenta = cuentaDao.findByNumeroCuenta(cuentaId);
        if (cuenta == null) {
            throw new NotExistCuentaException("No existe una cuenta con el numero de cuenta: " + cuentaId);
        }
        return getMovimientos(cuenta);
    }

    private MovimientosDto getMovimientos(Cuenta cuenta) {
        MovimientosDto respuestaMovimientosDto = new MovimientosDto();
        
        // Crear y guardar el nuevo movimiento de consulta
        TipoMovimiento tipoMovimiento = TipoMovimiento.CONSULTA_MOVIMIENTOS;
        Movimiento nuevoMovimiento = new Movimiento();
        nuevoMovimiento = nuevoMovimiento.guardarMovimiento(cuenta, tipoMovimiento, 0, cuenta.getNumeroCuenta(), cuenta.getNumeroCuenta());
        
        // Actualizar la cuenta si es necesario
       // cuentaDao.save(cuenta); // Guarda la cuenta actualizada en la base de datos
        
        // Obtener y asignar los movimientos
        List<Movimiento> movimientos = cuenta.getMovimientos().stream()
                .map(movimiento -> new Movimiento(
                        movimiento.getTipo(),
                        movimiento.getMonto(),
                        movimiento.getCuentaOrigen(),
                        movimiento.getCuentaDestino()))
                .collect(Collectors.toList());; // Verifica que esto esté retornando los movimientos correctos
        respuestaMovimientosDto.setNumeroCuenta(cuenta.getNumeroCuenta());
        respuestaMovimientosDto.setHistorialMovimientos(movimientos);
        
        return respuestaMovimientosDto;
    }
}
