package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
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
public class CuentaValidatorTest {

    @InjectMocks
    private CuentaValidator cuentaValidator;
    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidarCuentaSuccess() {
        CuentaDto cuentaDto = cuentaDtoMock();
        assertDoesNotThrow(() -> cuentaValidator.validar(cuentaDto)); // pasa el test perfecto
    }

    @Test
    public void testValidarTipoCuentaFail() {
        CuentaDto cuentaDto = cuentaDtoMock();
        cuentaDto.setTipoCuenta("CX"); // Tipo de cuenta invalido

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cuentaValidator.validar(cuentaDto);
        });
        assertEquals("El tipo de cuenta no es correcto", exception.getMessage());
    }

    @Test
    public void testValidarMonedaFail() {
        CuentaDto cuentaDto = cuentaDtoMock();
        cuentaDto.setMoneda("E");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cuentaValidator.validar(cuentaDto);
        });
        assertEquals("El tipo de moneda no es correcto", exception.getMessage());
    }

    // Mock de cuenta
    private CuentaDto cuentaDtoMock(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("CA");
        cuentaDto.setDni(37389808);
        cuentaDto.setMoneda("P");
        return cuentaDto;
    }
}
