package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.EducationRecord;
import services.EducationRecordService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class EducationRecordServiceTest extends AbstractTest { 

@Autowired 
private EducationRecordService	educationrecordService; 

@Test 
public void saveEducationRecordTest(){ 
EducationRecord educationrecord, saved;
Collection<EducationRecord> educationrecords;
educationrecord = educationrecordService.findAll().iterator().next();
educationrecord.setVersion(57);
saved = educationrecordService.save(educationrecord);
educationrecords = educationrecordService.findAll();
Assert.isTrue(educationrecords.contains(saved));
} 

@Test 
public void findAllEducationRecordTest() { 
Collection<EducationRecord> result; 
result = educationrecordService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneEducationRecordTest(){ 
EducationRecord educationrecord = educationrecordService.findAll().iterator().next(); 
int educationrecordId = educationrecord.getId(); 
Assert.isTrue(educationrecordId != 0); 
EducationRecord result; 
result = educationrecordService.findOne(educationrecordId); 
Assert.notNull(result); 
} 

@Test 
public void deleteEducationRecordTest() { 
EducationRecord educationrecord = educationrecordService.findAll().iterator().next(); 
Assert.notNull(educationrecord); 
Assert.isTrue(educationrecord.getId() != 0); 
Assert.isTrue(this.educationrecordService.exists(educationrecord.getId())); 
this.educationrecordService.delete(educationrecord); 
} 

} 
