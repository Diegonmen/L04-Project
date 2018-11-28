
package services;

import java.util.Collection;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Administrator;
import repositories.ActorRepository;
import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Actor;
import domain.Administrator;
import domain.Box;
import domain.Complaint;
import domain.Endorsement;
import domain.FixUpTask;
import domain.SocialIdentity;

@Service
@Transactional
public class AdministratorService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;

	@Autowired
	private ActorRepository			actorRepository;
	
	@Autowired
	LoginService loginservice;


	// Supporting services ----------------------------------------------------

	// Simple CRUD methods ----------------------------------------------------

	public Collection<Administrator> findAll() {
		Collection<Administrator> result;

		result = this.administratorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Collection<Actor> findSuspiciousActor() {
		final Collection<Actor> actors = new LinkedList<>();
		actors.addAll(this.actorRepository.findSuspiciousActor());
		return actors;

	}

	public boolean exists(final Integer arg0) {
		return this.administratorRepository.exists(arg0);
	}

	public Administrator findOne(final int administratorId) {
		Assert.isTrue(administratorId != 0);

		Administrator result;

		result = this.administratorRepository.findOne(administratorId);
		Assert.notNull(result);

		return result;
	}

	public Administrator save(final Administrator administrator) {
		Administrator result, saved;
		final UserAccount logedUserAccount;
		Authority authority;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		Assert.notNull(administrator, "administrator.not.null");

		if (this.exists(administrator.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "administrator.notLogged ");
			Assert.isTrue(logedUserAccount.equals(administrator.getUserAccount()), "administrator.notEqual.userAccount");
			saved = this.administratorRepository.findOne(administrator.getId());
			Assert.notNull(saved, "administrator.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(administrator.getUserAccount().getUsername()), "administrator.notEqual.username");
			Assert.isTrue(administrator.getUserAccount().getPassword().equals(saved.getUserAccount().getPassword()), "administrator.notEqual.password");
			Assert.isTrue(administrator.getUserAccount().isAccountNonLocked() == saved.getUserAccount().isAccountNonLocked() && administrator.isSuspicious() == saved.isSuspicious(), "administrator.notEqual.accountOrSuspicious");

		} else {
			Assert.isTrue(administrator.isSuspicious() == false, "administrator.notSuspicious.false");
			administrator.getUserAccount().setPassword(encoder.encodePassword(administrator.getUserAccount().getPassword(), null));
			administrator.getUserAccount().setEnabled(true);

		}

		result = this.administratorRepository.save(administrator);

		return result;

	}

	public Administrator create() {

		Administrator result;
		UserAccount userAccount;
		Authority authority;

		result = new Administrator();
		userAccount = new UserAccount();
		authority = new Authority();

		result.setSuspicious(false);

		authority.setAuthority("ADMINISTRATOR");
		userAccount.addAuthority(authority);
		userAccount.setEnabled(true);

		Collection<Box> boxes = new LinkedList<>();
		result.setBoxes(boxes);
		Collection<SocialIdentity> socialIdentity = new LinkedList<>();
		result.setSocialIdentity(socialIdentity);
		result.setUserAccount(userAccount);

		return result;

	}

	public void delete(final Administrator administrator) {
		Assert.notNull(administrator);
		Assert.isTrue(this.administratorRepository.exists(administrator.getId()));
		this.administratorRepository.delete(administrator);
	}
	
	public UserAccount changeEnabledActor(UserAccount userAccount) {
		Assert.notNull(userAccount);
		
		userAccount.setEnabled(!userAccount.isEnabled());
		return this.loginservice.save(userAccount);
	}

}
