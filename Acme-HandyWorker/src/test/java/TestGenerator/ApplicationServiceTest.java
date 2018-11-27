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
import services.ApplicationService;
import utilities.AbstractTest;

@ContextConfiguration(locations = { "classpath:spring/junit.xml", "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ApplicationServiceTest extends AbstractTest {

	@Autowired
	private ApplicationService applicationService;

	@Test
	public void saveApplicationTest() {
		Application application, saved;
		Collection<Application> applications;
		application = applicationService.findAll().iterator().next();
		application.setVersion(57);
		saved = applicationService.save(application);
		applications = applicationService.findAll();
		Assert.isTrue(applications.contains(saved));
	}

	@Test
	public void findAllApplicationTest() {
		Collection<Application> result;
		result = applicationService.findAll();
		Assert.notNull(result);
	}

	@Test
	public void findOneApplicationTest() {
		Application application = applicationService.findAll().iterator().next();
		int applicationId = application.getId();
		Assert.isTrue(applicationId != 0);
		Application result;
		result = applicationService.findOne(applicationId);
		Assert.notNull(result);
	}

	@Test
	public void deleteApplicationTest() {
		Application application = applicationService.findAll().iterator().next();
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		Assert.isTrue(this.applicationService.exists(application.getId()));
		this.applicationService.delete(application);
	}

}
