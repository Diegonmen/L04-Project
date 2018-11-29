package services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Report;
import repositories.ReportRepository;
import security.UserAccount;

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

	public void delete(Report report) {
		Assert.notNull(report);
		reportrepository.delete(report);
	}

	public List<Report> findAll() {
		return reportrepository.findAll();
	}
	
	public Collection<Report> findNotFinalModeReports() {
		Collection<Report> res = reportrepository.findNotFinalModeReports();
		Assert.notEmpty(res);
		return res;
	}
	
	public Collection<Report> findReportByCustomerUserAccount(UserAccount userAccount){
		Collection<Report> res = reportrepository.findReportByCustomerUserAccountId(userAccount.getId());
		Assert.notNull(res);
		return res;
	}
}
