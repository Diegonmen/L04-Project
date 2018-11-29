
package TestGenerator;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.RefereeService;
import utilities.AbstractTest;
import domain.Referee;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RefereeServiceTest extends AbstractTest {

	@Autowired
	private RefereeService	refereeService;

	@Test
	public void saveRefereeTest() {
		Referee created;
		Referee saved;
		Referee copyCreated;

		created = this.refereeService.findAll().iterator().next();
		this.authenticate(created.getUserAccount().getUsername());
		copyCreated = this.copyReferee(created);
		copyCreated.setName("Testreferee");
		saved = this.refereeService.save(copyCreated);
		Assert.isTrue(this.refereeService.findAll().contains(saved));
		Assert.isTrue(saved.getName().equals("Testreferee"));
	}

	@Test
	public void findAllRefereeTest() {
		Collection<Referee> result;
		result = this.refereeService.findAll();
		Assert.notNull(result);
	}

	@Test
	public void findOneRefereeTest() {
		final Referee referee = this.refereeService.findAll().iterator().next();
		final int refereeId = referee.getId();
		Assert.isTrue(refereeId != 0);
		Referee result;
		result = this.refereeService.findOne(refereeId);
		Assert.notNull(result);
	}

	@Test
	public void deleteRefereeTest() {
		final Referee referee = this.refereeService.findAll().iterator().next();
		Assert.notNull(referee);
		Assert.isTrue(referee.getId() != 0);
		Assert.isTrue(this.refereeService.exists(referee.getId()));
		this.refereeService.delete(referee);
	}

	@Test
	public void testCreate() {
		Referee referee;

		referee = this.refereeService.create();
		Assert.isNull(referee.getAddress());
		Assert.isNull(referee.getEmail());
		Assert.isNull(referee.getName());
		Assert.isNull(referee.getSurname());
		Assert.isNull(referee.getPhoneNumber());
		Assert.isNull(referee.getPhoto());
		Assert.isNull(referee.getMiddleName());
		Assert.isNull(referee.getSurname());
	}

	private Referee copyReferee(final Referee referee) {
		Referee result;

		result = new Referee();
		result.setAddress(referee.getAddress());
		result.setEmail(referee.getEmail());
		result.setId(referee.getId());
		result.setName(referee.getName());
		result.setMiddleName(referee.getMiddleName());
		result.setPhoneNumber(referee.getPhoneNumber());
		result.setSurname(referee.getSurname());
		result.setBoxes(referee.getBoxes());
		result.setPhoto(referee.getPhoto());
		result.setSocialIdentity(referee.getSocialIdentity());
		result.setSuspicious(referee.isSuspicious());
		result.setUserAccount(referee.getUserAccount());
		result.setReports(referee.getReports());
		result.setVersion(referee.getVersion());

		return result;
	}

}
