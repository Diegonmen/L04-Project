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
import services.WarrantyService;
import utilities.AbstractTest;

@ContextConfiguration(locations = { "classpath:spring/junit.xml", "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class WarrantyServiceTest extends AbstractTest {

	@Autowired
	private WarrantyService warrantyService;

	
	@Test
	public void findDraftModeWarrantiesTest() {
		Collection<Warranty> warranties = new LinkedList<>();
		warranties = warrantyService.findDraftModeWarranties();
		Assert.notEmpty(warranties);

	}

}
