package fantasymanager.data;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fantasymanager.data.dto.StatisticDto;

@Service
public class EstadisticaService {

	@Autowired
	private EstadisticaRepository repository;

	public List<StatisticDto> findAvgAndSumStatisticsByPlayer(int offset, int limit, Map<String, Boolean> sortOrders) {
		// final int page = offset / limit;
		// final List<Sort.Order> orders = sortOrders.entrySet().stream()
		// .map(e -> new Sort.Order(e.getValue() ? Sort.Direction.ASC :
		// Sort.Direction.DESC, e.getKey()))
		// .collect(Collectors.toList());
		//
		// final PageRequest pageRequest = new PageRequest(page, limit,
		// orders.isEmpty() ? null : new Sort(orders));
		// // final List<StatisticDto> items =
		// // repository.findAvgAndSumStatisticsByPlayer(pageRequest);
		// final List<StatisticDto> items =
		// repository.findAvgAndSumStatisticsByPlayer(pageRequest);
		// return items.subList(offset % limit, items.size());

		// return repository.findAvgAndSumStatisticsByPlayer();
		return null;
	}

	public Integer countAvgAndSumStatisticsByPlayer(int offset, int limit) {

		final int page = offset / limit;
		final PageRequest pageRequest = new PageRequest(page, limit);

		// final List<Object[]> list =
		// repository.countAvgAndSumStatisticsByPlayer(pageRequest);
		// return (Integer) list.get(0)[0];
		return Math.toIntExact(repository.count());
	}

}
