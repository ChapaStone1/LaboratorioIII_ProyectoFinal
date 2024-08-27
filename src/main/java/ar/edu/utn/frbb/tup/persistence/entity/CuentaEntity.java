package ar.edu.utn.frbb.tup.persistence.entity;


import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;


import java.time.LocalDateTime;
import java.util.List;

public class CuentaEntity extends BaseEntity{
    String nombre;
    LocalDateTime fechaCreacion;
    int balance;
    String tipoCuenta;
    String moneda;
    Long titular;
    long numeroCuenta;
    private List<Movimiento> movimientos;

    public CuentaEntity(Cuenta cuenta) {
        super(cuenta.getNumeroCuenta());
        this.balance = cuenta.getBalance();
        this.tipoCuenta = cuenta.getTipoCuenta().toString();
        this.titular = cuenta.getTitular().getDni();
        this.fechaCreacion = cuenta.getFechaCreacion();
        this.moneda = cuenta.getMoneda().toString();
        this.numeroCuenta = cuenta.getNumeroCuenta();
        this.movimientos = cuenta.getMovimientos();
    }

    public Cuenta toCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(this.balance);
        cuenta.setNumeroCuenta(this.numeroCuenta);
        cuenta.setTipoCuenta(TipoCuenta.valueOf(this.tipoCuenta));
        cuenta.setFechaCreacion(this.fechaCreacion);
        cuenta.setMoneda(TipoMoneda.valueOf(this.moneda));
        cuenta.setMovimientos(this.movimientos);
        return cuenta;
    }

    public Long getTitular() {
        return titular;
    }
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public long getNumeroCuenta() {
        return numeroCuenta;
    }

}
