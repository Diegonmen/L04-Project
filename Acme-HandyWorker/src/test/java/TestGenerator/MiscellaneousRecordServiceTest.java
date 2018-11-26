package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.MiscellaneousRecord;
import services.MiscellaneousRecordService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class MiscellaneousRecordServiceTest extends AbstractTest { 

@Autowired 
private MiscellaneousRecordService	miscellaneousrecordService; 

@Test 
public void saveMiscellaneousRecordTest(){ 
MiscellaneousRecord miscellaneousrecord, saved;
Collection<MiscellaneousRecord> miscellaneousrecords;
miscellaneousrecord = miscellaneousrecordService.findAll().iterator().next();
miscellaneousrecord.setVersion(57);
saved = miscellaneousrecordService.save(miscellaneousrecord);
miscellaneousrecords = miscellaneousrecordService.findAll();
Assert.isTrue(miscellaneousrecords.contains(saved));
} 

@Test 
public void findAllMiscellaneousRecordTest() { 
Collection<MiscellaneousRecord> result; 
result = miscellaneousrecordService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneMiscellaneousRecordTest(){ 
MiscellaneousRecord miscellaneousrecord = miscellaneousrecordService.findAll().iterator().next(); 
int miscellaneousrecordId = miscellaneousrecord.getId(); 
Assert.isTrue(miscellaneousrecordId != 0); 
MiscellaneousRecord result; 
result = miscellaneousrecordService.findOne(miscellaneousrecordId); 
Assert.notNull(result); 
} 

@Test 
public void deleteMiscellaneousRecordTest() { 
MiscellaneousRecord miscellaneousrecord = miscellaneousrecordService.findAll().iterator().next(); 
Assert.notNull(miscellaneousrecord); 
Assert.isTrue(miscellaneousrecord.getId() != 0); 
Assert.isTrue(this.miscellaneousrecordService.exists(miscellaneousrecord.getId())); 
this.miscellaneousrecordService.delete(miscellaneousrecord); 
} 

} 
