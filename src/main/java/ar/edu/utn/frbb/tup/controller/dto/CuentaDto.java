package ar.edu.utn.frbb.tup.controller.dto;

public class CuentaDto {
    private String tipoCuenta;
    private long dni;
    private String moneda;
    
    
    public long getDni() {
        return dni;
    }
    public String getMoneda() {
        return moneda;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

}
