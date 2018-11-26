package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.ProfessionalRecord;
import services.ProfessionalRecordService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class ProfessionalRecordServiceTest extends AbstractTest { 

@Autowired 
private ProfessionalRecordService	professionalrecordService; 

@Test 
public void saveProfessionalRecordTest(){ 
ProfessionalRecord professionalrecord, saved;
Collection<ProfessionalRecord> professionalrecords;
professionalrecord = professionalrecordService.findAll().iterator().next();
professionalrecord.setVersion(57);
saved = professionalrecordService.save(professionalrecord);
professionalrecords = professionalrecordService.findAll();
Assert.isTrue(professionalrecords.contains(saved));
} 

@Test 
public void findAllProfessionalRecordTest() { 
Collection<ProfessionalRecord> result; 
result = professionalrecordService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneProfessionalRecordTest(){ 
ProfessionalRecord professionalrecord = professionalrecordService.findAll().iterator().next(); 
int professionalrecordId = professionalrecord.getId(); 
Assert.isTrue(professionalrecordId != 0); 
ProfessionalRecord result; 
result = professionalrecordService.findOne(professionalrecordId); 
Assert.notNull(result); 
} 

@Test 
public void deleteProfessionalRecordTest() { 
ProfessionalRecord professionalrecord = professionalrecordService.findAll().iterator().next(); 
Assert.notNull(professionalrecord); 
Assert.isTrue(professionalrecord.getId() != 0); 
Assert.isTrue(this.professionalrecordService.exists(professionalrecord.getId())); 
this.professionalrecordService.delete(professionalrecord); 
} 

} 
