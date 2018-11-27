package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Sponsor;
import services.SponsorService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class SponsorServiceTest extends AbstractTest { 

@Autowired 
private SponsorService	sponsorService; 

@Test 
public void saveSponsorTest(){ 
Sponsor sponsor, saved;
Collection<Sponsor> sponsors;
sponsor = sponsorService.findAll().iterator().next();
sponsor.setVersion(57);
saved = sponsorService.save(sponsor);
sponsors = sponsorService.findAll();
Assert.isTrue(sponsors.contains(saved));
} 

@Test 
public void findAllSponsorTest() { 
Collection<Sponsor> result; 
result = sponsorService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneSponsorTest(){ 
Sponsor sponsor = sponsorService.findAll().iterator().next(); 
int sponsorId = sponsor.getId(); 
Assert.isTrue(sponsorId != 0); 
Sponsor result; 
result = sponsorService.findOne(sponsorId); 
Assert.notNull(result); 
} 

@Test 
public void deleteSponsorTest() { 
Sponsor sponsor = sponsorService.findAll().iterator().next(); 
Assert.notNull(sponsor); 
Assert.isTrue(sponsor.getId() != 0); 
Assert.isTrue(this.sponsorService.exists(sponsor.getId())); 
this.sponsorService.delete(sponsor); 
} 

} 
