package ar.edu.utn.frbb.tup.service;

public class InterpolService {
    public boolean pedidoCapturaInternacional(long dni) {
        if (dni == 26456437){
            return false;
        }
        return true;
    }  
}
