package fantasymanager.ui.statistic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.vaadin.data.provider.ListDataProvider;

import fantasymanager.data.dto.StatisticDto;

@Component
public class StatisticDBContainer extends ListDataProvider<StatisticDto> {

	private static final long serialVersionUID = 3090067422968423191L;

	// public static final String BEAN_ID = "id";

	public static final String[] PROPERTIES = { "nombre", "avg", "tot" };
	public static final String[] HEADERS = { "nombre", "avg", "tot" };

	public StatisticDBContainer() {
		this(new ArrayList<StatisticDto>());
	}

	public StatisticDBContainer(List<StatisticDto> pList) {
		super(pList);
	}
}
