package ar.edu.utn.frbb.tup.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.controller.validator.DepositosValidator;
import ar.edu.utn.frbb.tup.service.DepositosService;


@RestController
@RequestMapping("/deposito")
public class DepositosController {
    @Autowired
    private DepositosService depositoService;

    @Autowired
    private DepositosValidator depositoValidator;

    //@PostMapping
}
