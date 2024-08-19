package ar.edu.utn.frbb.tup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.controller.validator.RetirosValidator;
import ar.edu.utn.frbb.tup.service.RetirosService;

@RestController
@RequestMapping("/retiro")
public class RetirosController {
    @Autowired
    private RetirosService retiroService;

    @Autowired
    private RetirosValidator retiroValidator;

 //   @PostMapping
    
}
