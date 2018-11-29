

package TestGenerator;

import java.util.Collection;
import java.util.LinkedList;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Application;
import domain.Customer;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Phase;
import security.LoginService;
import services.ApplicationService;
import services.CustomerService;
import services.FixUpTaskService;
import services.HandyWorkerService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class HandyWorkerServiceTest extends AbstractTest {

	@Autowired
	private HandyWorkerService	handyworkerService;
	
	@Autowired
	private FixUpTaskService fixUpTaskService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ApplicationService applicationService;


	@Test
	public void saveHandyWorkerTest() {
		HandyWorker created;
		HandyWorker saved;
		HandyWorker copyCreated;

		created = this.handyworkerService.findAll().iterator().next();
		this.authenticate(created.getUserAccount().getUsername());
		copyCreated = this.copyHandyWorker(created);
		copyCreated.setName("TestHandyWorker");
		saved = this.handyworkerService.save(copyCreated);
		Assert.isTrue(this.handyworkerService.findAll().contains(saved));
		Assert.isTrue(saved.getName().equals("TestHandyWorker"));

	}

	@Test
	public void findAllHandyWorkerTest() {
		Collection<HandyWorker> result;
		result = this.handyworkerService.findAll();
		Assert.notNull(result);
	}

	@Test
	public void findOneHandyWorkerTest() {
		final HandyWorker handyworker = this.handyworkerService.findAll().iterator().next();
		final int handyworkerId = handyworker.getId();
		Assert.isTrue(handyworkerId != 0);
		HandyWorker result;
		result = this.handyworkerService.findOne(handyworkerId);
		Assert.notNull(result);
	}

	@Test
	public void deleteHandyWorkerTest() {
		final HandyWorker handyworker = this.handyworkerService.findAll().iterator().next();
		Assert.notNull(handyworker);
		Assert.isTrue(handyworker.getId() != 0);
		Assert.isTrue(this.handyworkerService.exists(handyworker.getId()));
		this.handyworkerService.delete(handyworker);
	}

	@Test
	public void testCreate() {
		HandyWorker handyWorker;

		handyWorker = this.handyworkerService.create();
		Assert.isNull(handyWorker.getAddress());
		Assert.isNull(handyWorker.getEmail());
		Assert.isNull(handyWorker.getName());
		Assert.isNull(handyWorker.getSurname());
		Assert.isNull(handyWorker.getPhoneNumber());
		Assert.isNull(handyWorker.getPhoto());
		Assert.isNull(handyWorker.getMake());
		Assert.isNull(handyWorker.getMiddleName());
	}
	
	@Test
	public void testFindCustomerProfile() {
		final FixUpTask fixUpTask;
		fixUpTask = this.fixUpTaskService.findAll().iterator().next();
		Assert.notNull(fixUpTask);
		Customer customer = this.handyworkerService.findCustomerProfile(fixUpTask);
		Assert.notNull(customer);
		Assert.isTrue(customer.getId() != 0);
	}
	
	@Test
	public void allCustomerFixUpTaskTest() {
		final Collection<FixUpTask> fixUpTasks;
		Customer customer = customerService.findAll().iterator().next();
		Assert.notNull(customer);
		fixUpTasks = handyworkerService.allCustomerFixUpTask(customer);
		Assert.notNull(fixUpTasks);
	}
	
	@Test
	public void saveHandyWorkerFixUpTaskTest() {
		final FixUpTask created;
		final FixUpTask saved;
		final FixUpTask copyCreated;
		created = this.fixUpTaskService.findAllFixUpTaskWithAcceptedApplications().iterator().next();
		Assert.notNull(created);
		this.authenticate(this.handyworkerService.findByFixUpTask(created).getUserAccount().getUsername());
		copyCreated = this.copyFixUpTask(created);;
		Collection<Phase> phases = new LinkedList<>();
		Phase phase = new Phase();
		phase.setDescription("Description");
		phase.setTitle("Title");
		phases.add(phase);
		saved = this.handyworkerService.saveHandyWorkerFixUpTask(copyCreated, phases);
		Assert.isTrue(this.fixUpTaskService.findAll().contains(saved));
	}
	
	@Test
	public void saveApplicationHandyWorkerTest() {
		Application created;
		Application saved;
		Application copyCreated;
		HandyWorker handyWorker;
		super.authenticate("handyWorker1");

		handyWorker = this.handyworkerService.findHandyWorkerByUserAccount(LoginService.getPrincipal());
		for (final Application a : this.applicationService.findApplicationsByHandyWorker(handyWorker)) {
			if (a.getStatus().equals("PENDING")) {
				String comment = "Comment Test";
				created = a;
				copyCreated = created;
				copyCreated.setStatus("ACCEPTED");
				saved = this.handyworkerService.saveHandyWorkerApplication(copyCreated, comment);
				Assert.isTrue(this.applicationService.findAll().contains(saved));
				Assert.isTrue(saved.getStatus().equals("ACCEPTED"));
			}
		}

	}
	

	private HandyWorker copyHandyWorker(final HandyWorker handyWorker) {
		HandyWorker result;

		result = new HandyWorker();
		result.setAddress(handyWorker.getAddress());
		result.setEmail(handyWorker.getEmail());
		result.setId(handyWorker.getId());
		result.setName(handyWorker.getName());
		result.setMiddleName(handyWorker.getMiddleName());
		result.setPhoneNumber(handyWorker.getPhoneNumber());
		result.setSurname(handyWorker.getSurname());
		result.setMake(handyWorker.getMake());
		result.setFinder(handyWorker.getFinder());
		result.setApplications(handyWorker.getApplications());
		result.setBoxes(handyWorker.getBoxes());
		result.setCurriculum(handyWorker.getCurriculum());
		result.setPhoto(handyWorker.getPhoto());
		result.setSocialIdentity(handyWorker.getSocialIdentity());
		result.setTutorials(handyWorker.getTutorials());
		result.setEndorsements(handyWorker.getEndorsements());
		result.setSuspicious(handyWorker.isSuspicious());
		result.setUserAccount(handyWorker.getUserAccount());
		result.setVersion(handyWorker.getVersion());

		return result;
		
	}
	
	private FixUpTask copyFixUpTask(final FixUpTask fixUpTask) {
		FixUpTask result;

		result = new FixUpTask();
		result.setAddress(fixUpTask.getAddress());
		result.setApplications(fixUpTask.getApplications());
		result.setCategory(fixUpTask.getCategory());
		result.setComplaints(fixUpTask.getComplaints());
		result.setDescription(fixUpTask.getDescription());
		result.setEndDate(fixUpTask.getEndDate());
		result.setMaxPrice(fixUpTask.getMaxPrice());
		result.setId(fixUpTask.getId());
		result.setPhases(fixUpTask.getPhases());
		result.setPublicationMoment(fixUpTask.getPublicationMoment());
		result.setStartDate(fixUpTask.getStartDate());
		result.setTicker(fixUpTask.getTicker());
		result.setWarranty(fixUpTask.getWarranty());
		result.setVersion(fixUpTask.getVersion());
		return result;
	}
}
