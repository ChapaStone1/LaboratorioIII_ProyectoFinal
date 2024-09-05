package ar.edu.utn.frbb.tup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InterpolServiceTest {
    @InjectMocks
    private InterpolService interpolService;
    @Test
    public void testPedidoCapturaInternacional_True() {
        long dniConCaptura = 26456437;
        boolean resultado = interpolService.pedidoCapturaInternacional(dniConCaptura);
        assertTrue(resultado, "Se esperaba que el DNI " + dniConCaptura + " tenga pedido de captura internacional.");
    }

    @Test
    public void testPedidoCapturaInternacional_False() {
        long dniSinCaptura = 12345678;
        boolean resultado = interpolService.pedidoCapturaInternacional(dniSinCaptura);
        assertFalse(resultado, "Se esperaba que el DNI " + dniSinCaptura + " no tenga pedido de captura internacional.");
    }

    
}
