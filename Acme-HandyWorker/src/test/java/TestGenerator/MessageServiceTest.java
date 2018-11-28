package TestGenerator;

import java.util.Arrays;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Box;
import domain.Message;
import services.ActorService;
import services.BoxServices;
import services.MessageService;
import utilities.AbstractTest;

@ContextConfiguration(locations = { "classpath:spring/junit.xml", "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MessageServiceTest extends AbstractTest {

	@Autowired
	private MessageService messageService;
	@Autowired
	private ActorService actorservice;
	@Autowired
	private BoxServices boxservices;
	
	@Test
	public void moveMessage() {
		Actor sender = actorservice.findByUsernames(Arrays.asList("admin1")).iterator().next();
		this.authenticate(sender.getUserAccount().getUsername());
		
		Box inbox = boxservices.findInbox(sender);
		Box trashbox = boxservices.findTashBox(sender);
		Message message = inbox.getMessages().iterator().next();
		
		messageService.moveTo(trashbox, message);
		
		inbox = boxservices.findInbox(sender);
		trashbox = boxservices.findTashBox(sender);
		
		Assert.isTrue(!inbox.getMessages().contains(message));
		Assert.isTrue(trashbox.getMessages().contains(message));
	}
	
	@Test
	public void removeMessage() {
		Actor sender = actorservice.findByUsernames(Arrays.asList("admin1")).iterator().next();
		this.authenticate(sender.getUserAccount().getUsername());
		
		Box inbox = boxservices.findInbox(sender);
		Box trashbox  = boxservices.findOutbox(sender);
		
		Message message = inbox.getMessages().iterator().next();
		
		messageService.removeMessage(message);
		
		inbox = boxservices.findInbox(sender);
		trashbox = boxservices.findTashBox(sender);
		
		Assert.isTrue(trashbox.getMessages().contains(message));
		
		messageService.removeMessage(message);
		
		inbox = boxservices.findInbox(sender);
		trashbox = boxservices.findInbox(sender);
		
		Assert.isTrue(!trashbox.getMessages().contains(message));
	}
	
	@Test
	public void sendMessage() {
		Actor sender = actorservice.findByUsernames(Arrays.asList("admin1")).iterator().next();
		this.authenticate(sender.getUserAccount().getUsername());
		
		Message message = new Message();
		message.setBody("sample body");
		message.setSubject("query subject unique");
		message.setPriority(Message.NEUTRAL);
		
		Message saved = messageService.sendMessage(Arrays.asList("admin1", "handyWorker1"), message);
		
		Collection<Actor> actorsSends = actorservice.findByUsernames(Arrays.asList("admin1", "handyWorker1"));
		
		boolean result = true;
		
		for(Actor a : actorsSends) {
			Box inbox = boxservices.findInbox(a);
			result = result && inbox.getMessages().contains(saved);
		}
		
		Actor self = actorservice.findSelf();
		
		Box outbox = boxservices.findOutbox(self);
		
		result = result && outbox.getMessages().contains(saved);
		
		Assert.isTrue(result);
	}

	@Test
	public void saveMessageTest() {
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
	public void findOneMessageTest() {
		Message message = messageService.findAll().iterator().next();
		int messageId = message.getId();
		Assert.isTrue(messageId != 0);
		Message result;
		result = messageService.findOne(messageId);
		Assert.notNull(result);
	}

	@Test
	public void deleteMessageTest() {
		Actor sender = actorservice.findByUsernames(Arrays.asList("admin1")).iterator().next();
		this.authenticate(sender.getUserAccount().getUsername());
		
		Box inbox = boxservices.findInbox(sender);
		Message message = inbox.getMessages().iterator().next();
		
		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);
		Assert.isTrue(this.messageService.exists(message.getId()));
		
		this.messageService.delete(message);
	}

}
