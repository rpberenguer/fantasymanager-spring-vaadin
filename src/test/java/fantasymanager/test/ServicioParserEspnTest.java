package fantasymanager.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fantasymanager.exceptions.FantasyManagerException;
import fantasymanager.exceptions.FantasyManagerParserException;
import fantasymanager.services.ServicioParserEspnImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ServicioParserEspnTest {

	@Autowired
	ServicioParserEspnImpl servicioParserEspn;

	@Test
	public void testGetEstadisticas() {
		// try {
		// System.out.println("***** Inicioooo del TEST *****");
		//
		// // servicioParserEspn.getStatistics("20171017", "20171113");
		// // servicioParserEspn.getStatistics("20170216", "20170412");
		//
		// // servicioParserEspn.getStatistics("20161130");
		//
		// System.out.println("***** Fin del TEST *****");
		//
		// } catch (final FantasyManagerParserException e) {
		// e.printStackTrace();
		// }
	}

	@Test
	public void testGetEquipos() {
		try {
			System.out.println("***** Inicioooo del TEST *****");

			servicioParserEspn.getRosters();

			System.out.println("***** Fin del TEST *****");

		} catch (final FantasyManagerParserException e) {
			e.printStackTrace();
		} catch (final FantasyManagerException e) {
			e.printStackTrace();
		}
	}
}
