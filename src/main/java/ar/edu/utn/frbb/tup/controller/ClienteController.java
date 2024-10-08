package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.CapturaInternacionalException;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.DeudorVerazException;
import ar.edu.utn.frbb.tup.model.exception.TipoPersonaException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteValidator clienteValidator;

    @PostMapping
    public Cliente crearCliente(@RequestBody ClienteDto clienteDto) throws TipoPersonaException, ClienteAlreadyExistsException, DeudorVerazException, CapturaInternacionalException {
        clienteValidator.validar(clienteDto);
        return clienteService.darDeAltaCliente(clienteDto);
    }
    
    @GetMapping("/{dni}")
    public Cliente getCliente(@PathVariable long dni) {
        clienteValidator.validateDni(dni);
        return clienteService.buscarClientePorDni(dni);
    }
}
