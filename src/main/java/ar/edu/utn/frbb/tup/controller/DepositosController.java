package ar.edu.utn.frbb.tup.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.controller.dto.DepositosDto;
import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.controller.validator.DepositosValidator;
import ar.edu.utn.frbb.tup.model.exception.InputErrorException;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaInvalidoException;
import ar.edu.utn.frbb.tup.service.DepositosService;


@RestController
@RequestMapping("/api/v1/deposito")
public class DepositosController {
    @Autowired
    private DepositosService depositoService;

    @Autowired
    private DepositosValidator depositoValidator;

    @PostMapping
    public RespuestaDto realizarDeposito(@RequestBody DepositosDto depositoDto) throws TipoMonedaInvalidoException, InputErrorException, NotExistCuentaException {
        depositoValidator.validar(depositoDto);
        return depositoService.realizarDeposito(depositoDto);
    }
}
