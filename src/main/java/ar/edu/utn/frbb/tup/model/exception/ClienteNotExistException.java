package ar.edu.utn.frbb.tup.model.exception;

public class ClienteNotExistException extends RuntimeException  {
    public ClienteNotExistException(String message) {
        super(message);
    }

}
