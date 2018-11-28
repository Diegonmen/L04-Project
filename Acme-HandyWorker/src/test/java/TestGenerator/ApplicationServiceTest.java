
package TestGenerator;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Application;
import domain.CreditCard;
import domain.Customer;
import domain.FixUpTask;
import domain.HandyWorker;
import repositories.FixUpTaskRepository;
import security.LoginService;
import services.ApplicationService;
import services.CustomerService;
import services.HandyWorkerService;
import utilities.AbstractTest;

@ContextConfiguration(locations = { "classpath:spring/junit.xml", "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ApplicationServiceTest extends AbstractTest {

	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private FixUpTaskRepository fixUpTaskRepository;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private HandyWorkerService handyWorkerService;

	@Test
	public void saveApplicationCustomerTest() {
		Application created;
		Application saved;
		Application copyCreated;
		Customer customer;
		super.authenticate("customer1");

		customer = this.customerService.findCustomerByUserAccount(LoginService.getPrincipal());
		for (final Application a : this.applicationService.findApplicationsByCustomer(customer)) {
			if (a.getStatus().equals("PENDING")) {
				created = a;
				copyCreated = created;
				copyCreated.setStatus("ACCEPTED");
				final String comment = "Test Comment";
				final CreditCard creditCard = new CreditCard();
				creditCard.setBrandName("VISA");
				creditCard.setCVV(123);
				creditCard.setExpirationMonth(12);
				creditCard.setExpirationYear(2020);
				creditCard.setHolderName("Paco Asencio");
				creditCard.setNumber("1234567812345678");
				saved = this.applicationService.saveCustomer(copyCreated, comment, creditCard);
				Assert.isTrue(this.applicationService.findAll().contains(saved));
				Assert.isTrue(saved.getStatus().equals("ACCEPTED"));
			}
		}

	}
	
	@Test
	public void saveApplicationHandyWorkerTest() {
		Application created;
		Application saved;
		Application copyCreated;
		HandyWorker handyWorker;
		super.authenticate("handyWorker1");

		handyWorker = this.handyWorkerService.findHandyWorkerByUserAccount(LoginService.getPrincipal());
		for (final Application a : this.applicationService.findApplicationsByHandyWorker(handyWorker)) {
			if (a.getStatus().equals("PENDING")) {
				String comment = "Comment Test";
				created = a;
				copyCreated = created;
				copyCreated.setStatus("ACCEPTED");
				saved = this.applicationService.saveHandyWorker(copyCreated, comment);
				Assert.isTrue(this.applicationService.findAll().contains(saved));
				Assert.isTrue(saved.getStatus().equals("ACCEPTED"));
			}
		}

	}

	@Test
	public void findAllApplicationTest() {
		Collection<Application> result;
		result = this.applicationService.findAll();
		Assert.notNull(result);
	}

	@Test
	public void findOneApplicationTest() {
		final Application application = this.applicationService.findAll().iterator().next();
		final int applicationId = application.getId();
		Assert.isTrue(applicationId != 0);
		Application result;
		result = this.applicationService.findOne(applicationId);
		Assert.notNull(result);
	}

	@Test
	public void deleteApplicationTest() {
		final Application application = this.applicationService.findAll().iterator().next();
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		Assert.isTrue(this.applicationService.exists(application.getId()));
		this.applicationService.delete(application);
	}
	
	@Test
	public void findAcceptedHandyWorkerApplicationByFixUpTaskTest () {
		FixUpTask fixUpTask = this.fixUpTaskRepository.findAllFixUpTaskWithAcceptedApplications().iterator().next();
		Assert.notNull(fixUpTask);
		Application res = this.applicationService.findAcceptedHandyWorkerApplicationByFixUpTask(fixUpTask);
		Assert.isTrue(res.getStatus().equals("ACCEPTED"));
	}

}
