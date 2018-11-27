
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
import services.FixUpTaskService;
import utilities.AbstractTest;
import domain.Customer;
import domain.FixUpTask;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FixUpTaskServiceTest extends AbstractTest {

	@Autowired
	private FixUpTaskService	fixuptaskService;

	@Autowired
	private CustomerService		customerService;


	@Test
	public void saveFixUpTaskTest() {
		final FixUpTask created;
		final FixUpTask saved;
		final FixUpTask copyCreated;
		created = this.fixuptaskService.findAll().iterator().next();
		this.authenticate(this.customerService.findCustomerByFixUpTask(created).getUserAccount().getUsername());
		copyCreated = this.copyFixUpTask(created);
		copyCreated.setDescription("Test");
		saved = this.fixuptaskService.save(copyCreated);
		Assert.isTrue(this.fixuptaskService.findAllCustomer().contains(saved));
		Assert.isTrue(saved.getDescription().equals("Test"));
	}

	@Test
	public void findAllFixUpTaskTest() {
		this.authenticate("handyWorker1");
		Collection<FixUpTask> result;
		result = this.fixuptaskService.findAll();
		Assert.notNull(result);
	}

	@Test
	public void findOneFixUpTaskTest() {
		FixUpTask result;
		final FixUpTask fixuptask = this.fixuptaskService.findAllCustomer().iterator().next();
		this.authenticate(this.customerService.findCustomerByFixUpTask(fixuptask).getUserAccount().getUsername());
		Assert.isTrue(fixuptask.getId() != 0);
		Assert.isTrue(this.fixuptaskService.exists(fixuptask.getId()));
		result = this.fixuptaskService.findOne(fixuptask.getId());
		Assert.notNull(result);
	}

	@Test
	public void deleteFixUpTaskTest() {
		final FixUpTask fixuptask = this.fixuptaskService.findAllCustomer().iterator().next();
		this.authenticate(this.customerService.findCustomerByFixUpTask(fixuptask).getUserAccount().getUsername());
		Assert.notNull(fixuptask);
		Assert.isTrue(fixuptask.getId() != 0);
		Assert.isTrue(this.fixuptaskService.exists(fixuptask.getId()));
		this.fixuptaskService.delete(fixuptask);
	}

	@Test
	public void findFixUpTasksByCustomerTest() {
		Collection<FixUpTask> fixUpTasks;
		final Customer customer = this.customerService.findAll().iterator().next();
		Assert.notNull(customer);
		fixUpTasks = this.fixuptaskService.findByCustomer(customer);
		Assert.notNull(fixUpTasks);
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
