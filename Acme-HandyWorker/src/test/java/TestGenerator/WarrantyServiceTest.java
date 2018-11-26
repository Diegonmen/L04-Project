package TestGenerator; 

import java.util.Collection;

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
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class WarrantyServiceTest extends AbstractTest { 

@Autowired 
private WarrantyService	warrantyService; 

@Test 
public void saveWarrantyTest(){ 
Warranty warranty, saved;
Collection<Warranty> warrantys;
warranty = warrantyService.findAll().iterator().next();
warranty.setVersion(57);
saved = warrantyService.save(warranty);
warrantys = warrantyService.findAll();
Assert.isTrue(warrantys.contains(saved));
} 

@Test 
public void findAllWarrantyTest() { 
Collection<Warranty> result; 
result = warrantyService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneWarrantyTest(){ 
Warranty warranty = warrantyService.findAll().iterator().next(); 
int warrantyId = warranty.getId(); 
Assert.isTrue(warrantyId != 0); 
Warranty result; 
result = warrantyService.findOne(warrantyId); 
Assert.notNull(result); 
} 

@Test 
public void deleteWarrantyTest() { 
Warranty warranty = warrantyService.findAll().iterator().next(); 
Assert.notNull(warranty); 
Assert.isTrue(warranty.getId() != 0); 
Assert.isTrue(this.warrantyService.exists(warranty.getId())); 
this.warrantyService.delete(warranty); 
} 

} 
