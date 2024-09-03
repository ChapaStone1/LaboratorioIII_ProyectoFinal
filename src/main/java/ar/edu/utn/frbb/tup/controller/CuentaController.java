package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNotSupportedException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.MovimientosService;
import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.dto.MovimientosDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;


@RestController
@RequestMapping("/api/v1/cuenta")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaValidator cuentaValidator;

    @Autowired
    private MovimientosService movimientoService;

    @PostMapping
    public ResponseEntity<Cuenta> crearCuenta(@RequestBody CuentaDto cuentaDto, WebRequest request) 
            throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, 
                TipoCuentaNotSupportedException, TipoMonedaNotSupportedException, 
                ClienteNotExistException {
        
        // Valida el DTO
        cuentaValidator.validar(cuentaDto);
        Cuenta cuenta = cuentaService.darDeAltaCuenta(cuentaDto);
      
        // Retorna la cuenta con un estado HTTP 201 (Created)
        return new ResponseEntity<>(cuenta, HttpStatus.CREATED);
    }

    @GetMapping("/{idCuenta}")
    public Cuenta mostrarCuenta(@PathVariable("idCuenta") long idCuenta, WebRequest request) throws NotExistCuentaException {
        Cuenta cuenta = cuentaService.buscarCuentaPorNumeroCuenta(idCuenta);
        return cuenta;
    }

    @GetMapping("/{idcliente}/cuentas")
    public List<Cuenta> getCuentasCliente(@PathVariable("idcliente") long idcliente, WebRequest request) {
        return cuentaService.cuentasdeCliente(idcliente);
    }

    @GetMapping("/{idCuenta}/movimientos")
    public ResponseEntity<MovimientosDto> getMovimientos(@PathVariable("idCuenta") long idCuenta) throws NotExistCuentaException {
        MovimientosDto respuesta = movimientoService.buscarMovimientosPorCuentaId(idCuenta);      
        return ResponseEntity.ok(respuesta);
    }

}
