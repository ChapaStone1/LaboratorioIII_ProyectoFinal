package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;

public class Cuenta {
    private long numeroCuenta;
    LocalDateTime fechaCreacion;
    double balance;
    TipoCuenta tipoCuenta;
    TipoMoneda moneda;

    @JsonIgnore
    Cliente titular;
    @JsonIgnore
    private Set<Movimiento> movimientos;

    public Cuenta() {
        this.numeroCuenta = 1000000L + new Random().nextLong(90000000);
        this.balance = 0;
        this.fechaCreacion = LocalDateTime.now();
        this.movimientos = new HashSet<>();
    }

    public Cuenta(CuentaDto cuentaDto) {
        this.numeroCuenta = 1000000L + new Random().nextLong(90000000);
        this.balance = 0;
        this.fechaCreacion = LocalDateTime.now();
        this.movimientos = new HashSet<>();
        this.tipoCuenta = TipoCuenta.fromString(cuentaDto.getTipoCuenta());  // Suponiendo que getTipoCuenta() existe en CuentaDto
        this.moneda = TipoMoneda.fromString(cuentaDto.getMoneda());          // Suponiendo que getMoneda() existe en CuentaDto
    }

    public Cliente getTitular() {
        return titular;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }


    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public Cuenta setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }
    
    public Cuenta setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
        return this;
    }


    public TipoMoneda getMoneda() {
        return moneda;
    }

    public Cuenta setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
        return this;
    }


    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Cuenta setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public double getBalance() {
        return balance;
    }

    public Cuenta setBalance(double balance) {
        this.balance = balance;
        return this;
    }
    public void addMovimiento(Movimiento movimiento) {
        this.movimientos.add(movimiento);
    }
    public Set<Movimiento> getMovimientos() {
        return movimientos;
    }
    public Cuenta setMovimientos( Set<Movimiento> movimientos) {
        this.movimientos = movimientos;
        return this;
    }


}
