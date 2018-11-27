
package TestGenerator;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.AdministratorService;
import utilities.AbstractTest;
import domain.Administrator;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	@Autowired
	private AdministratorService	administratorService;


	@Test
	public void saveAdministratorTest() {
		Administrator created;
		Administrator saved;
		Administrator copyCreated;

		created = this.administratorService.findAll().iterator().next();
		this.authenticate(created.getUserAccount().getUsername());
		copyCreated = this.copyAdministrator(created);
		copyCreated.setName("Testadministrator");
		saved = this.administratorService.save(copyCreated);
		Assert.isTrue(this.administratorService.findAll().contains(saved));
		Assert.isTrue(saved.getName().equals("Testadministrator"));
	}

	@Test
	public void findAllAdministratorTest() {
		Collection<Administrator> result;
		result = this.administratorService.findAll();
		Assert.notNull(result);
	}

	@Test
	public void findOneAdministratorTest() {
		final Administrator administrator = this.administratorService.findAll().iterator().next();
		final int administratorId = administrator.getId();
		Assert.isTrue(administratorId != 0);
		Administrator result;
		result = this.administratorService.findOne(administratorId);
		Assert.notNull(result);
	}

	@Test
	public void deleteAdministratorTest() {
		final Administrator administrator = this.administratorService.findAll().iterator().next();
		Assert.notNull(administrator);
		Assert.isTrue(administrator.getId() != 0);
		Assert.isTrue(this.administratorService.exists(administrator.getId()));
		this.administratorService.delete(administrator);
	}

	@Test
	public void testCreate() {
		Administrator administrator;

		administrator = this.administratorService.create();
		Assert.isNull(administrator.getAddress());
		Assert.isNull(administrator.getEmail());
		Assert.isNull(administrator.getName());
		Assert.isNull(administrator.getSurname());
		Assert.isNull(administrator.getPhoneNumber());
		Assert.isNull(administrator.getPhoto());
		Assert.isNull(administrator.getMiddleName());
		Assert.isNull(administrator.getSurname());
	}

	private Administrator copyAdministrator(final Administrator administrator) {
		Administrator result;

		result = new Administrator();
		result.setAddress(administrator.getAddress());
		result.setEmail(administrator.getEmail());
		result.setId(administrator.getId());
		result.setName(administrator.getName());
		result.setMiddleName(administrator.getMiddleName());
		result.setPhoneNumber(administrator.getPhoneNumber());
		result.setSurname(administrator.getSurname());
		result.setBoxes(administrator.getBoxes());
		result.setPhoto(administrator.getPhoto());
		result.setSocialIdentity(administrator.getSocialIdentity());
		result.setSuspicious(administrator.isSuspicious());
		result.setUserAccount(administrator.getUserAccount());
		result.setVersion(administrator.getVersion());

		return result;
	}

}
