package services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Box;
import repositories.BoxRepository;

@Service
@Transactional
public class BoxServices {
	
	@Autowired
	private BoxRepository boxrepository;
	@Autowired
	private ActorService actorservice;
	
	public Box newBox(Box name) {
		Actor current = actorservice.findSelf();
		
		Box saved = boxrepository.save(name);
		
		current.getBoxes().add(saved);
		
		actorservice.save(current);
		
		return saved;
	}
	
	public boolean exists(Integer id) {
		return actorservice.exists(id);
	}

	public Box findInbox(Actor a) {
		Assert.notNull(a);
		
		for(Box b : a.getBoxes()) {
			if("INBOX".equals(b.getName())) {
				return b;
			}
		}
		
		return null;
	}

	public Box findOutbox(Actor a) {
		Assert.notNull(a);
		
		for(Box b : a.getBoxes()) {
			if("OUTBOX".equals(b.getName())) {
				return b;
			}
		}
		
		return null;
	}
	
	public Box findTashBox(Actor a) {
		Assert.notNull(a);
		
		for(Box b : a.getBoxes()) {
			if("TRASHBOX".equals(b.getName())) {
				return b;
			}
		}
		
		return null;
	}

	public Box getOutBoxFolderFromActorId(int id) {
		return boxrepository.getOutBoxFolderFromActorId(id);
	}

	public Box getInBoxFolderFromActorId(int id) {
		return boxrepository.getInBoxFolderFromActorId(id);
	}

	public Box getSpamBoxFolderFromActorId(int id) {
		return boxrepository.getSpamBoxFolderFromActorId(id);
	}

	public Box getTrashBoxFolderFromActorId(int id) {
		return boxrepository.getTrashBoxFolderFromActorId(id);
	}
	
	public List<Box> save(Iterable<Box> entities) {
		return boxrepository.save(entities);
	}

	public Box save(Box entity) {
		Assert.notNull(entity);
		return boxrepository.save(entity);
	}

	public Collection<Box> findBoxesByUserAccountId(int userAccountId) {
		return boxrepository.findBoxesByUserAccountId(userAccountId);
	}

	public List<Box> findAll() {
		return boxrepository.findAll();
	}

	public Box findOne(Integer id) {
		Assert.notNull(id);
		return boxrepository.findOne(id);
	}

	public void delete(Box entity) {
		Assert.notNull(entity);
		Assert.isTrue(!"INBOX".equals(entity.getName()) && !"OUTBOX".equals(entity.getName()) && !"TRASHBOX".equals(entity.getName()) && !"SPAMBOX".equals(entity.getName()));
		boxrepository.delete(entity);
	}

}
