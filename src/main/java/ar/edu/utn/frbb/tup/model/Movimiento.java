package ar.edu.utn.frbb.tup.model;
import java.time.LocalDateTime;
import java.util.Random;

public class Movimiento {
    private static int contadorId = 0;
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
        this.numTransaccion = generarNumTransaccion();
    }

    public Movimiento() {
        this.id = ++contadorId;
        this.fecha = LocalDateTime.now();
        this.numTransaccion = generarNumTransaccion();
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
    }

    public long generarNumTransaccion() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    private String generarDescripcion(double monto, TipoMovimiento tipo, long cuentaOrigen, long cuentaDestino) {
        switch (tipo) {
            case RETIRO:
                return "Retiro en cuenta: " + cuentaOrigen + " - Monto: " + monto + " - Fecha: " + LocalDateTime.now();
            case DEPOSITO:
                return "Depósito en cuenta: " + cuentaDestino + " - Monto: " + monto + " - Fecha: " + LocalDateTime.now();
            case TRANSFERENCIA:
                return "Transferencia de cuenta: " + cuentaOrigen + " a cuenta: " + cuentaDestino + " - Monto: " + monto + " - Fecha: " + LocalDateTime.now();
            default:
                return "Movimiento desconocido";
        }
    }

    public Movimiento guardarMovimiento(Cuenta cuenta, TipoMovimiento tipo, double monto, long cuentaOrigen, long cuentaDestino, String descripcion) {
        Movimiento movimiento = new Movimiento(tipo, monto, cuentaOrigen, cuentaDestino);
        movimiento.setDescripcion(descripcion != null ? descripcion : movimiento.generarDescripcion(monto, tipo, cuentaOrigen, cuentaDestino));

        switch (tipo) {
            case DEPOSITO:
                movimiento.setCuentaDestino(cuenta.getNumeroCuenta());
                break;
            case TRANSFERENCIA:
                movimiento.setCuentaOrigen(cuentaOrigen);
                movimiento.setCuentaDestino(cuentaDestino);
                break;
            case RETIRO:
                movimiento.setMonto(-monto);
                movimiento.setCuentaOrigen(cuenta.getNumeroCuenta());
                break;
            default:
                throw new IllegalArgumentException("Tipo de movimiento no válido: " + tipo);
        }

        cuenta.agregarMovimiento(movimiento);
        return movimiento; // Retorna la instancia de Movimiento
    }
}