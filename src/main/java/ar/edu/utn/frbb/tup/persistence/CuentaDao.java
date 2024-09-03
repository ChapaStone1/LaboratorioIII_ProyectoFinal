package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CuentaDao  extends AbstractBaseDao{
    @Override
    protected String getEntityName() {
        return "CUENTA";
    }

    public void save(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    public Cuenta find(long id) {
        if (getInMemoryDatabase().get(id) == null) {
            return null;
        }
        return ((CuentaEntity) getInMemoryDatabase().get(id)).toCuenta();
    }

    public Set<Cuenta> getCuentasByCliente(long dni) {
    Set<Cuenta> cuentasDelCliente = new HashSet<>();
    for (Object object : getInMemoryDatabase().values()) {
        CuentaEntity cuenta = (CuentaEntity) object;
        Cliente titular = cuenta.getTitular();
        
        // Verifica si titular es null antes de acceder a getDni()
        if (titular != null && titular.getDni() == dni) {
            cuentasDelCliente.add(cuenta.toCuenta());
        }
    }
    return cuentasDelCliente;
}
    
    public Cuenta findByNumeroCuenta(long numeroCuenta) {
        for (Object object : getInMemoryDatabase().values()) {
            CuentaEntity cuentaEntity = (CuentaEntity) object;
            if (cuentaEntity.getNumeroCuenta() == numeroCuenta) {
                return cuentaEntity.toCuenta();
            }
        }
        return null;
    }
    public void actualizarCuenta(Cuenta cuenta){
        CuentaEntity cuentaEntity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(cuenta.getNumeroCuenta(), cuentaEntity);
    }
}
