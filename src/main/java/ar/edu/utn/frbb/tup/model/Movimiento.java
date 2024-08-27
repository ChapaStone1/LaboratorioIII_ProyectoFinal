package ar.edu.utn.frbb.tup.model;
import java.time.LocalDateTime;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Movimiento {
    private LocalDateTime fecha;
    private TipoMovimiento tipo;
    private double monto;
    @JsonIgnore
    private long cuentaOrigen;
    @JsonIgnore
    private long cuentaDestino;
    private String descripcion;
    private long numTransaccion;
    

    public Movimiento(LocalDateTime fecha, TipoMovimiento tipo, String descripcion, double monto) {
        this.numTransaccion = generarNumTransaccion();
        this.fecha = LocalDateTime.now();
        this.tipo = tipo;
        this.descripcion = descripcion;    
        this.monto = monto;
    }

    public Movimiento() {
    }
    
    public long getCuentaDestino() {
        return cuentaDestino;
    }
    public long getCuentaOrigen() {
        return cuentaOrigen;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public long getNumTransaccion() {
        return numTransaccion;
    }
    public double getMonto() {
        return monto;
    }
    public TipoMovimiento getTipo() {
        return tipo;
    }
    public void setCuentaDestino(long cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }
    public void setCuentaOrigen(long cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setFecha() {
        this.fecha = LocalDateTime.now();
    }
    public void setMonto(double monto) {
        this.monto = monto;
    }
    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    private long generarNumTransaccion() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    private String generarDescripcion(double monto, TipoMovimiento tipo, long cuentaOrigen, long cuentaDestino) {
        switch (tipo) {
            case RETIRO:
                return "Retiro en cuenta: " + cuentaOrigen;
            case DEPOSITO:
                return "Depósito en cuenta: " + cuentaDestino;
            case TRANSFERENCIA:
                return "Transferencia de cuenta" + cuentaOrigen + " a cuenta: " + cuentaDestino; 
            default:
                return "Movimiento desconocido";
        }
    }

    public Movimiento guardarMovimiento(Cuenta cuenta, TipoMovimiento tipo, double monto, long cuentaOrigen, long cuentaDestino) {
        LocalDateTime fecha = LocalDateTime.now();
        Movimiento movimiento = new Movimiento(fecha,tipo, "", monto);
        switch (tipo) {
            case DEPOSITO:
                movimiento.setCuentaDestino(cuenta.getNumeroCuenta());
                movimiento.setDescripcion(generarDescripcion(monto, tipo, cuentaOrigen, cuentaDestino));
                break;
            case TRANSFERENCIA:
                movimiento.setCuentaOrigen(cuentaOrigen);
                movimiento.setCuentaDestino(cuentaDestino);
                movimiento.setDescripcion(generarDescripcion(monto, tipo, cuentaOrigen, cuentaDestino));
                break;
            case RETIRO:
                movimiento.setMonto(-monto);
                movimiento.setCuentaOrigen(cuenta.getNumeroCuenta());
                movimiento.setDescripcion(generarDescripcion(monto, tipo, cuentaOrigen, cuentaDestino));
                break;
            default:
                throw new IllegalArgumentException("Tipo de movimiento no válido: " + tipo);
        }
        cuenta.addMovimiento(movimiento);
        return movimiento; // Retorna la instancia de Movimiento
    }
}