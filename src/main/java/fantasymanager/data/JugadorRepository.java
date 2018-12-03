package fantasymanager.data;

import org.springframework.data.repository.CrudRepository;

public interface JugadorRepository extends CrudRepository<Jugador, Integer> {

	Jugador findJugadorByIdNba(String idNba);

	Jugador findJugadorByNombre(String playerName);
}