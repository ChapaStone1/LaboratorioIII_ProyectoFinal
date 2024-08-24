package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNotSupportedException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CuentaService {
    CuentaDao cuentaDao = new CuentaDao();

    @Autowired
    ClienteService clienteService;

    public Cuenta darDeAltaCuenta(CuentaDto cuentadto) throws ClienteNotExistException, CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNotSupportedException, TipoMonedaNotSupportedException {
        Cuenta cuenta = parsearCuenta(cuentadto);
        
        if(cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }
        if (!tipoCuentaYMonedaSoportada(cuenta)) {
            throw new TipoCuentaNotSupportedException("El tipo de cuenta " + cuenta.getTipoCuenta() + " y/o el tipo de moneda " + cuenta.getMoneda() + " no está soportado.");
        }
        if (cuentaMismoTipo(cuenta, cuentasdeCliente(cuentadto.getDni()))) {
            throw new TipoCuentaAlreadyExistsException("El cliente " + cuentadto.getDni() + " ya tiene una cuenta de tipo " + cuenta.getTipoCuenta() + ".");
        }
        
        clienteService.agregarCuenta(cuenta, cuentadto.getDni());
        cuentaDao.save(cuenta);
        return cuenta;
    }
  

    // Validaciones de cuenta
    // Valido que cuenta y moneda sean soportadas
    private boolean tipoCuentaYMonedaSoportada(Cuenta cuenta) {
        return (cuenta.getTipoCuenta() == TipoCuenta.CAJA_AHORRO && (cuenta.getMoneda() == TipoMoneda.PESOS || cuenta.getMoneda() == TipoMoneda.DOLARES)) ||
               (cuenta.getTipoCuenta() == TipoCuenta.CUENTA_CORRIENTE && cuenta.getMoneda() == TipoMoneda.PESOS);
    }

    // Valido que el cliente no tenga cuentas del mismo tipo
    private boolean cuentaMismoTipo(Cuenta cuenta, List<Cuenta> cuentasCliente) {
        for (Cuenta cuentaDelCliente : cuentasCliente) {
            if (cuentaDelCliente.getTipoCuenta() == cuenta.getTipoCuenta()) {
                return true;
            }
        }
        return false;
    }
    // Creo metodo para que me devuelva la lista de un cliente, en caso de que no tenga me devuelve lista vacia
    public List<Cuenta> cuentasdeCliente(long dni){
        List<Cuenta> cuentas = cuentaDao.getCuentasByCliente(dni);
        return cuentas != null ? cuentas : new ArrayList<>();
    }


    // Retorno la lista de todas las cuentas de un cliente para el GET de cuentas de cliente

    public Cuenta buscarCuentaPorNumeroCuenta(long numeroCuenta) throws NotExistCuentaException {
        if (cuentaDao.findByNumeroCuenta(numeroCuenta) != null){
            return cuentaDao.findByNumeroCuenta(numeroCuenta);
        } 

        else{
            throw new NotExistCuentaException("Número de cuenta no existe.");
        }
    }


    private Cuenta parsearCuenta(CuentaDto cuentaDto) throws ClienteNotExistException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        Cliente titular = clienteService.buscarClientePorDni(cuentaDto.getDni());
        cuenta.setTitular(titular);
        return cuenta;
    }

}