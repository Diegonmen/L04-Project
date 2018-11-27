
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RefereeRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Referee;

@Service
@Transactional
public class RefereeService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private RefereeRepository	refereeRepository;


	// Supporting services ----------------------------------------------------

	// Simple CRUD methods ----------------------------------------------------

	public Collection<Referee> findAll() {
		Collection<Referee> result;

		result = this.refereeRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public boolean exists(final Integer arg0) {
		return this.refereeRepository.exists(arg0);
	}

	public Referee findOne(final int refereeId) {
		Assert.isTrue(refereeId != 0);

		Referee result;

		result = this.refereeRepository.findOne(refereeId);
		Assert.notNull(result);

		return result;
	}

	public Referee save(final Referee referee) {
		Referee result, saved;
		final UserAccount logedUserAccount;
		Authority authority;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		authority = new Authority();
		authority.setAuthority("REFEREE");
		Assert.notNull(referee, "referee.not.null");

		if (this.exists(referee.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "referee.notLogged ");
			Assert.isTrue(logedUserAccount.equals(referee.getUserAccount()), "referee.notEqual.userAccount");
			saved = this.refereeRepository.findOne(referee.getId());
			Assert.notNull(saved, "referee.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(referee.getUserAccount().getUsername()), "referee.notEqual.username");
			Assert.isTrue(referee.getUserAccount().getPassword().equals(saved.getUserAccount().getPassword()), "referee.notEqual.password");
			Assert.isTrue(referee.getUserAccount().isAccountNonLocked() == saved.getUserAccount().isAccountNonLocked() && referee.isSuspicious() == saved.isSuspicious(), "referee.notEqual.accountOrSuspicious");

		} else {
			Assert.isTrue(referee.isSuspicious() == false, "referee.notSuspicious.false");
			referee.getUserAccount().setPassword(encoder.encodePassword(referee.getUserAccount().getPassword(), null));
			referee.getUserAccount().setEnabled(true);

		}

		result = this.refereeRepository.save(referee);

		return result;

	}

	public Referee create() {

		Referee result;
		UserAccount userAccount;
		Authority authority;

		result = new Referee();
		userAccount = new UserAccount();
		authority = new Authority();

		result.setSuspicious(false);

		authority.setAuthority("REFEREE");
		userAccount.addAuthority(authority);
		userAccount.setEnabled(true);

		result.setUserAccount(userAccount);

		return result;

	}

	public void delete(final Referee referee) {
		Assert.notNull(referee);
		Assert.isTrue(this.refereeRepository.exists(referee.getId()));
		this.refereeRepository.delete(referee);
	}

}
