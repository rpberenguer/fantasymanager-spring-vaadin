package fantasymanager.data;

import org.springframework.data.repository.CrudRepository;

public interface EquipoRepository extends CrudRepository<Equipo, Integer> {

	Equipo findByCodigoCorto(String codigoCorto);
}