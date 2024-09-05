package ar.edu.utn.frbb.tup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.controller.dto.RetirosDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistException;
import ar.edu.utn.frbb.tup.model.exception.InputErrorException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaInvalidoException;

@Service
public class RetirosService {

    @Autowired
    private CuentaService cuentaService;

     public RespuestaDto realizarRetiro(RetirosDto retiroDto) throws TipoMonedaInvalidoException, InputErrorException, NotExistCuentaException, NoAlcanzaException, ClienteNotExistException {
        Cuenta cuenta = cuentaService.buscarCuentaPorNumeroCuenta(retiroDto.getCuenta());

        if (cuenta == null) {
            throw new NotExistCuentaException("La cuenta " + retiroDto.getCuenta() + " no existe.");
        }

        if (!cuenta.getMoneda().equals(TipoMoneda.fromString(retiroDto.getMoneda()))) {
            throw new TipoMonedaInvalidoException("Las monedas del retiro y la cuenta no coinciden");
        }
        if (cuenta.getBalance() < retiroDto.getMonto()) {
            throw new NoAlcanzaException("El saldo de la cuenta es insuficiente");
        }
        return retirar(cuenta, retiroDto);
    }

    private RespuestaDto retirar(Cuenta cuenta, RetirosDto depositoDto) throws ClienteNotExistException {
        RespuestaDto respuesta = new RespuestaDto();
        double nuevoBalance = cuenta.getBalance() - depositoDto.getMonto();
        cuentaService.actualizarBalance(cuenta, nuevoBalance, TipoMovimiento.RETIRO);
        respuesta = cuentaService.agregarMovimientoRetiro(cuenta, depositoDto.getMonto(), cuenta.getNumeroCuenta());
        return respuesta;
    }
}
