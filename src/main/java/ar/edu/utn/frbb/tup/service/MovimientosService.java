package ar.edu.utn.frbb.tup.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
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
        // Obtener y asignar los movimientos
        Set<Movimiento> movimientos = cuenta.getMovimientos().stream()
                .map(movimiento -> new Movimiento(
                        movimiento.getFecha(),
                        movimiento.getTipo(),
                        movimiento.getDescripcion(),
                        movimiento.getMonto()))
                .collect(Collectors.toSet());
        ; // Verifica que esto esté retornando los movimientos correctos

        // Asignar los movimientos al DTO
        respuestaMovimientosDto.setNumeroCuenta(cuenta.getNumeroCuenta());
        respuestaMovimientosDto.setRespuestaMovimientos(movimientos);
        
        return respuestaMovimientosDto;
    }
}
