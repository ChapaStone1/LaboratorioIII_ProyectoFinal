package ar.edu.utn.frbb.tup.model.exception;

public class TipoCuentaAlreadyExistsException extends RuntimeException  {
    public TipoCuentaAlreadyExistsException(String message) {
        super(message);
    }
}
