package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Box;
import domain.Message;
import repositories.MessageRepository;

@Service
@Transactional
public class MessageService {

	@Autowired
	private MessageRepository messagerepository;
	@Autowired
	private ActorService actorservice;
	@Autowired
	private BoxServices boxservices;

	public Message save(Message entity) {
		return messagerepository.save(entity);
	}

	public List<Message> findAll() {
		return messagerepository.findAll();
	}

	public Message findOne(Integer id) {
		return messagerepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return messagerepository.exists(id);
	}
	
	public void delete(Message entity) {
		messagerepository.delete(entity);
	}
	
	public Box moveTo(Box src, Message message) {
		Assert.notNull(src);
		Assert.notNull(message);
		
		Actor self = actorservice.findSelf();
		
		for(Box b : self.getBoxes()) {
			b.getMessages().remove(message);
		}
		
		boxservices.save(self.getBoxes());
		
		src.getMessages().add(message);
		
		return boxservices.save(src);
	}
	
	public void removeMessage(Message message) {
		Assert.notNull(message);
		Actor current = actorservice.findSelf();
		
		Box outbox = boxservices.findTashBox(current);
		
		if(outbox.getMessages().contains(message)) {
			message.getRecipients().remove(current);
			
			message = messagerepository.save(message);
			
			if(message.getRecipients().isEmpty()) {
				messagerepository.delete(message.getId());
			}
		} else {
			for(Box box : current.getBoxes()) {
				if("TRASHBOX".equals(box.getName())) {
					continue;
				}
				
				box.getMessages().remove(message);
			}
			
			outbox.getMessages().add(message);
			
			boxservices.save(current.getBoxes());
		}
	}

	public Message sendMessage(Collection<String> usernames, Message message) {
		Actor current = actorservice.findSelf();
		
		Collection<Actor> recipents = actorservice.findByUsernames(usernames);
		message.setRecipients(recipents);
		message.setSender(current);
		message.setMoment(new Date());
		
		Message saved = messagerepository.save(message);
		
		for(Actor a : recipents) {
			Box inbox = boxservices.findInbox(a);
			inbox.getMessages().add(saved);
			
			boxservices.save(inbox);
		}
		
		Box outbox = null;
		
		for(Box b : current.getBoxes()) {
			if("OUTBOX".equals(b.getName())) {
				outbox = b;
				break;
			}
		}
		outbox.getMessages().add(saved);
		
		boxservices.save(outbox);
		
		return saved;
	}
	
	
}
