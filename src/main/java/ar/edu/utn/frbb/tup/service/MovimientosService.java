package ar.edu.utn.frbb.tup.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.controller.dto.MovimientosDto;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;

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

    private MovimientosDto getMovimientos(Cuenta cuenta){
        List<Movimiento> movimientos = cuenta.getMovimientos(); // Obtener los movimientos directamente

        MovimientosDto respuestaMovimientosDto = new MovimientosDto();
        respuestaMovimientosDto.setNumeroCuenta(cuenta.getNumeroCuenta());
        respuestaMovimientosDto.setHistorialMovimientos(movimientos); // Asignar los movimientos directamente
        return respuestaMovimientosDto;
    }
}
