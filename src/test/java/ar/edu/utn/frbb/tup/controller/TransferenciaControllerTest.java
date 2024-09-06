package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.RespuestaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciasDto;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.service.TransferenciaService;
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
public class TransferenciaControllerTest {
    @Mock
    private TransferenciaService transferenciaService;

    @Mock
    private TransferenciaValidator transferenciaValidator;

    @InjectMocks
    private TransferenciaController transferenciaController;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTransferenciaSuccess(){
        TransferenciasDto transferenciaDto = transferenciasDtoMock(); // Mock de TransferenciasDto
        RespuestaDto respuestaDto = respuestaDtoMock(); // Mock de RespuestaDto

        // Simulamos que el servicio de transferencias retorna una respuesta exitosa
        when(transferenciaService.realizarTransferencia(transferenciaDto)).thenReturn(respuestaDto);

        RespuestaDto result = transferenciaController.transferir(transferenciaDto);


        assertNotNull(result); // Verificamos que la respuesta no sea nula
        assertEquals(respuestaDto.getEstado(), result.getEstado()); // Verificamos que el estado de la respuesta es correcto
        assertEquals(respuestaDto.getMensaje(), result.getMensaje()); // Verificamos que el mensaje de la respuesta es correcto
        verify(transferenciaValidator).validar(transferenciaDto); // Verificamos que el validador fue llamado
        verify(transferenciaService).realizarTransferencia(transferenciaDto); // Verificamos que el servicio fue llamado
    }

    // Mocks de objetos
    private TransferenciasDto transferenciasDtoMock(){
        TransferenciasDto transferenciasDto = new TransferenciasDto();
        transferenciasDto.setCuentaOrigen(416513215);
        transferenciasDto.setCuentaDestino(131265421);
        transferenciasDto.setMoneda("P");
        transferenciasDto.setMonto(100.00);
        return transferenciasDto;
    }
    private RespuestaDto respuestaDtoMock(){
        RespuestaDto respuestaDto = new RespuestaDto();
        respuestaDto.setEstado("EXITOSO");
        respuestaDto.setMensaje("Transferencia exitosa.");
        return respuestaDto;
    }
    
}
