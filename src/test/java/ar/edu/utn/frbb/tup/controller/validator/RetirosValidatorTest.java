package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.RetirosDto;
import ar.edu.utn.frbb.tup.model.exception.InputErrorException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RetirosValidatorTest {
    @InjectMocks
    private RetirosValidator retirosValidator;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidarRetiroSuccess() {
        RetirosDto retiroDto = retirosDtoMock();
        assertDoesNotThrow(() -> retirosValidator.validar(retiroDto)); // No debe lanzar ninguna excepción
    }

    @Test
    public void testValidarNumeroCuentaFail() {
        RetirosDto retiroDto = retirosDtoMock();
        retiroDto.setCuenta(123456789); // Número de cuenta con más de 8 dígitos

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            retirosValidator.validar(retiroDto);
        });
        assertEquals("El número de cuenta no es valido.", exception.getMessage());
    }

    @Test
    public void testValidarMontoFail() {
        RetirosDto retiroDto = retirosDtoMock();
        retiroDto.setMonto(-50.00); // Monto negativo

        InputErrorException exception = assertThrows(InputErrorException.class, () -> {
            retirosValidator.validar(retiroDto);
        });
        assertEquals("Monto ingresado no es valido.", exception.getMessage());
    }

    @Test
    public void testValidarMonedaFail() {
        RetirosDto retiroDto = retirosDtoMock();
        retiroDto.setMoneda("E"); // Moneda no válida

        InputErrorException exception = assertThrows(InputErrorException.class, () -> {
            retirosValidator.validar(retiroDto);
        });
        assertEquals("Tipo de moneda ingresado no es valido.", exception.getMessage());
    }

    // Mock de RetirosDto
    private RetirosDto retirosDtoMock(){
        RetirosDto retirosDto = new RetirosDto();
        retirosDto.setCuenta(41651321);
        retirosDto.setMoneda("P");
        retirosDto.setMonto(100.00);
        return retirosDto;
    }
}
