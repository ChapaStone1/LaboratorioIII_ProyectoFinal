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
        // Configurar el mock para devolver un valor par
        when(transferenciaDto.getCuentaDestino()).thenReturn(2203200322L);

        // Ejecutar el método a probar
        boolean result = banelcoService.servicioDeBanelco(transferenciaDto);

        // Verificar el resultado esperado
        assertTrue(result, "El resultado debe ser verdadero para una cuenta destino par");
    }

    @Test
    public void testServicioDeBanelcoCuentaDestinoImpar() {
        // Configurar el mock para devolver un valor impar
        when(transferenciaDto.getCuentaDestino()).thenReturn(22032003223L);

        // Ejecutar el método a probar
        boolean result = banelcoService.servicioDeBanelco(transferenciaDto);

        // Verificar el resultado esperado
        assertFalse(result, "El resultado debe ser falso para una cuenta destino impar");
    }
    
}