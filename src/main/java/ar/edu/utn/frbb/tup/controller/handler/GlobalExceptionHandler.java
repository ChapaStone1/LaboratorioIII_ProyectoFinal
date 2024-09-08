package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.model.exception.*;

import java.lang.reflect.InvocationTargetException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<CustomApiError> buildErrorResponse(HttpStatus status, String message) {
        CustomApiError apiError = new CustomApiError();
        apiError.setErrorCode(status.value());
        apiError.setErrorMessage(message);
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(InputErrorException.class)
    public ResponseEntity<CustomApiError> handleInputErrorException(InputErrorException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Error en los datos de entrada: " + ex.getMessage());
    }

    @ExceptionHandler(TipoPersonaException.class)
    public ResponseEntity<CustomApiError> handleTipoPersonaException(TipoPersonaException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Error en los datos de entrada de tipo persona: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Argumento inválido: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<CustomApiError> handleIllegalStateException(IllegalStateException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Estado inválido: " + ex.getMessage());
    }

    @ExceptionHandler(ClienteAlreadyExistsException.class)
    public ResponseEntity<CustomApiError> handleClienteAlreadyExistsException(ClienteAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "El cliente ya existe: " + ex.getMessage());
    }

    @ExceptionHandler(ClienteNotExistException.class)
    public ResponseEntity<CustomApiError> handleClienteNotExistException(ClienteNotExistException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Cliente no encontrado: " + ex.getMessage());
    }

    @ExceptionHandler(CapturaInternacionalException.class)
    public ResponseEntity<CustomApiError> handleCapturaInternacionalException(CapturaInternacionalException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "El cliente posee pedido de captura internacional: " + ex.getMessage());
    }

    @ExceptionHandler(DeudorVerazException.class)
    public ResponseEntity<CustomApiError> handleDeudorVerazException(DeudorVerazException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "El cliente figura como deudor en los servicios de Veraz: " + ex.getMessage());
    }

    @ExceptionHandler(NotExistCuentaException.class)
    public ResponseEntity<CustomApiError> handleNotExistCuentaException(NotExistCuentaException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Cuenta no encontrada: " + ex.getMessage());
    }

    @ExceptionHandler(CuentaAlreadyExistsException.class)
    public ResponseEntity<CustomApiError> handleCuentaAlreadyExistsException(CuentaAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "La cuenta ya existe: " + ex.getMessage());
    }

    @ExceptionHandler(TipoCuentaAlreadyExistsException.class)
    public ResponseEntity<CustomApiError> handleTipoCuentaAlreadyExistsException(TipoCuentaAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Tipo de cuenta ya existe: " + ex.getMessage());
    }

    @ExceptionHandler(TipoCuentaNotSupportedException.class)
    public ResponseEntity<CustomApiError> handleTipoCuentaNotSupportedException(TipoCuentaNotSupportedException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Tipo de cuenta no soportado: " + ex.getMessage());
    }

    @ExceptionHandler(TipoDeCuentasException.class)
    public ResponseEntity<CustomApiError> handleTipoDeCuentasException(TipoDeCuentasException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Tipo de cuentas no soportadas para la operación: " + ex.getMessage());
    }

    @ExceptionHandler(NoAlcanzaException.class)
    public ResponseEntity<CustomApiError> handleNoAlcanzaException(NoAlcanzaException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Fondos insuficientes: " + ex.getMessage());
    }

    @ExceptionHandler(TipoMonedaInvalidoException.class)
    public ResponseEntity<CustomApiError> handleTipoMonedaInvalidoException(TipoMonedaInvalidoException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Tipo de moneda inválido: " + ex.getMessage());
    }

    @ExceptionHandler(TipoMonedaNotSupportedException.class)
    public ResponseEntity<CustomApiError> handleTipoMonedaNotSupportedException(TipoMonedaNotSupportedException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Tipo de moneda no soportado: " + ex.getMessage());
    }

    @ExceptionHandler(InvocationTargetException.class)
    public ResponseEntity<CustomApiError> handleInvocationTargetException(InvocationTargetException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error al invocar el método: " + ex.getTargetException().getMessage());
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CustomApiError> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        CustomApiError apiError = new CustomApiError();
        apiError.setErrorCode(HttpStatus.NOT_FOUND.value());
        apiError.setErrorMessage("Endpoint no encontrado: " + ex.getRequestURL());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}