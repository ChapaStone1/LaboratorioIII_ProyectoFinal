package ar.edu.utn.frbb.tup.service;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciasDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BanelcoServiceTest {
    @InjectMocks
    private BanelcoService banelcoService;

    @Mock
    private TransferenciasDto transferenciaDto;

    @Test
    public void testServicioDeBanelcoCuentaDestinoPar() {
        // True si es numero par el numero de cuenta destino
        when(transferenciaDto.getCuentaDestino()).thenReturn(2203200322L);

        boolean result = banelcoService.servicioDeBanelco(transferenciaDto);

        // Verificar el resultado esperado
        assertTrue(result, "El resultado debe ser verdadero para una cuenta destino par");
    }

    @Test
    public void testServicioDeBanelcoCuentaDestinoImpar() {
        // False si es numero impar el numero de cuenta destino
        when(transferenciaDto.getCuentaDestino()).thenReturn(22032003223L);

        boolean result = banelcoService.servicioDeBanelco(transferenciaDto);

        // Verificar el resultado esperado
        assertFalse(result, "El resultado debe ser falso para una cuenta destino impar");
    }
    
}