package ar.edu.utn.frbb.tup.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.InputErrorException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.exception.NotExistCuentaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.model.exception.TipoDeCuentasException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaInvalidoException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNotSupportedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InputErrorException.class)
    public ResponseEntity<String> handleInputErrorException(InputErrorException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Clientes Exception
    @ExceptionHandler(ClienteAlreadyExistsException.class)
    public ResponseEntity<String> handleClienteAlreadyExistsException(ClienteAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ClienteNotExistException.class)
    public ResponseEntity<String> handleClienteNotExistException(ClienteNotExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Cuentas Exception
    @ExceptionHandler(NotExistCuentaException.class)
    public ResponseEntity<String> handleNotExistCuentaException(NotExistCuentaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CuentaAlreadyExistsException.class)
    public ResponseEntity<String> handleCuentaAlreadyExistsException(CuentaAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(TipoCuentaAlreadyExistsException.class)
    public ResponseEntity<String> handleTipoCuentaAlreadyExistsException(TipoCuentaAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(TipoCuentaNotSupportedException.class)
    public ResponseEntity<String> handleTipoCuentaNotSupportedException(TipoCuentaNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Tipos de Cuentas exception (por cuentas no soportadas para transferir)
    @ExceptionHandler(TipoDeCuentasException.class)
    public ResponseEntity<String> handleTipoDeCuentasException(TipoDeCuentasException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    
    // No alcanza plata exception
    @ExceptionHandler(NoAlcanzaException.class)
    public ResponseEntity<String> handleNoAlcanzaException(NoAlcanzaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Tipos de Moneda exception
    @ExceptionHandler(TipoMonedaInvalidoException.class)
    public ResponseEntity<String> handleTipoMonedaInvalidoException(TipoMonedaInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TipoMonedaNotSupportedException.class)
    public ResponseEntity<String> handleTipoMonedaNotSupportedException(TipoMonedaNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
