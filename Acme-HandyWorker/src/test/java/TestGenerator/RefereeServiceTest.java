package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Referee;
import services.RefereeService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class RefereeServiceTest extends AbstractTest { 

@Autowired 
private RefereeService	refereeService; 

@Test 
public void saveRefereeTest(){ 
Referee referee, saved;
Collection<Referee> referees;
referee = refereeService.findAll().iterator().next();
referee.setVersion(57);
saved = refereeService.save(referee);
referees = refereeService.findAll();
Assert.isTrue(referees.contains(saved));
} 

@Test 
public void findAllRefereeTest() { 
Collection<Referee> result; 
result = refereeService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneRefereeTest(){ 
Referee referee = refereeService.findAll().iterator().next(); 
int refereeId = referee.getId(); 
Assert.isTrue(refereeId != 0); 
Referee result; 
result = refereeService.findOne(refereeId); 
Assert.notNull(result); 
} 

@Test 
public void deleteRefereeTest() { 
Referee referee = refereeService.findAll().iterator().next(); 
Assert.notNull(referee); 
Assert.isTrue(referee.getId() != 0); 
Assert.isTrue(this.refereeService.exists(referee.getId())); 
this.refereeService.delete(referee); 
} 

} 
