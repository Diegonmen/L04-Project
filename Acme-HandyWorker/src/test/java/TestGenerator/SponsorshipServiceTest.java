package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Sponsorship;
import services.SponsorshipService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class SponsorshipServiceTest extends AbstractTest { 

@Autowired 
private SponsorshipService	sponsorshipService; 

@Test 
public void saveSponsorshipTest(){ 
Sponsorship sponsorship, saved;
Collection<Sponsorship> sponsorships;
sponsorship = sponsorshipService.findAll().iterator().next();
sponsorship.setVersion(57);
saved = sponsorshipService.save(sponsorship);
sponsorships = sponsorshipService.findAll();
Assert.isTrue(sponsorships.contains(saved));
} 

@Test 
public void findAllSponsorshipTest() { 
Collection<Sponsorship> result; 
result = sponsorshipService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneSponsorshipTest(){ 
Sponsorship sponsorship = sponsorshipService.findAll().iterator().next(); 
int sponsorshipId = sponsorship.getId(); 
Assert.isTrue(sponsorshipId != 0); 
Sponsorship result; 
result = sponsorshipService.findOne(sponsorshipId); 
Assert.notNull(result); 
} 

@Test 
public void deleteSponsorshipTest() { 
Sponsorship sponsorship = sponsorshipService.findAll().iterator().next(); 
Assert.notNull(sponsorship); 
Assert.isTrue(sponsorship.getId() != 0); 
Assert.isTrue(this.sponsorshipService.exists(sponsorship.getId())); 
this.sponsorshipService.delete(sponsorship); 
} 

} 
