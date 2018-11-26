package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.CreditCard;
import services.CreditCardService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class CreditCardServiceTest extends AbstractTest { 

@Autowired 
private CreditCardService	creditcardService; 

@Test 
public void saveCreditCardTest(){ 
CreditCard creditcard, saved;
Collection<CreditCard> creditcards;
creditcard = creditcardService.findAll().iterator().next();
creditcard.setVersion(57);
saved = creditcardService.save(creditcard);
creditcards = creditcardService.findAll();
Assert.isTrue(creditcards.contains(saved));
} 

@Test 
public void findAllCreditCardTest() { 
Collection<CreditCard> result; 
result = creditcardService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneCreditCardTest(){ 
CreditCard creditcard = creditcardService.findAll().iterator().next(); 
int creditcardId = creditcard.getId(); 
Assert.isTrue(creditcardId != 0); 
CreditCard result; 
result = creditcardService.findOne(creditcardId); 
Assert.notNull(result); 
} 

@Test 
public void deleteCreditCardTest() { 
CreditCard creditcard = creditcardService.findAll().iterator().next(); 
Assert.notNull(creditcard); 
Assert.isTrue(creditcard.getId() != 0); 
Assert.isTrue(this.creditcardService.exists(creditcard.getId())); 
this.creditcardService.delete(creditcard); 
} 

} 
