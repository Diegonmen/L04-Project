package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Complaint;
import services.ComplaintService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class ComplaintServiceTest extends AbstractTest { 

@Autowired 
private ComplaintService	complaintService; 

@Test 
public void saveComplaintTest(){ 
Complaint complaint, saved;
Collection<Complaint> complaints;
complaint = complaintService.findAll().iterator().next();
complaint.setVersion(57);
saved = complaintService.save(complaint);
complaints = complaintService.findAll();
Assert.isTrue(complaints.contains(saved));
} 

@Test 
public void findAllComplaintTest() { 
Collection<Complaint> result; 
result = complaintService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneComplaintTest(){ 
Complaint complaint = complaintService.findAll().iterator().next(); 
int complaintId = complaint.getId(); 
Assert.isTrue(complaintId != 0); 
Complaint result; 
result = complaintService.findOne(complaintId); 
Assert.notNull(result); 
} 

@Test 
public void deleteComplaintTest() { 
Complaint complaint = complaintService.findAll().iterator().next(); 
Assert.notNull(complaint); 
Assert.isTrue(complaint.getId() != 0); 
Assert.isTrue(this.complaintService.exists(complaint.getId())); 
this.complaintService.delete(complaint); 
} 

} 
