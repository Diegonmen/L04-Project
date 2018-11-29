package TestGenerator;

import java.util.Collection;
import java.util.LinkedList;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Warranty;
import repositories.AdministratorRepository;
import services.FixUpTaskService;
import services.WarrantyService;
import utilities.AbstractTest;

@ContextConfiguration(locations = { "classpath:spring/junit.xml", "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class WarrantyServiceTest extends AbstractTest {

	@Autowired
	private WarrantyService warrantyService;

	@Autowired
	private AdministratorRepository administratorRepository;

	@Autowired
	private FixUpTaskService fixUpTaskService;

	@Test
	public void saveWarrantyTest() {
		Warranty warranty, saved;
		Collection<Warranty> warrantys;
		this.authenticate(this.administratorRepository.findAll().iterator().next().getUserAccount().getUsername());
		warranty = fixUpTaskService.findAll().iterator().next().getWarranty();
		warranty.setTitle("Test Title");
		saved = warrantyService.save(warranty);
		warrantys = warrantyService.findAll();
		Assert.isTrue(warrantys.contains(saved));
	}

	@Test
	public void findAllWarrantyTest() {
		this.authenticate(this.administratorRepository.findAll().iterator().next().getUserAccount().getUsername());
		Collection<Warranty> result;
		result = warrantyService.findAll();
		Assert.notNull(result);
	}

	@Test
	public void findOneWarrantyTest() {
		this.authenticate(this.administratorRepository.findAll().iterator().next().getUserAccount().getUsername());
		Warranty warranty = warrantyService.findAll().iterator().next();
		int warrantyId = warranty.getId();
		Assert.isTrue(warrantyService.exists(warrantyId));
		Warranty result;
		result = warrantyService.findOne(warrantyId);
		Assert.notNull(result);
	}

	@Test
	public void deleteWarrantyTest() {
		this.authenticate(this.administratorRepository.findAll().iterator().next().getUserAccount().getUsername());
		Warranty warranty = warrantyService.findDraftModeWarranties().iterator().next();
		Assert.notNull(warranty);
		Assert.isTrue(this.warrantyService.exists(warranty.getId()));
		this.warrantyService.delete(warranty);
	}

	@Test
	public void findDraftModeWarrantiesTest() {
		Collection<Warranty> warranties = new LinkedList<>();
		warranties = warrantyService.findDraftModeWarranties();
		Assert.notEmpty(warranties);

	}

}
