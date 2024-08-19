package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;
import java.util.Random;

public class Movimiento {
    private static int contadorId = 0; // Variable estática para llevar la cuenta de los IDs
    private int id;
    private LocalDateTime fecha;
    private TipoMovimiento tipo;
    private double monto;
    private long cuentaOrigen;
    private long cuentaDestino;
    private long numTransaccion;
    private String descripcion;

    public Movimiento(TipoMovimiento tipo, double monto, long cuentaOrigen, long cuentaDestino) {
        this.id = ++contadorId;
        this.fecha = LocalDateTime.now();
        this.tipo = tipo;
        this.monto = monto;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.descripcion = generarDescripcion(); // Setea la descripción automáticamente
    }

    public Movimiento() {
        this.id = ++contadorId; // Incrementa y asigna el ID
        this.fecha = LocalDateTime.now();
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
    public int getId() {
        return id;
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
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setMonto(double monto) {
        this.monto = monto;
    }
    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
        this.descripcion = generarDescripcion(); // Actualiza la descripción si cambia el tipo
    }

    private String generarDescripcion() {
        switch (tipo) {
            case RETIRO:
                return "Retiro";
            case DEPOSITO:
                return "Depósito";
            case TRANSFERENCIA:
                return "Transferencia";
            default:
                return "Movimiento desconocido";
        }
    }

    public void guardarMovimiento(Cuenta cuenta, TipoMovimiento tipo, double monto, long cuentaOrigen, long cuentaDestino, String descripcion) {
        Movimiento movimiento = new Movimiento(tipo, monto, cuentaOrigen, cuentaDestino);
        movimiento.setDescripcion(descripcion != null ? descripcion : movimiento.generarDescripcion());

        switch (tipo) {
            case DEPOSITO:
                movimiento.setCuentaDestino(cuenta.getNumeroCuenta());
                break;

            case TRANSFERENCIA:
                movimiento.setCuentaOrigen(cuentaOrigen); // La cuenta de origen es la que se recibe como parámetro
                movimiento.setCuentaDestino(cuentaDestino); // La cuenta de destino es la que se recibe como parámetro
                break;

            case RETIRO:
                movimiento.setMonto(-monto); // El retiro se representa con un monto negativo
                movimiento.setCuentaOrigen(cuenta.getNumeroCuenta()); // Asumimos que el retiro es desde la cuenta actual
                break;

            default:
                throw new IllegalArgumentException("Tipo de movimiento no válido: " + tipo);
        }

        cuenta.agregarMovimiento(movimiento);
    }

    
    public void imprimirMovimiento() {
        System.out.println("Id: " + id + " Fecha: " + fecha + " Tipo: " + tipo + " Monto: " + monto + " CuentaOrigen: " + cuentaOrigen + " CuentaDestino: " + cuentaDestino);
    }
}
