
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
import security.LoginService;
import services.ApplicationService;
import services.CustomerService;
import services.FixUpTaskService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CustomerServiceTest extends AbstractTest {

	@Autowired
	private CustomerService	customerService;
	
	@Autowired
	private FixUpTaskService fixuptaskService;
	
	@Autowired
	private ApplicationService applicationService;


	@Test
	public void saveCustomerTest() {
		Customer created;
		Customer saved;
		Customer copyCreated;

		created = this.customerService.findAll().iterator().next();
		this.authenticate(created.getUserAccount().getUsername());
		copyCreated = this.copyCustomer(created);
		copyCreated.setName("TestCustomer");
		saved = this.customerService.save(copyCreated);
		Assert.isTrue(this.customerService.findAll().contains(saved));
		Assert.isTrue(saved.getName().equals("TestCustomer"));

	}

	@Test
	public void findAllCustomerTest() {
		Collection<Customer> result;
		result = this.customerService.findAll();
		Assert.notNull(result);
	}

	@Test
	public void findOneCustomerTest() {
		final Customer customer = this.customerService.findAll().iterator().next();
		final int customerId = customer.getId();
		Assert.isTrue(customerId != 0);
		Customer result;
		result = this.customerService.findOne(customerId);
		Assert.notNull(result);
	}

	@Test
	public void deleteCustomerTest() {
		final Customer customer = this.customerService.findAll().iterator().next();
		Assert.notNull(customer);
		Assert.isTrue(customer.getId() != 0);
		Assert.isTrue(this.customerService.exists(customer.getId()));
		this.customerService.delete(customer);
	}

	@Test
	public void testCreate() {
		Customer customer;

		customer = this.customerService.create();
		Assert.isNull(customer.getAddress());
		Assert.isNull(customer.getEmail());
		Assert.isNull(customer.getName());
		Assert.isNull(customer.getSurname());
		Assert.isNull(customer.getPhoneNumber());
		Assert.isNull(customer.getPhoto());
		Assert.isNull(customer.getMiddleName());
		Assert.isNull(customer.getSurname());
	}
	
	@Test
	public void saveCustomerFixUpTaskTest() {
		final FixUpTask created;
		final FixUpTask saved;
		final FixUpTask copyCreated;
		created = this.fixuptaskService.findAll().iterator().next();
		this.authenticate(this.customerService.findCustomerByFixUpTask(created).getUserAccount().getUsername());
		copyCreated = this.copyFixUpTask(created);
		copyCreated.setDescription("Test");
		saved = this.customerService.saveCustomerFixUpTask(copyCreated);
		Assert.isTrue(this.fixuptaskService.findAll().contains(saved));
		Assert.isTrue(saved.getDescription().equals("Test"));
	}
	
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
				saved = this.customerService.saveCustomerApplication(copyCreated, comment, creditCard);
				Assert.isTrue(this.applicationService.findAll().contains(saved));
				Assert.isTrue(saved.getStatus().equals("ACCEPTED"));
			}
		}

	}

	private Customer copyCustomer(final Customer customer) {
		Customer result;

		result = new Customer();
		result.setAddress(customer.getAddress());
		result.setEmail(customer.getEmail());
		result.setId(customer.getId());
		result.setName(customer.getName());
		result.setMiddleName(customer.getMiddleName());
		result.setPhoneNumber(customer.getPhoneNumber());
		result.setSurname(customer.getSurname());
		result.setBoxes(customer.getBoxes());
		result.setComplaints(customer.getComplaints());
		result.setPhoto(customer.getPhoto());
		result.setSocialIdentity(customer.getSocialIdentity());
		result.setEndorsements(customer.getEndorsements());
		result.setSuspicious(customer.isSuspicious());
		result.setUserAccount(customer.getUserAccount());
		result.setVersion(customer.getVersion());

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
