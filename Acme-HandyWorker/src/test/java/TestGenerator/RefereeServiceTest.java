
package TestGenerator;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Complaint;
import domain.Referee;
import domain.Report;
import security.LoginService;
import security.UserAccount;
import services.ComplaintService;
import services.RefereeService;
import services.ReportService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RefereeServiceTest extends AbstractTest {

	@Autowired
	private RefereeService	refereeService;

	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private ReportService reportService;
	
	
	@Test
	public void saveRefereeTest() {
		Referee created;
		Referee saved;
		Referee copyCreated;

		created = this.refereeService.findAll().iterator().next();
		this.authenticate(created.getUserAccount().getUsername());
		copyCreated = this.copyReferee(created);
		copyCreated.setName("Testadministrator");
		saved = this.refereeService.save(copyCreated);
		Assert.isTrue(this.refereeService.findAll().contains(saved));
		Assert.isTrue(saved.getName().equals("Testadministrator"));
	}
	
	private Referee copyReferee(final Referee referee) {
		Referee result;

		result = new Referee();
		result.setAddress(referee.getAddress());
		result.setEmail(referee.getEmail());
		result.setId(referee.getId());
		result.setName(referee.getName());
		result.setMiddleName(referee.getMiddleName());
		result.setPhoneNumber(referee.getPhoneNumber());
		result.setSurname(referee.getSurname());
		result.setBoxes(referee.getBoxes());
		result.setPhoto(referee.getPhoto());
		result.setSocialIdentity(referee.getSocialIdentity());
		result.setSuspicious(referee.isSuspicious());
		result.setUserAccount(referee.getUserAccount());
		result.setReports(referee.getReports());
		result.setVersion(referee.getVersion());

		return result;
	}
	
	
	@Test
	public void findAllRefereeTest() {
		Collection<Referee> result;
		result = this.refereeService.findAll();
		Assert.notNull(result);
	}

	@Test
	public void findOneRefereeTest() {
		final Referee referee = this.refereeService.findAll().iterator().next();
		final int refereeId = referee.getId();
		Assert.isTrue(refereeId != 0);
		Referee result;
		result = this.refereeService.findOne(refereeId);
		Assert.notNull(result);
	}

	@Test
	public void deleteRefereeTest() {
		final Referee referee = this.refereeService.findAll().iterator().next();
		Assert.notNull(referee);
		Assert.isTrue(referee.getId() != 0);
		Assert.isTrue(this.refereeService.exists(referee.getId()));
		this.refereeService.delete(referee);
	}

	@Test
	public void testCreate() {
		Referee referee;

		referee = this.refereeService.create();
		Assert.isNull(referee.getAddress());
		Assert.isNull(referee.getEmail());
		Assert.isNull(referee.getName());
		Assert.isNull(referee.getSurname());
		Assert.isNull(referee.getPhoneNumber());
		Assert.isNull(referee.getPhoto());
		Assert.isNull(referee.getMiddleName());
		Assert.isNull(referee.getSurname());
	}

	@Test
	public void findAllByRefereeNoAssignedTest() {
		this.authenticate(refereeService.findAll().iterator().next().getUserAccount().getUsername());
		
		final Collection<Complaint> res = this.complaintService.findComplaintsNoAsigned();
		Assert.notEmpty(res);
	}

	@Test
	public void selfAssignComplaintTest() {
		this.authenticate(refereeService.findAll().iterator().next().getUserAccount().getUsername());
		Report report = this.reportService.findAll().iterator().next();
		Complaint complaint = this.complaintService.findComplaintsNoAsigned().iterator().next();
		Assert.notNull(report);
		Assert.notNull(complaint);
		Report res = refereeService.selfAssignComplaint(report, complaint);
		Assert.notNull(res);
	}
	
	@Test
	public void findSelfAsignedComplaintsByRefereeTest() {
		this.authenticate("useracount0");
		UserAccount logedUserAccount = LoginService.getPrincipal();
		Referee referee = refereeService.findRefereeByUserAccount(logedUserAccount);
		Assert.isTrue(refereeService.exists(referee.getId()));
		Collection<Complaint> res = this.complaintService.findSelfAsignedComplaintsByReferee(referee);
		Assert.notEmpty(res);
		
	}
	
	@Test
	public void saveReportTest() {
		Report created, saved;
		this.authenticate(refereeService.findAll().iterator().next().getUserAccount().getUsername());
		created = reportService.findNotFinalModeReports().iterator().next();
		Assert.notNull(created);
		created.setDescription("Descripcion Test");
		saved = refereeService.saveReport(created);
		Assert.isTrue(saved.getDescription().equals("Descripcion Test"));
		
	}
}
