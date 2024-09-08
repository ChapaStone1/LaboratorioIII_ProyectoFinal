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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Set;


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
    public Cuenta crearCuenta(@RequestBody CuentaDto cuentaDto, WebRequest request)  throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNotSupportedException, TipoMonedaNotSupportedException, ClienteNotExistException {
        cuentaValidator.validar(cuentaDto);
        return cuentaService.darDeAltaCuenta(cuentaDto);
    }

    @GetMapping("/{idCuenta}")
    public Cuenta mostrarCuenta(@PathVariable("idCuenta") long idCuenta, WebRequest request) throws NotExistCuentaException {
        Cuenta cuenta = cuentaService.buscarCuentaPorNumeroCuenta(idCuenta);
        if (cuenta == null) {
            throw new NotExistCuentaException("La cuenta no existe");
        }
        return cuenta;
    }

    @GetMapping("/{idcliente}/cuentas")
    public Set<Cuenta> getCuentasCliente(@PathVariable("idcliente") long idcliente, WebRequest request) {
        return cuentaService.cuentasdeCliente(idcliente);
    }

    @GetMapping("/{idCuenta}/movimientos")
    public ResponseEntity<MovimientosDto> getMovimientos(@PathVariable("idCuenta") long idCuenta) throws NotExistCuentaException {
        MovimientosDto respuesta = movimientoService.buscarMovimientosPorCuentaId(idCuenta);      
        return ResponseEntity.ok(respuesta);
    }

}
