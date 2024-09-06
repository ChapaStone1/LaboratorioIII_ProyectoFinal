package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.controller.dto.RetirosDto;
import ar.edu.utn.frbb.tup.controller.validator.RetirosValidator;
import ar.edu.utn.frbb.tup.service.RetirosService;
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
public class RetirosControllerTest {
    @Mock
    private RetirosService retirosService;

    @Mock
    private RetirosValidator retirosValidator;

    @InjectMocks
    private RetirosController retirosController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testDepositoSuccess(){
        RetirosDto retirosDto = retirosDtoMock();
        RespuestaDto respuestaDto = respuestaDtoMock(); // Mock de RespuestaDto

        when(retirosService.realizarRetiro(retirosDto)).thenReturn(respuestaDto);
        RespuestaDto result = retirosController.realizarRetiro(retirosDto);

        assertNotNull(result); // Verificamos que la respuesta no sea nula
        assertEquals(respuestaDto.getEstado(), result.getEstado()); // Verificamos que el estado de la respuesta es correcto
        assertEquals(respuestaDto.getMensaje(), result.getMensaje()); // Verificamos que el mensaje de la respuesta es correcto
        verify(retirosValidator).validar(retirosDto); // Verificamos que el validador fue llamado
        verify(retirosService).realizarRetiro(retirosDto); // Verificamos que el servicio fue llamado
    }

    // Mocks de objetos
    private RetirosDto retirosDtoMock(){
        RetirosDto retirosDto = new RetirosDto();
        retirosDto.setCuenta(416513215);
        retirosDto.setMoneda("P");
        retirosDto.setMonto(100.00);
        return retirosDto;
    }
    private RespuestaDto respuestaDtoMock(){
        RespuestaDto respuestaDto = new RespuestaDto();
        respuestaDto.setEstado("EXITOSO");
        respuestaDto.setMensaje("Retiro exitoso.");
        return respuestaDto;
    }
    
}
