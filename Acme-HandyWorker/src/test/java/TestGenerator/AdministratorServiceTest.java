
package TestGenerator;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Administrator;
import domain.Category;
import domain.Customer;
import domain.Referee;
import domain.Sponsor;
import domain.Warranty;
import security.UserAccount;
import services.AdministratorService;
import services.CategoryService;
import services.CustomerService;
import services.FixUpTaskService;
import services.RefereeService;
import services.SponsorService;
import services.WarrantyService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	@Autowired
	private AdministratorService	administratorService;
	
	@Autowired
	private CustomerService	customerService;

	@Autowired
	private WarrantyService warrantyService;

	@Autowired
	private FixUpTaskService fixUpTaskService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private RefereeService refereeService;
	
	@Autowired
	private SponsorService sponsorService;


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
	
	@Test
	public void testChangeEnabledActor() {
		Customer customer = customerService.findAll().iterator().next();
		
		boolean isEnabled = customer.getUserAccount().isEnabled();
		
		UserAccount account = this.administratorService.changeEnabledActor(customer.getUserAccount());

		Assert.isTrue(isEnabled != account.isEnabled());
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

	
	@Test
	public void saveWarrantyTest() {
		Warranty warranty, saved;
		Collection<Warranty> warrantys;
		this.authenticate(this.administratorService.findAll().iterator().next().getUserAccount().getUsername());
		warranty = fixUpTaskService.findAll().iterator().next().getWarranty();
		warranty.setTitle("Test Title");
		saved = warrantyService.save(warranty);
		warrantys = warrantyService.findAll();
		Assert.isTrue(warrantys.contains(saved));
	}

	@Test
	public void findAllWarrantyTest() {
		this.authenticate(this.administratorService.findAll().iterator().next().getUserAccount().getUsername());
		Collection<Warranty> result;
		result = warrantyService.findAll();
		Assert.notNull(result);
	}

	@Test
	public void findOneWarrantyTest() {
		this.authenticate(this.administratorService.findAll().iterator().next().getUserAccount().getUsername());
		Warranty warranty = warrantyService.findAll().iterator().next();
		int warrantyId = warranty.getId();
		Assert.isTrue(warrantyService.exists(warrantyId));
		Warranty result;
		result = warrantyService.findOne(warrantyId);
		Assert.notNull(result);
	}

	@Test
	public void deleteWarrantyTest() {
		this.authenticate(this.administratorService.findAll().iterator().next().getUserAccount().getUsername());
		Warranty warranty = warrantyService.findDraftModeWarranties().iterator().next();
		Assert.notNull(warranty);
		Assert.isTrue(this.warrantyService.exists(warranty.getId()));
		this.warrantyService.delete(warranty);
	}

	@Test
	public void saveCategoryTest() {
		Category category, saved;
		Collection<Category> categorys;
		this.authenticate(this.administratorService.findAll().iterator().next().getUserAccount().getUsername());
		category = categoryService.findAll().iterator().next();
		category.setName("Test Name");
		;
		saved = categoryService.save(category);
		categorys = categoryService.findAll();
		Assert.isTrue(categorys.contains(saved));
	}

	@Test
	public void findAllCategoryTest() {
		Collection<Category> result;
		this.authenticate(this.administratorService.findAll().iterator().next().getUserAccount().getUsername());
		result = categoryService.findAll();
		Assert.notEmpty(result);
	}

	@Test
	public void findOneCategoryTest() {
		this.authenticate(this.administratorService.findAll().iterator().next().getUserAccount().getUsername());
		Category category = categoryService.findAll().iterator().next();
		Assert.isTrue(categoryService.exists(category.getId()));
		Category result;
		result = categoryService.findOne(category.getId());
		Assert.notNull(result);
	}

	@Test
	public void deleteCategoryTest() {
		this.authenticate(this.administratorService.findAll().iterator().next().getUserAccount().getUsername());
		Category category = categoryService.findAll().iterator().next();
		Assert.notNull(category);
		Assert.isTrue(this.categoryService.exists(category.getId()));
		this.categoryService.delete(category);
	}
	
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



}
