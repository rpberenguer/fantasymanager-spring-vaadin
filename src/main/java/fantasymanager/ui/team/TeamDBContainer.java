package fantasymanager.ui.team;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.vaadin.data.provider.ListDataProvider;

import fantasymanager.data.Equipo;

@Component
public class TeamDBContainer extends ListDataProvider<Equipo> {

	private static final long serialVersionUID = 3090067422968423191L;

	public static final String BEAN_ID = "id";

	public static final String[] PROPERTIES = { "codigoCorto", "codigoLargo", "nombre" };
	public static final String[] HEADERS = { "codigoCorto", "codigoLargo", "nombre" };

	public TeamDBContainer() {
		this(new ArrayList<Equipo>());
	}

	public TeamDBContainer(List<Equipo> pList) {
		super(pList);
	}
}
