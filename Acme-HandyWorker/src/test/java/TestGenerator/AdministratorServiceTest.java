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
import services.AdministratorService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class AdministratorServiceTest extends AbstractTest { 

@Autowired 
private AdministratorService	administratorService; 

@Test 
public void saveAdministratorTest(){ 
Administrator administrator, saved;
Collection<Administrator> administrators;
administrator = administratorService.findAll().iterator().next();
administrator.setVersion(57);
saved = administratorService.save(administrator);
administrators = administratorService.findAll();
Assert.isTrue(administrators.contains(saved));
} 

@Test 
public void findAllAdministratorTest() { 
Collection<Administrator> result; 
result = administratorService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneAdministratorTest(){ 
Administrator administrator = administratorService.findAll().iterator().next(); 
int administratorId = administrator.getId(); 
Assert.isTrue(administratorId != 0); 
Administrator result; 
result = administratorService.findOne(administratorId); 
Assert.notNull(result); 
} 

@Test 
public void deleteAdministratorTest() { 
Administrator administrator = administratorService.findAll().iterator().next(); 
Assert.notNull(administrator); 
Assert.isTrue(administrator.getId() != 0); 
Assert.isTrue(this.administratorService.exists(administrator.getId())); 
this.administratorService.delete(administrator); 
} 

} 
