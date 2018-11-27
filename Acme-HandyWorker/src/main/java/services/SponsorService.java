
package services;

import java.util.Collection;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Box;
import domain.SocialIdentity;
import domain.Sponsor;
import domain.Sponsorship;
import repositories.SponsorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class SponsorService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private SponsorRepository	sponsorRepository;


	// Supporting services ----------------------------------------------------

	// Simple CRUD methods ----------------------------------------------------

	public Collection<Sponsor> findAll() {
		Collection<Sponsor> result;

		result = this.sponsorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public boolean exists(final Integer arg0) {
		return this.sponsorRepository.exists(arg0);
	}

	public Sponsor findOne(final int sponsorId) {
		Assert.isTrue(sponsorId != 0);

		Sponsor result;

		result = this.sponsorRepository.findOne(sponsorId);
		Assert.notNull(result);

		return result;
	}

	public Sponsor save(final Sponsor sponsor) {
		Sponsor result, saved;
		final UserAccount logedUserAccount;
		Authority authority, authority2;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		authority = new Authority();
		authority.setAuthority("SPONSOR");
		authority2 = new Authority();
		authority2.setAuthority("ADMINISTRADOR");
		Assert.notNull(sponsor, "sponsor.not.null");

		if (this.exists(sponsor.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "sponsor.notLogged ");
			Assert.isTrue(logedUserAccount.equals(sponsor.getUserAccount()), "sponsor.notEqual.userAccount");
			saved = this.sponsorRepository.findOne(sponsor.getId());
			Assert.notNull(saved, "sponsor.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(sponsor.getUserAccount().getUsername()), "sponsor.notEqual.username");
			Assert.isTrue(sponsor.getUserAccount().getPassword().equals(saved.getUserAccount().getPassword()), "sponsor.notEqual.password");
			Assert.isTrue(sponsor.getUserAccount().isAccountNonLocked() == saved.getUserAccount().isAccountNonLocked() && sponsor.isSuspicious() == saved.isSuspicious(), "sponsor.notEqual.accountOrSuspicious");

		} else {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "admin.notLogged ");
			Assert.isTrue(logedUserAccount.getAuthorities().contains(authority2), "admin.notEqual.userAccount");
			Assert.isTrue(sponsor.isSuspicious() == false, "admin.notSuspicious.false");
			sponsor.getUserAccount().setPassword(encoder.encodePassword(sponsor.getUserAccount().getPassword(), null));
			sponsor.getUserAccount().setEnabled(true);

		}

		result = this.sponsorRepository.save(sponsor);

		return result;

	}

	public Sponsor create() {

		Sponsor result;
		UserAccount userAccount;
		Authority authority;

		result = new Sponsor();
		userAccount = new UserAccount();
		authority = new Authority();

		result.setSuspicious(false);

		authority.setAuthority("SPONSOR");
		userAccount.addAuthority(authority);
		userAccount.setEnabled(true);

		final Collection<Box> boxes = new LinkedList<>();
		result.setBoxes(boxes);
		final Collection<Sponsorship> sponsorships = new LinkedList<>();
		result.setSponsorships(sponsorships);
		final Collection<SocialIdentity> socialIdentities = new LinkedList<>();
		result.setSocialIdentity(socialIdentities);
		result.setUserAccount(userAccount);

		return result;

	}

	public void delete(final Sponsor sponsor) {
		Assert.notNull(sponsor);
		Assert.isTrue(this.sponsorRepository.exists(sponsor.getId()));
		this.sponsorRepository.delete(sponsor);
	}

}
