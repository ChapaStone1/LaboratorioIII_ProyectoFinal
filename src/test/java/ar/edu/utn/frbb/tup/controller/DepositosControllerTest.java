package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.DepositosDto;
import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.controller.validator.DepositosValidator;
import ar.edu.utn.frbb.tup.service.DepositosService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DepositosControllerTest {

    @Mock
    private DepositosService depositoService;

    @Mock
    private DepositosValidator depositoValidator;

    @InjectMocks
    private DepositosController depositosController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testDepositoSuccess(){
        DepositosDto depositoDto = depositosDtoMock(); // Mock de DepositosDto
        RespuestaDto respuestaDto = respuestaDtoMock(); // Mock de RespuestaDto

        when(depositoService.realizarDeposito(depositoDto)).thenReturn(respuestaDto);
        RespuestaDto result = depositosController.realizarDeposito(depositoDto);

        assertNotNull(result); // Verificamos que la respuesta no sea nula
        assertEquals(respuestaDto.getEstado(), result.getEstado()); // Verificamos que el estado de la respuesta es correcto
        assertEquals(respuestaDto.getMensaje(), result.getMensaje()); // Verificamos que el mensaje de la respuesta es correcto
        verify(depositoValidator).validar(depositoDto); // Verificamos que el validador fue llamado
        verify(depositoService).realizarDeposito(depositoDto); // Verificamos que el servicio fue llamado
    }

    // Mocks de objetos
    private DepositosDto depositosDtoMock(){
        DepositosDto depositosDto = new DepositosDto();
        depositosDto.setCuenta(416513215);
        depositosDto.setMoneda("P");
        depositosDto.setMonto(100.00);
        return depositosDto;
    }
    private RespuestaDto respuestaDtoMock(){
        RespuestaDto respuestaDto = new RespuestaDto();
        respuestaDto.setEstado("EXITOSO");
        respuestaDto.setMensaje("Deposito exitoso.");
        return respuestaDto;
    }
}