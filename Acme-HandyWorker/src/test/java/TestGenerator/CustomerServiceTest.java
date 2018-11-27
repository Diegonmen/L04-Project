
package TestGenerator;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.CustomerService;
import utilities.AbstractTest;
import domain.Customer;
import domain.HandyWorker;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CustomerServiceTest extends AbstractTest {

	@Autowired
	private CustomerService	customerService;


	@Test
	public void saveCustomerTest() {
		Customer created;
		Customer saved;
		Customer copyCreated;

		created = this.customerService.findAll().iterator().next();
		System.out.println(created);
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
		Assert.isNull(customer.getBoxes());
		Assert.isNull(customer.getComplaints());
		Assert.isNull(customer.getEndorsements());
		Assert.isNull(customer.getMiddleName());
		Assert.isNull(customer.getSurname());
		Assert.isNull(customer.getSocialIdentity());
		Assert.isNull(customer.getFixUpTasks());
		Assert.isNull(customer.getUserAccount());
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
}
