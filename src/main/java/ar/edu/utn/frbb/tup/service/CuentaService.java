package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNotSupportedException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
public class CuentaService {
    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    ClienteService clienteService;

    public Cuenta darDeAltaCuenta(CuentaDto cuentadto) throws ClienteNotExistException, CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNotSupportedException, TipoMonedaNotSupportedException {
        Cuenta cuenta = parsearCuenta(cuentadto);

        if (cuenta.getTitular() == null) {
            throw new IllegalStateException("El titular de la cuenta no está asignado.");
        }
        
        if(cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }
        // Valido que cuenta y moneda sean soportadas CA en pesos o CA en dolares y CC en pesos
        if (!tipoCuentaYMonedaSoportada(cuenta)) {
            throw new TipoCuentaNotSupportedException("El tipo de cuenta " + cuenta.getTipoCuenta() + " y/o el tipo de moneda " + cuenta.getMoneda() + " no está soportado.");
        }

        if (cuentaYMonedaMismoTipo(cuenta, cuentasdeCliente(cuentadto.getDni()))) {
            throw new TipoCuentaAlreadyExistsException("El cliente " + cuentadto.getDni() + " ya tiene una cuenta de tipo " + cuenta.getTipoCuenta() + ".");
        }
        
        clienteService.agregarCuenta(cuenta, cuentadto.getDni());
        cuentaDao.save(cuenta);
        System.out.println(cuenta.getTitular().getApellido());
        System.out.println(cuenta.getNumeroCuenta());
        return cuenta;
    }
  
    // Validaciones de cuenta
    // Valido que cuenta y moneda sean soportadas CA en pesos o CA en dolares y CC en pesos
    private boolean tipoCuentaYMonedaSoportada(Cuenta cuenta) {
        return (cuenta.getTipoCuenta() == TipoCuenta.CAJA_AHORRO && (cuenta.getMoneda() == TipoMoneda.PESOS || cuenta.getMoneda() == TipoMoneda.DOLARES)) || (cuenta.getTipoCuenta() == TipoCuenta.CUENTA_CORRIENTE && cuenta.getMoneda() == TipoMoneda.PESOS);
    }

    // Valido que el cliente no tenga cuentas del mismo tipo
    private boolean cuentaYMonedaMismoTipo(Cuenta cuenta, Set<Cuenta> set) {
        for (Cuenta cuentaDelCliente : set) {
            if (cuentaDelCliente.getTipoCuenta() == cuenta.getTipoCuenta() && cuentaDelCliente.getMoneda() == cuenta.getMoneda()) {
                return true;
            }
        }
        return false;
    }
    // Creo metodo para que me devuelva la lista de un cliente, en caso de que no tenga me devuelve lista vacia
    public Set<Cuenta> cuentasdeCliente(long dni){
        Set<Cuenta> cuentas = cuentaDao.getCuentasByCliente(dni);
        return cuentas != null ? cuentas : new HashSet<>();
    }
    // Retorno la lista de todas las cuentas de un cliente para el GET de cuentas de cliente

    public Cuenta buscarCuentaPorNumeroCuenta(long numeroCuenta) throws NotExistCuentaException {
        if (cuentaDao.findByNumeroCuenta(numeroCuenta) != null){
            return cuentaDao.findByNumeroCuenta(numeroCuenta);
        } 
        else{
            return null;
        }
    }

    private Cuenta parsearCuenta(CuentaDto cuentaDto) throws ClienteNotExistException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        Cliente titular = clienteService.buscarClientePorDni(cuentaDto.getDni());
        cuenta.setTitular(titular);
        return cuenta;
    }
    
    private Cuenta buscarCuentaPorNumeroCuenta(Set<Cuenta> cuentas, long numeroCuenta) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getNumeroCuenta() == numeroCuenta) {
                return cuenta;
            }
        }
        return null;
    }

    // metodos para calcular balances y comisiones
    public double calcularComision(TipoMoneda moneda, double monto) {
        if (moneda == TipoMoneda.PESOS && monto >= 1000000) {
            return 0.02 * monto;
        } else if (moneda == TipoMoneda.DOLARES && monto >= 5000) {
            return 0.005 * monto;
        }
        else{
            return 0;
        }
    }

    public double balanceCuentaOrigen(double balance, double monto, double comision){
        return balance - monto - comision;
    }

    public double balanceCuentaDestino(double balance, double monto, double comision){
        return balance + (monto - comision);
    }

    public void actualizarCuenta(Cuenta cuenta){
        cuentaDao.actualizarCuenta(cuenta);
    }

    public void actualizarBalance(Cuenta cuenta, double nuevoBalance, TipoMovimiento movimiento) throws ClienteNotExistException{
        if (cuenta.getTitular() == null) {
            throw new IllegalStateException("La cuenta no tiene un titular asignado.");
        }
        
        cuenta.setBalance(nuevoBalance);

        cuentaDao.actualizarCuenta(cuenta);

        Cliente clienteOrigen = clienteDao.find(cuenta.getTitular().getDni(), true);
        if (clienteOrigen != null) {
            actualizarCuentaEnCliente(clienteOrigen, cuenta);
        } else {
            throw new ClienteNotExistException("El cliente con DNI " + cuenta.getTitular().getDni() + " no existe.");
        }
        actualizarCuentaEnCliente(clienteOrigen, cuenta);
    }

    private void actualizarCuentaEnCliente(Cliente cliente, Cuenta cuentaActualizada) {
        if (cliente != null) {
            Set<Cuenta> cuentas = cliente.getCuentas();
            Cuenta cuentaExistente = buscarCuentaPorNumeroCuenta(cuentas, cuentaActualizada.getNumeroCuenta());

            if (cuentaExistente != null) {
                cuentas.remove(cuentaExistente);
                cuentas.add(cuentaActualizada);
                clienteDao.actualizarCliente(cliente);
            }
        }
    }

    // metodos para agregar movimientos al historial
    public RespuestaDto agregarMovimientoTransferencia(Cuenta cuenta, TipoMovimiento movimiento, double monto, long numCuentaOrigen, long numCuentaDestino){
        Movimiento movimientoNuevo = new Movimiento();
        RespuestaDto respuesta = new RespuestaDto();
        movimientoNuevo = movimientoNuevo.guardarMovimiento(cuenta, movimiento, monto, numCuentaOrigen, numCuentaDestino);
        respuesta.setEstado("EXITOSA");
        respuesta.setMensaje("Transferencia Exitosa. Número de transferencia: " + movimientoNuevo.getNumMovimiento() + ". Realizado el " + movimientoNuevo.getFecha());
        return respuesta;
    }

    public RespuestaDto agregarMovimientoRetiro(Cuenta cuenta, double monto, long numCuentaOrigen){
        Movimiento movimientoNuevo = new Movimiento();
        RespuestaDto respuesta = new RespuestaDto();
        movimientoNuevo = movimientoNuevo.guardarMovimiento(cuenta, TipoMovimiento.RETIRO, monto, numCuentaOrigen, numCuentaOrigen);
        respuesta.setEstado("EXITOSA");
        respuesta.setMensaje("Retiro Exitoso. Número de movimiento: " + movimientoNuevo.getNumMovimiento() + ". Realizado el " + movimientoNuevo.getFecha());
        return respuesta;
    }

    public RespuestaDto agregarMovimientoDeposito(Cuenta cuenta, double monto, long numCuentaOrigen){
        Movimiento movimientoNuevo = new Movimiento();
        RespuestaDto respuesta = new RespuestaDto();
        movimientoNuevo = movimientoNuevo.guardarMovimiento(cuenta, TipoMovimiento.DEPOSITO, monto, numCuentaOrigen, numCuentaOrigen);
        respuesta.setEstado("EXITOSA");
        respuesta.setMensaje("Deposito Exitoso. Número de movimiento: " + movimientoNuevo.getNumMovimiento() + ". Realizado el " + movimientoNuevo.getFecha());
        return respuesta;
    }
}