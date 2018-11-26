package TestGenerator;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Customer;
import domain.FixUpTask;
import services.CustomerService;
import services.FixUpTaskService;
import utilities.AbstractTest;

@ContextConfiguration(locations = { "classpath:spring/junit.xml", "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FixUpTaskServiceTest extends AbstractTest {

	@Autowired
	private FixUpTaskService fixuptaskService;
	
	@Autowired
	private CustomerService customerService;

	@Test
	public void saveFixUpTaskTest() {
		FixUpTask fixuptask, saved;
		Collection<FixUpTask> fixuptasks;
		fixuptask = fixuptaskService.findAll().iterator().next();
		fixuptask.setVersion(57);
		saved = fixuptaskService.save(fixuptask);
		fixuptasks = fixuptaskService.findAll();
		Assert.isTrue(fixuptasks.contains(saved));
	}

	@Test
	public void findAllFixUpTaskTest() {
		Collection<FixUpTask> result;
		result = fixuptaskService.findAll();
		Assert.notNull(result);
	}

	@Test
	public void findOneFixUpTaskTest() {
		FixUpTask fixuptask = fixuptaskService.findAll().iterator().next();
		int fixuptaskId = fixuptask.getId();
		Assert.isTrue(fixuptaskId != 0);
		FixUpTask result;
		result = fixuptaskService.findOne(fixuptaskId);
		Assert.notNull(result);
	}

	@Test
	public void deleteFixUpTaskTest() {
		FixUpTask fixuptask = fixuptaskService.findAll().iterator().next();
		Assert.notNull(fixuptask);
		Assert.isTrue(fixuptask.getId() != 0);
		Assert.isTrue(this.fixuptaskService.exists(fixuptask.getId()));
		this.fixuptaskService.delete(fixuptask);
	}
	
	@Test
	public void findFixUpTasksByCustomerTest(){
		Collection<FixUpTask> fixUpTasks;
		Customer customer = customerService.findAll().iterator().next();
		Assert.notNull(customer);
		fixUpTasks = fixuptaskService.findByCustomer(customer);
		Assert.notNull(fixUpTasks);
	}

}
