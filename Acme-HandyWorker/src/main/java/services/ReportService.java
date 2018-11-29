package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Report;
import repositories.ReportRepository;

@Service
@Transactional
public class ReportService {

	@Autowired
	private ReportRepository reportrepository;

	public Report save(Report entity) {
		Assert.notNull(entity);
		return reportrepository.save(entity);
	}

	public Report findOne(Integer id) {
		Assert.notNull(id);
		return reportrepository.findOne(id);
	}

	public boolean exists(Integer id) {
		Assert.notNull(id);
		return reportrepository.exists(id);
	}

	public void delete(Integer id) {
		Assert.notNull(id);
		reportrepository.delete(id);
	}

	public List<Report> findAll() {
		return reportrepository.findAll();
	}
	
}
