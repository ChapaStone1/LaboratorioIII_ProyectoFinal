package ar.edu.utn.frbb.tup.controller.dto;
import java.util.HashSet;
import java.util.Set;

import ar.edu.utn.frbb.tup.model.Movimiento;

public class MovimientosDto {

    private long numeroCuenta;
    private Set<Movimiento> movimientosCuenta;

    // Constructor sin parámetros
    public MovimientosDto() {
        this.movimientosCuenta = new HashSet<>();
    }

    // Constructor con parámetros
    public MovimientosDto(long numeroCuenta, Set<Movimiento> movimientosCuenta) {
        this.numeroCuenta = numeroCuenta;
        this.movimientosCuenta = movimientosCuenta;
    }

    // Getters y Setters
    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Set<Movimiento> getMovimientosCuenta() {
        return movimientosCuenta;
    }

    public void setRespuestaMovimientos(Set<Movimiento> movimientosCuenta) {
        this.movimientosCuenta = movimientosCuenta;
    }
}