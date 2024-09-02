package ar.edu.utn.frbb.tup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.controller.dto.DepositosDto;
import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.InputErrorException;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaInvalidoException;

@Service
public class DepositosService {

    @Autowired
    private CuentaService cuentaService;

     public RespuestaDto realizarDeposito(DepositosDto depositoDto) throws TipoMonedaInvalidoException, InputErrorException, NotExistCuentaException {
        Cuenta cuenta = cuentaService.buscarCuentaPorNumeroCuenta(depositoDto.getCuenta());
        if (cuenta == null) {
            throw new NotExistCuentaException("La cuenta " + depositoDto.getCuenta() + " no existe.");
        }

        if (!(String.valueOf(cuenta.getMoneda())).equals(depositoDto.getMoneda())) {
            throw new TipoMonedaInvalidoException("Las monedas de las cuentas no coinciden");
        }
        return depositar(cuenta, depositoDto);
    }

    private RespuestaDto depositar(Cuenta cuenta, DepositosDto depositoDto) {
        RespuestaDto respuesta = new RespuestaDto();
        double nuevoBalance = cuenta.getBalance() + depositoDto.getMonto();
        cuentaService.actualizarBalance(cuenta, nuevoBalance, TipoMovimiento.DEPOSITO);
        respuesta = cuentaService.agregarMovimientoDeposito(cuenta, depositoDto.getMonto(), cuenta.getNumeroCuenta());
        return respuesta;
    }
    
}
