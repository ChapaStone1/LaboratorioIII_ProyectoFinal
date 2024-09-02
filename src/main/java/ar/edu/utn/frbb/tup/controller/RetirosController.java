package ar.edu.utn.frbb.tup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.controller.dto.RetirosDto;
import ar.edu.utn.frbb.tup.controller.validator.RetirosValidator;
import ar.edu.utn.frbb.tup.model.exception.InputErrorException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaInvalidoException;
import ar.edu.utn.frbb.tup.service.RetirosService;

@RestController
@RequestMapping("/api/v1/retiro")
public class RetirosController {
    @Autowired
    private RetirosService retiroService;

    @Autowired
    private RetirosValidator retiroValidator;

    @PostMapping
    public RespuestaDto realizarRetiro(@RequestBody RetirosDto retiroDto) throws TipoMonedaInvalidoException, InputErrorException, NotExistCuentaException, NoAlcanzaException {
        retiroValidator.validar(retiroDto);
        return retiroService.realizarRetiro(retiroDto);
    }

    
}
