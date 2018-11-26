package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.SocialIdentity;
import services.SocialIdentityService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class SocialIdentityServiceTest extends AbstractTest { 

@Autowired 
private SocialIdentityService	socialidentityService; 

@Test 
public void saveSocialIdentityTest(){ 
SocialIdentity socialidentity, saved;
Collection<SocialIdentity> socialidentitys;
socialidentity = socialidentityService.findAll().iterator().next();
socialidentity.setVersion(57);
saved = socialidentityService.save(socialidentity);
socialidentitys = socialidentityService.findAll();
Assert.isTrue(socialidentitys.contains(saved));
} 

@Test 
public void findAllSocialIdentityTest() { 
Collection<SocialIdentity> result; 
result = socialidentityService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneSocialIdentityTest(){ 
SocialIdentity socialidentity = socialidentityService.findAll().iterator().next(); 
int socialidentityId = socialidentity.getId(); 
Assert.isTrue(socialidentityId != 0); 
SocialIdentity result; 
result = socialidentityService.findOne(socialidentityId); 
Assert.notNull(result); 
} 

@Test 
public void deleteSocialIdentityTest() { 
SocialIdentity socialidentity = socialidentityService.findAll().iterator().next(); 
Assert.notNull(socialidentity); 
Assert.isTrue(socialidentity.getId() != 0); 
Assert.isTrue(this.socialidentityService.exists(socialidentity.getId())); 
this.socialidentityService.delete(socialidentity); 
} 

} 
