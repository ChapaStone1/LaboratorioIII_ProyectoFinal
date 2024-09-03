package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteDao extends AbstractBaseDao{

    @Autowired
    CuentaDao cuentaDao;

    public Cliente find(long dni, boolean loadComplete) {
        // Verifica si el cliente existe en la base de datos en memoria
        ClienteEntity entity = (ClienteEntity) getInMemoryDatabase().get(dni);
        if (entity == null) {
            return null; // Cliente no encontrado
        }

        // Convierte ClienteEntity a Cliente
        Cliente cliente = entity.toCliente();

        // Si se solicita carga completa, añade las cuentas asociadas al cliente
        if (loadComplete) {
            Set<Cuenta> cuentas = cuentaDao.getCuentasByCliente(dni);
            if (cuentas != null) {
                for (Cuenta cuenta : cuentas) {
                    cliente.addCuenta(cuenta);
                }
            }
        }

        return cliente;
    }


    public void save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        getInMemoryDatabase().put(entity.getId(), entity);
    }
    public boolean actualizarCliente(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        return getInMemoryDatabase().put(cliente.getDni(), entity) != null;
    }


    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }
}
