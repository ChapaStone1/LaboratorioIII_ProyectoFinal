package ar.edu.utn.frbb.tup.service;
import java.nio.file.OpenOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciasDto;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistException;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.model.exception.TipoDeCuentasException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;

@Service
public class TransferenciaService {

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private BanelcoService banelcoService;

    public Operacion realizarTransferencia(TransferenciasDto transferenciaDto) throws ClienteNotExistException, NotExistCuentaException, TipoDeCuentasException{
        // Obtener las cuentas



        return mensaje;
    }
    
}
