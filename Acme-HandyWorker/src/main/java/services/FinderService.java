
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.FinderRepository;
import domain.Finder;
import domain.FixUpTask;

@Service
@Transactional
public class FinderService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private FinderRepository	finderRepository;

	public Finder create() {
		Finder res = new Finder();
		String keyWord = "";
		Double minPrice = 0.0;
		Double maxPrice = 0.0;
		Collection<FixUpTask> fixUpTasks = new ArrayList<>();
		res.setKeyWord(keyWord);
		res.setMinPrice(minPrice);
		res.setMaxPrice(maxPrice);
		res.setStartDate(new Date());
		res.setEndDate(new Date());
		res.setLastSearchDate(new Date());
		res.setFixUpTasks(fixUpTasks);
		return res;
	}
	// Simple CRUD methods ----------------------------------------------------

}
