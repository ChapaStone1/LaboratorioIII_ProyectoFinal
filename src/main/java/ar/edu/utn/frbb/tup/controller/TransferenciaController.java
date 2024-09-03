package ar.edu.utn.frbb.tup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciasDto;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistException;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.model.exception.TipoDeCuentasException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaInvalidoException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNotSupportedException;
import ar.edu.utn.frbb.tup.service.TransferenciaService;

@RestController
@RequestMapping("/api/v1/transferencia")
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService;

    @Autowired
    private TransferenciaValidator transferenciaValidator;

    //Endpoint para realizar una transferencia, ingresando Json con los campos necesarios (cuentaOrigen, cuentaDestino, monto y tipoMoneda)
   @PostMapping
    public RespuestaDto transferir(@RequestBody TransferenciasDto transferenciaDto) throws ClienteNotExistException, NotExistCuentaException, TipoDeCuentasException, TipoMonedaInvalidoException, TipoMonedaNotSupportedException {
        transferenciaValidator.validar(transferenciaDto);
        RespuestaDto respuesta = transferenciaService.realizarTransferencia(transferenciaDto);
        return respuesta;
    }   
}