
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
import domain.FixUpTask;
import repositories.FixUpTaskRepository;
import services.ApplicationService;
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
