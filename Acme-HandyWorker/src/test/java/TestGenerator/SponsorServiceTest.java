
package TestGenerator;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.SponsorService;
import utilities.AbstractTest;
import domain.Sponsor;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SponsorServiceTest extends AbstractTest {

	@Autowired
	private SponsorService	sponsorService;


	@Test
	public void saveSponsorTest() {
		Sponsor created;
		Sponsor saved;
		Sponsor copyCreated;

		created = this.sponsorService.findAll().iterator().next();
		this.authenticate(created.getUserAccount().getUsername());
		copyCreated = this.copySponsor(created);
		copyCreated.setName("TestSponsor");
		saved = this.sponsorService.save(copyCreated);
		Assert.isTrue(this.sponsorService.findAll().contains(saved));
		Assert.isTrue(saved.getName().equals("TestSponsor"));
	}

	@Test
	public void findAllSponsorTest() {
		Collection<Sponsor> result;
		result = this.sponsorService.findAll();
		Assert.notNull(result);
	}

	@Test
	public void findOneSponsorTest() {
		final Sponsor sponsor = this.sponsorService.findAll().iterator().next();
		final int sponsorId = sponsor.getId();
		Assert.isTrue(sponsorId != 0);
		Sponsor result;
		result = this.sponsorService.findOne(sponsorId);
		Assert.notNull(result);
	}

	@Test
	public void deleteSponsorTest() {
		final Sponsor sponsor = this.sponsorService.findAll().iterator().next();
		Assert.notNull(sponsor);
		Assert.isTrue(sponsor.getId() != 0);
		Assert.isTrue(this.sponsorService.exists(sponsor.getId()));
		this.sponsorService.delete(sponsor);
	}

	@Test
	public void testCreate() {
		Sponsor sponsor;

		sponsor = this.sponsorService.create();
		Assert.isNull(sponsor.getAddress());
		Assert.isNull(sponsor.getEmail());
		Assert.isNull(sponsor.getName());
		Assert.isNull(sponsor.getSurname());
		Assert.isNull(sponsor.getPhoneNumber());
		Assert.isNull(sponsor.getPhoto());
		Assert.isNull(sponsor.getMiddleName());
		Assert.isNull(sponsor.getSurname());
	}

	private Sponsor copySponsor(final Sponsor sponsor) {
		Sponsor result;

		result = new Sponsor();
		result.setAddress(sponsor.getAddress());
		result.setEmail(sponsor.getEmail());
		result.setId(sponsor.getId());
		result.setName(sponsor.getName());
		result.setMiddleName(sponsor.getMiddleName());
		result.setPhoneNumber(sponsor.getPhoneNumber());
		result.setSurname(sponsor.getSurname());
		result.setBoxes(sponsor.getBoxes());
		result.setPhoto(sponsor.getPhoto());
		result.setSocialIdentity(sponsor.getSocialIdentity());
		result.setSuspicious(sponsor.isSuspicious());
		result.setUserAccount(sponsor.getUserAccount());
		result.setSponsorships(sponsor.getSponsorships());
		result.setVersion(sponsor.getVersion());

		return result;
	}

}
