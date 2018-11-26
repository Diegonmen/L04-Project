package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.WelcomeMessage;
import services.WelcomeMessageService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class WelcomeMessageServiceTest extends AbstractTest { 

@Autowired 
private WelcomeMessageService	welcomemessageService; 

@Test 
public void saveWelcomeMessageTest(){ 
WelcomeMessage welcomemessage, saved;
Collection<WelcomeMessage> welcomemessages;
welcomemessage = welcomemessageService.findAll().iterator().next();
welcomemessage.setVersion(57);
saved = welcomemessageService.save(welcomemessage);
welcomemessages = welcomemessageService.findAll();
Assert.isTrue(welcomemessages.contains(saved));
} 

@Test 
public void findAllWelcomeMessageTest() { 
Collection<WelcomeMessage> result; 
result = welcomemessageService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneWelcomeMessageTest(){ 
WelcomeMessage welcomemessage = welcomemessageService.findAll().iterator().next(); 
int welcomemessageId = welcomemessage.getId(); 
Assert.isTrue(welcomemessageId != 0); 
WelcomeMessage result; 
result = welcomemessageService.findOne(welcomemessageId); 
Assert.notNull(result); 
} 

@Test 
public void deleteWelcomeMessageTest() { 
WelcomeMessage welcomemessage = welcomemessageService.findAll().iterator().next(); 
Assert.notNull(welcomemessage); 
Assert.isTrue(welcomemessage.getId() != 0); 
Assert.isTrue(this.welcomemessageService.exists(welcomemessage.getId())); 
this.welcomemessageService.delete(welcomemessage); 
} 

} 
