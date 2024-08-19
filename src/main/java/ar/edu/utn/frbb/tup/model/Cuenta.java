package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;

public class Cuenta {
    private long numeroCuenta;
    LocalDateTime fechaCreacion;
    int balance;
    TipoCuenta tipoCuenta;
    Cliente titular;
    TipoMoneda moneda;
    private List<Movimiento> movimientos;

    public Cuenta() {
        this.numeroCuenta = 1000000L + new Random().nextLong(90000000);
        this.balance = 0;
        this.fechaCreacion = LocalDateTime.now();
        this.movimientos = new ArrayList<>();
    }

    public Cuenta(CuentaDto cuentaDto) {
        this.numeroCuenta = 1000000L + new Random().nextLong(90000000);
        this.balance = 0;
        this.fechaCreacion = LocalDateTime.now();
        this.movimientos = new ArrayList<>();
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

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public Cuenta setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
        return this;
    }

    public void agregarMovimiento(Movimiento movimiento) {
        this.movimientos.add(movimiento);
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Cuenta setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public int getBalance() {
        return balance;
    }

    public Cuenta setBalance(int balance) {
        this.balance = balance;
        return this;
    }
    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void debitarDeCuenta(int cantidadADebitar) throws NoAlcanzaException, CantidadNegativaException {
        if (cantidadADebitar < 0) {
            throw new CantidadNegativaException();
        }

        if (balance < cantidadADebitar) {
            throw new NoAlcanzaException();
        }
        this.balance = this.balance - cantidadADebitar;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void forzaDebitoDeCuenta(int i) {
        this.balance = this.balance - i;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }




    /* 

    public void retiro(int monto) {
        if (monto > this.balance) {
            System.out.println("Saldo insuficiente para realizar el retiro.");
        } else {
            this.balance -= monto;
            System.out.println("Se retiraron " + monto + " unidades monetarias de la cuenta " + nombre);
            registrarMovimiento(new Movimiento(generarIdMovimiento(), TipoMovimiento.RETIRO, monto, this.CBU, this.CBU));
        }
    }

    public void deposito(int cantidad) {
        this.balance += cantidad;
        registrarMovimiento(new Movimiento(generarIdMovimiento(), TipoMovimiento.DEPOSITO, cantidad, this.CBU, this.CBU));
    }

    public void transferirDineroHaciaOtraCuenta(int cantidad, Cuenta cuentaDestino) {
        if (cantidad > 0 && cantidad <= getBalance()) {
            this.balance -= cantidad;
            cuentaDestino.deposito(cantidad);
            registrarMovimiento(new Movimiento(generarIdMovimiento(), TipoMovimiento.TRANSFERENCIA, cantidad, this.CBU, cuentaDestino.getCBU()));
        } else {
            System.out.println("No se puede realizar la transferencia.");
        }
    }


    // metodos de movimientos de cuenta
    private void registrarMovimiento(Movimiento movimiento) {
        this.movimientos.add(movimiento);
    }

    private int generarIdMovimiento() {
        return contadorMovimientos++;
    }

    public void imprimirMovimientos() {
        for (Movimiento movimiento : movimientos) {
            movimiento.imprimirMovimiento();
        }
    }

    // metodo de imprimir

    public void imprimirCuenta() {
        System.out.println("     Nombre: " + this.nombre);
        System.out.println("        Fecha de creacion: " + this.fechaCreacion);
        System.out.println("        Balance: " + this.balance);
        System.out.println("        CBU: " + this.CBU);
    }
*/
}
