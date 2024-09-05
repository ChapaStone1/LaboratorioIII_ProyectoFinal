package ar.edu.utn.frbb.tup.service;
import org.springframework.stereotype.Service;
@Service
public class InterpolService {
    public boolean pedidoCapturaInternacional(long dni) {
        if (dni == 26456437){
            return true;
        }
        return false;
    }  
}
