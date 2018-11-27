
package TestGenerator;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import services.ApplicationService;
import services.CustomerService;
import utilities.AbstractTest;
import domain.Application;
import domain.CreditCard;
import domain.Customer;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ApplicationServiceTest extends AbstractTest {

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private CustomerService		customerService;


	@Test
	public void saveApplicationTest() {
		Application created;
		Application saved;
		Application copyCreated;
		Customer customer;
		super.authenticate("customer1");

		customer = this.customerService.findCustomerByUserAccount(LoginService.getPrincipal());
		for (final Application a : this.applicationService.findApplicationsByCustomer(customer))
			if (a.getStatus().equals("PENDING")) {
				created = a;
				copyCreated = created;
				copyCreated.setStatus("ACCEPTED");
				final String comment = "Comentario";
				copyCreated.getComments().add(comment);
				final CreditCard creditCard = new CreditCard();
				creditCard.setBrandName("VISA");
				creditCard.setCVV(123);
				creditCard.setExpirationMonth(12);
				creditCard.setExpirationYear(2020);
				creditCard.setHolderName("Paco Asencio");
				creditCard.setNumber("1234567812345678");
				copyCreated.setCreditCard(creditCard);
				saved = this.applicationService.save(copyCreated);
				Assert.isTrue(this.applicationService.findAll().contains(saved));
				Assert.isTrue(saved.getStatus().equals("ACCEPTED"));
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

}
