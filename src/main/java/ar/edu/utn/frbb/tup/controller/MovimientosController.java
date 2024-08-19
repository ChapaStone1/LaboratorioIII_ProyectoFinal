package ar.edu.utn.frbb.tup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ar.edu.utn.frbb.tup.controller.dto.MovimientosDto;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.service.MovimientosService;

public class MovimientosController {
   @Autowired
    private MovimientosService movimientoService;

        // Retrieve account movements by account ID
    @GetMapping("/{idcuenta}/movimientos")
    public ResponseEntity<MovimientosDto> getMovimientos(@PathVariable long idCuenta) throws NotExistCuentaException {

      MovimientosDto respuesta = movimientoService.buscarMovimientosPorCuentaId(idCuenta);
        return ResponseEntity.ok(respuesta);
    }
}
