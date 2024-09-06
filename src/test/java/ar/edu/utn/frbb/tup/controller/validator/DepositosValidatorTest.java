package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.DepositosDto;
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
public class DepositosValidatorTest {
    @InjectMocks
    private DepositosValidator depositosValidator;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidarDepositoSuccess() {
        DepositosDto depositoDto = depositosDtoMock(); // Mock de DepositosDto con datos válidos

        assertDoesNotThrow(() -> depositosValidator.validar(depositoDto)); // No debe lanzar ninguna excepción
    }

    @Test
    public void testValidarNumeroCuentaFail() {
        DepositosDto depositoDto = depositosDtoMock();
        depositoDto.setCuenta(123456789); // Número de cuenta con más de 8 dígitos

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            depositosValidator.validar(depositoDto);
        });
        assertEquals("Numero de cuenta no valido.", exception.getMessage());
    }

    @Test
    public void testValidarMontoFail() {
        DepositosDto depositoDto = depositosDtoMock();
        depositoDto.setMonto(-50.00); // Monto negativo

        InputErrorException exception = assertThrows(InputErrorException.class, () -> {
            depositosValidator.validar(depositoDto);
        });
        assertEquals("Monto no valido.", exception.getMessage());
    }

    @Test
    public void testValidarMonedaFail() {
        DepositosDto depositoDto = depositosDtoMock();
        depositoDto.setMoneda("E");

        InputErrorException exception = assertThrows(InputErrorException.class, () -> {
            depositosValidator.validar(depositoDto);
        });
        assertEquals("Tipo de moneda no valido.", exception.getMessage());
    }

    // Mocks de DepositosDto
    private DepositosDto depositosDtoMock(){
        DepositosDto depositosDto = new DepositosDto();
        depositosDto.setCuenta(41651321);
        depositosDto.setMoneda("P");
        depositosDto.setMonto(100.00);
        return depositosDto;
    }
}
