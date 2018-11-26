package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.PersonalRecord;
import services.PersonalRecordService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class PersonalRecordServiceTest extends AbstractTest { 

@Autowired 
private PersonalRecordService	personalrecordService; 

@Test 
public void savePersonalRecordTest(){ 
PersonalRecord personalrecord, saved;
Collection<PersonalRecord> personalrecords;
personalrecord = personalrecordService.findAll().iterator().next();
personalrecord.setVersion(57);
saved = personalrecordService.save(personalrecord);
personalrecords = personalrecordService.findAll();
Assert.isTrue(personalrecords.contains(saved));
} 

@Test 
public void findAllPersonalRecordTest() { 
Collection<PersonalRecord> result; 
result = personalrecordService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOnePersonalRecordTest(){ 
PersonalRecord personalrecord = personalrecordService.findAll().iterator().next(); 
int personalrecordId = personalrecord.getId(); 
Assert.isTrue(personalrecordId != 0); 
PersonalRecord result; 
result = personalrecordService.findOne(personalrecordId); 
Assert.notNull(result); 
} 

@Test 
public void deletePersonalRecordTest() { 
PersonalRecord personalrecord = personalrecordService.findAll().iterator().next(); 
Assert.notNull(personalrecord); 
Assert.isTrue(personalrecord.getId() != 0); 
Assert.isTrue(this.personalrecordService.exists(personalrecord.getId())); 
this.personalrecordService.delete(personalrecord); 
} 

} 
