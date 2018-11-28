
package services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;

@Service
@Transactional
public class ActorService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ActorRepository	actorRepository;
	@PersistenceContext
	EntityManager manager;
	
	public Collection<Actor> findSuspiciousActor() {
		return actorRepository.findSuspiciousActor();
	}

	// Supporting services ----------------------------------------------------

	// Simple CRUD methods ----------------------------------------------------
	
	public Collection<String> findAllUsername(int adminId) {
		return actorRepository.findAllUsername(adminId);
	}

	public Collection<Actor> findAll() {
		Collection<Actor> result;

		result = this.actorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	@SuppressWarnings("unchecked")
	public Collection<Actor> findByUsernames(Collection<String> usernames) {
		Assert.notNull(usernames);
		List<String> transformed = new LinkedList<String>();
		
		for(String e : usernames) {
			if(e != null) {
				transformed.add(String.format("'%s'", e));
			}
		}
		
		if(transformed.isEmpty()) {
			return new LinkedList<Actor>();
		}
		
		StringBuilder queryParam = new StringBuilder(transformed.toString());
		queryParam.deleteCharAt(0);
		queryParam.deleteCharAt(queryParam.length() -1);
		
		Query query = manager.createQuery(String.format("select c from Actor c where c.userAccount.username in (%s)", queryParam));
		
		return query.getResultList();
	}

	public Actor findSelf() {
		UserAccount account = LoginService.getPrincipal();
		Assert.notNull(account);
		
		return actorRepository.findSelf(account.getUsername());
	}

	public boolean exists(final Integer id) {
		return this.actorRepository.exists(id);
	}

	public Actor findOne(final int actorId) {
		Assert.isTrue(actorId != 0);

		Actor result;

		result = this.actorRepository.findOne(actorId);
		Assert.notNull(result);

		return result;
	}

	public Actor save(final Actor actor) {
		Assert.notNull(actor);

		Actor result;

		result = this.actorRepository.save(actor);

		return result;
	}

	public void delete(final Actor actor) {
		Assert.notNull(actor);
		Assert.isTrue(actor.getId() != 0);
		Assert.isTrue(this.actorRepository.exists(actor.getId()));

		this.actorRepository.delete(actor);
	}

	// Other business methods -------------------------------------------------

	public Actor findByPrincipal() {
		Actor actor;

		final UserAccount principalUserAccount = LoginService.getPrincipal();
		if (principalUserAccount == null)
			actor = null;
		else
			actor = this.actorRepository.findByUserAccountId(principalUserAccount.getId());

		return actor;
	}

}
