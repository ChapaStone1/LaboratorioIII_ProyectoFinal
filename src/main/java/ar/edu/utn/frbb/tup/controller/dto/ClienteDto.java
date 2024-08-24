package ar.edu.utn.frbb.tup.controller.dto;


public class ClienteDto extends PersonaDto{
    private String tipoPersona;

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }
}
