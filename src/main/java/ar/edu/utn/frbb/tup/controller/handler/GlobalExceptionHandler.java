package ar.edu.utn.frbb.tup.controller.handler;

import java.lang.reflect.InvocationTargetException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ar.edu.utn.frbb.tup.model.exception.CapturaInternacionalException;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotExistException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.DeudorVerazException;
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Error en los datos de entrada: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Argumento inválido: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Estado inválido: " + ex.getMessage());
    }

    // Clientes Exception
    @ExceptionHandler(ClienteAlreadyExistsException.class)
    public ResponseEntity<String> handleClienteAlreadyExistsException(ClienteAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body("El cliente ya existe: " + ex.getMessage());
    }

    @ExceptionHandler(ClienteNotExistException.class)
    public ResponseEntity<String> handleClienteNotExistException(ClienteNotExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body("Cliente no encontrado: " + ex.getMessage());
    }
    @ExceptionHandler(CapturaInternacionalException.class)
    public ResponseEntity<String> handleCapturaInternacionalException(CapturaInternacionalException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body("El cliente posee pedido de captura internacional: " + ex.getMessage());
    }
    @ExceptionHandler(DeudorVerazException.class)
    public ResponseEntity<String> handleDeudorVerazException(DeudorVerazException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body("El cliente figura como deudor en los servicios de Veraz: " + ex.getMessage());
    }

    // Cuentas Exception
    @ExceptionHandler(NotExistCuentaException.class)
    public ResponseEntity<String> handleNotExistCuentaException(NotExistCuentaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body("Cuenta no encontrada: " + ex.getMessage());
    }

    @ExceptionHandler(CuentaAlreadyExistsException.class)
    public ResponseEntity<String> handleCuentaAlreadyExistsException(CuentaAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body("La cuenta ya existe: " + ex.getMessage());
    }

    @ExceptionHandler(TipoCuentaAlreadyExistsException.class)
    public ResponseEntity<String> handleTipoCuentaAlreadyExistsException(TipoCuentaAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body("Tipo de cuenta ya existe: " + ex.getMessage());
    }

    @ExceptionHandler(TipoCuentaNotSupportedException.class)
    public ResponseEntity<String> handleTipoCuentaNotSupportedException(TipoCuentaNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Tipo de cuenta no soportado: " + ex.getMessage());
    }

    // Tipos de Cuentas exception (por cuentas no soportadas para transferir)
    @ExceptionHandler(TipoDeCuentasException.class)
    public ResponseEntity<String> handleTipoDeCuentasException(TipoDeCuentasException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Tipo de cuentas no soportadas para la operación: " + ex.getMessage());
    }
    
    // No alcanza plata exception
    @ExceptionHandler(NoAlcanzaException.class)
    public ResponseEntity<String> handleNoAlcanzaException(NoAlcanzaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Fondos insuficientes: " + ex.getMessage());
    }

    // Tipos de Moneda exception
    @ExceptionHandler(TipoMonedaInvalidoException.class)
    public ResponseEntity<String> handleTipoMonedaInvalidoException(TipoMonedaInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Tipo de moneda inválido: " + ex.getMessage());
    }

    @ExceptionHandler(TipoMonedaNotSupportedException.class)
    public ResponseEntity<String> handleTipoMonedaNotSupportedException(TipoMonedaNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Tipo de moneda no soportado: " + ex.getMessage());
    }

    @ExceptionHandler(InvocationTargetException.class)
    public ResponseEntity<String> handleInvocationTargetException(InvocationTargetException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error al invocar el método: " + ex.getTargetException().getMessage());
    }
    
}
