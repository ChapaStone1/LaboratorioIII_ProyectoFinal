package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNotSupportedException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;


@RestController
@RequestMapping("/cuenta")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaValidator cuentaValidator;

    // Create a new account
    @PostMapping
    public Cuenta crearCuenta(@RequestBody CuentaDto cuentaDto, WebRequest request) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNotSupportedException, TipoMonedaNotSupportedException, ClienteNotExistException {
        cuentaValidator.validate(cuentaDto);
        return cuentaService.darDeAltaCuenta(cuentaDto);
    }

    @GetMapping("/{numeroCuenta}")
    public Cuenta mostrarCuenta(@PathVariable long numeroCuenta, WebRequest request) throws NotExistCuentaException {
        Cuenta cuenta = cuentaService.buscarCuentaPorNumeroCuenta(numeroCuenta);
        return cuenta;
    }

    @GetMapping("/{idcliente}/cuentas")
    public List<Cuenta> getCuentasCliente(@PathVariable long id, WebRequest request) {
        return cuentaService.cuentasdeCliente(id);
    }
}
