package ar.edu.utn.frbb.tup.service;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciasDto;
import org.springframework.stereotype.Service;

@Service
public class BanelcoService {
    public boolean servicioDeBanelco(TransferenciasDto transferenciaDto) {
        return transferenciaDto.getCuentaDestino() % 2 == 0;
    }
}
