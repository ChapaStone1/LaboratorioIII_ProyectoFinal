package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciasDto;
import ar.edu.utn.frbb.tup.model.exception.InputErrorException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaInvalidoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferenciaValidatorTest {
    @InjectMocks
    private TransferenciaValidator transferenciaValidator;
    
    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidarTransferenciaSuccess() {
        TransferenciasDto transferenciaDto = transfereciaDtoMock();
        assertDoesNotThrow(() -> transferenciaValidator.validar(transferenciaDto));
    }

    @Test
    public void testValidarMontoFail() {
        TransferenciasDto transferenciaDto = transfereciaDtoMock();
        transferenciaDto.setMonto(-100.00);

        assertThrows(InputErrorException.class, () -> transferenciaValidator.validar(transferenciaDto));
    }

    @Test
    public void testValidarTipoMonedaFail() {
        TransferenciasDto transferenciaDto = transfereciaDtoMock();
        transferenciaDto.setMoneda("e"); // Moneda inválida

        assertThrows(TipoMonedaInvalidoException.class, () -> transferenciaValidator.validar(transferenciaDto));
    }

    @Test
    public void testValidarCuentasIgualesFail() {
        TransferenciasDto transferenciaDto = transfereciaDtoMock();
        transferenciaDto.setCuentaDestino(transferenciaDto.getCuentaOrigen());

        assertThrows(IllegalArgumentException.class, () -> transferenciaValidator.validar(transferenciaDto));
    }

    // Mocks de TransferenciaDto
    private TransferenciasDto transfereciaDtoMock(){
        TransferenciasDto transferenciasDto = new TransferenciasDto();
        transferenciasDto.setCuentaDestino(41651321);
        transferenciasDto.setCuentaOrigen(41651322);
        transferenciasDto.setMoneda("P");
        transferenciasDto.setMonto(100.00);
        return transferenciasDto;
    }
}
