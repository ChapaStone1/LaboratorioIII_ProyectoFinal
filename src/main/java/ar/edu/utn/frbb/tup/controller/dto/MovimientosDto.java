package ar.edu.utn.frbb.tup.controller.dto;
import java.util.ArrayList;
import java.util.List;
import ar.edu.utn.frbb.tup.model.Movimiento;

public class MovimientosDto {

    private long numeroCuenta;
    private List<Movimiento> movimientosCuenta;

    // Constructor sin parámetros
    public MovimientosDto() {
        this.movimientosCuenta = new ArrayList<>();
    }

    // Constructor con parámetros
    public MovimientosDto(long numeroCuenta, List<Movimiento> movimientosCuenta) {
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

    public List<Movimiento> getMovimientosCuenta() {
        return movimientosCuenta;
    }

    public void setHistorialMovimientos(List<Movimiento> movimientosCuenta) {
        this.movimientosCuenta = movimientosCuenta;
    }
}