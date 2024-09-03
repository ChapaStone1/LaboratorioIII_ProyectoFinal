package ar.edu.utn.frbb.tup.model.exception;

public class CuentaAlreadyExistsException extends RuntimeException {
    public CuentaAlreadyExistsException(String message) {
        super(message);
    }
}
