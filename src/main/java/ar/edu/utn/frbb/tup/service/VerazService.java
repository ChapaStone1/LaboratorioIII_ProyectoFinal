package ar.edu.utn.frbb.tup.service;
import org.springframework.stereotype.Service;

@Service
public class VerazService {

    // Método principal que verifica el DNI
    public boolean servicioDeVeraz(long dni) {
        if (esPrimo(dni)) {
            return true;
        }
        return false;
    }

    private boolean esPrimo(long numero) {
        if (numero <= 1) {
            return false;
        }
        if (numero == 2 || numero == 3) {
            return true;
        }
        if (numero % 2 == 0 || numero % 3 == 0) {
            return false;
        }
        for (long i = 5; i * i <= numero; i += 6) {
            if (numero % i == 0 || numero % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
    
}
