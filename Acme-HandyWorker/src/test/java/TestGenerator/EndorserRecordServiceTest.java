package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.EndorserRecord;
import services.EndorserRecordService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class EndorserRecordServiceTest extends AbstractTest { 

@Autowired 
private EndorserRecordService	endorserrecordService; 

@Test 
public void saveEndorserRecordTest(){ 
EndorserRecord endorserrecord, saved;
Collection<EndorserRecord> endorserrecords;
endorserrecord = endorserrecordService.findAll().iterator().next();
endorserrecord.setVersion(57);
saved = endorserrecordService.save(endorserrecord);
endorserrecords = endorserrecordService.findAll();
Assert.isTrue(endorserrecords.contains(saved));
} 

@Test 
public void findAllEndorserRecordTest() { 
Collection<EndorserRecord> result; 
result = endorserrecordService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneEndorserRecordTest(){ 
EndorserRecord endorserrecord = endorserrecordService.findAll().iterator().next(); 
int endorserrecordId = endorserrecord.getId(); 
Assert.isTrue(endorserrecordId != 0); 
EndorserRecord result; 
result = endorserrecordService.findOne(endorserrecordId); 
Assert.notNull(result); 
} 

@Test 
public void deleteEndorserRecordTest() { 
EndorserRecord endorserrecord = endorserrecordService.findAll().iterator().next(); 
Assert.notNull(endorserrecord); 
Assert.isTrue(endorserrecord.getId() != 0); 
Assert.isTrue(this.endorserrecordService.exists(endorserrecord.getId())); 
this.endorserrecordService.delete(endorserrecord); 
} 

} 
