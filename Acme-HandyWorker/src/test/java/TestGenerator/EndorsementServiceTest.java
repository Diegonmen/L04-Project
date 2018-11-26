package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Endorsement;
import services.EndorsementService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class EndorsementServiceTest extends AbstractTest { 

@Autowired 
private EndorsementService	endorsementService; 

@Test 
public void saveEndorsementTest(){ 
Endorsement endorsement, saved;
Collection<Endorsement> endorsements;
endorsement = endorsementService.findAll().iterator().next();
endorsement.setVersion(57);
saved = endorsementService.save(endorsement);
endorsements = endorsementService.findAll();
Assert.isTrue(endorsements.contains(saved));
} 

@Test 
public void findAllEndorsementTest() { 
Collection<Endorsement> result; 
result = endorsementService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneEndorsementTest(){ 
Endorsement endorsement = endorsementService.findAll().iterator().next(); 
int endorsementId = endorsement.getId(); 
Assert.isTrue(endorsementId != 0); 
Endorsement result; 
result = endorsementService.findOne(endorsementId); 
Assert.notNull(result); 
} 

@Test 
public void deleteEndorsementTest() { 
Endorsement endorsement = endorsementService.findAll().iterator().next(); 
Assert.notNull(endorsement); 
Assert.isTrue(endorsement.getId() != 0); 
Assert.isTrue(this.endorsementService.exists(endorsement.getId())); 
this.endorsementService.delete(endorsement); 
} 

} 
