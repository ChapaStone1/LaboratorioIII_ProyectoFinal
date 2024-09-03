package ar.edu.utn.frbb.tup.model.exception;

public class ClienteAlreadyExistsException extends RuntimeException  {
    public ClienteAlreadyExistsException(String message) {
        super(message);
    }
}
