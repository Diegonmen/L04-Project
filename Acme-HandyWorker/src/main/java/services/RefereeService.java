
package services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Box;
import domain.Complaint;
import domain.Referee;
import domain.Report;
import domain.SocialIdentity;
import repositories.RefereeRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class RefereeService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private RefereeRepository	refereeRepository;
	
	@Autowired
	private ReportService reportService;

	public Referee save(Referee entity) {
		return refereeRepository.save(entity);
	}

	public List<Referee> findAll() {
		return refereeRepository.findAll();
	}

	public Referee findOne(Integer id) {
		return refereeRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return refereeRepository.exists(id);
	}

	public void delete(Referee entity) {
		refereeRepository.delete(entity);
	}
	
	public Referee create() {

		Referee result;
		UserAccount userAccount;
		Authority authority;

		result = new Referee();
		userAccount = new UserAccount();
		authority = new Authority();

		result.setSuspicious(false);

		authority.setAuthority("referee");
		userAccount.addAuthority(authority);
		userAccount.setEnabled(true);

		final Collection<Box> boxes = new LinkedList<>();
		result.setBoxes(boxes);
		final Collection<SocialIdentity> socialIdentities = new LinkedList<>();
		result.setSocialIdentity(socialIdentities);
		final Collection<Report> reports = new LinkedList<>();
		result.setReports(reports);
		result.setUserAccount(userAccount);

		return result;

	}

	public Report selfAssignComplaint(Report r, Complaint c) {
		Assert.notNull(r);
		Assert.notNull(c);
		
		r.getComplaints().add(c);
		
		return reportService.save(r);
	}
	
	public Referee findRefereeByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Assert.isTrue(userAccount.getId() != 0);
		final Referee res = this.refereeRepository.findByUserAccountId(userAccount.getId());
		return res;
	}
	
	public Report saveReport(final Report report) {
		Assert.notNull(report);
		Assert.isTrue(report.getId() != 0);
		UserAccount logedUserAccount = LoginService.getPrincipal();
		Referee referee = findRefereeByUserAccount(logedUserAccount);
		Authority authority = new Authority();
		authority.setAuthority("REFEREE");
		Report saved = this.reportService.findOne(report.getId());
		if(reportService.exists(report.getId()) && logedUserAccount.getAuthorities().contains(authority) && saved.isFinalMode() && findRefereeByUserAccount(logedUserAccount).equals(findRefereeByReport(report)) ) {
			Assert.notNull(saved, "report.not.null");
			Assert.isTrue(referee.getUserAccount().isAccountNonLocked() && !(referee.isSuspicious()),"referee.notEqual.accountOrSuspicious");
			
			Report result = this.reportService.save(report);
			Assert.notNull(result);
			return result;
		}else {
			Report result = this.reportService.findOne(report.getId());
			return result;
		}
	}	
		
		public Referee findRefereeByReport(Report report) {
			Assert.notNull(report);
			Assert.isTrue(report.getId()!=0);
			Referee res = refereeRepository.findRefereeByReportId(report.getId());
			Assert.notNull(res);
			return res;
		}
	

}
