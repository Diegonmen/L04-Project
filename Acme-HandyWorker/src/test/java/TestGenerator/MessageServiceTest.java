package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Message;
import services.MessageService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class MessageServiceTest extends AbstractTest { 

@Autowired 
private MessageService	messageService; 

@Test 
public void saveMessageTest(){ 
Message message, saved;
Collection<Message> messages;
message = messageService.findAll().iterator().next();
message.setVersion(57);
saved = messageService.save(message);
messages = messageService.findAll();
Assert.isTrue(messages.contains(saved));
} 

@Test 
public void findAllMessageTest() { 
Collection<Message> result; 
result = messageService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneMessageTest(){ 
Message message = messageService.findAll().iterator().next(); 
int messageId = message.getId(); 
Assert.isTrue(messageId != 0); 
Message result; 
result = messageService.findOne(messageId); 
Assert.notNull(result); 
} 

@Test 
public void deleteMessageTest() { 
Message message = messageService.findAll().iterator().next(); 
Assert.notNull(message); 
Assert.isTrue(message.getId() != 0); 
Assert.isTrue(this.messageService.exists(message.getId())); 
this.messageService.delete(message); 
} 

} 
