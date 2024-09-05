package ar.edu.utn.frbb.tup.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VerazServiceTest {
    @InjectMocks
    private VerazService verazService;

    @Test
    public void testTieneDeudaConVeraz() {
        long dniPrimo = 10000019;
        boolean resultado = verazService.servicioDeVeraz(dniPrimo);
        assertTrue(resultado, "El cliente " + dniPrimo + " tenga deuda con veraz");
    }
    @Test
    public void testNoTieneDeudaConVeraz() {
        long dniNoPrimo = 37389808;
        boolean resultado = verazService.servicioDeVeraz(dniNoPrimo);
        assertFalse(resultado, "El cliente " + dniNoPrimo + " no tiene deuda con veraz.");
    }
}
