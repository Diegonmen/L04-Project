
package services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Administrator;
import domain.Box;
import domain.Category;
import domain.Message;
import domain.Referee;
import domain.SocialIdentity;
import domain.Sponsor;
import domain.Warranty;
import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class AdministratorService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;

	// Supporting services ----------------------------------------------------
	
	@Autowired
	private ActorService			actorservice;
	
	@Autowired
	LoginService loginservice;
	
	@Autowired
	private MessageService messageservice;
	
	@Autowired
	private WarrantyService warrantyService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private RefereeService refereeService;
	
	@Autowired
	private SponsorService sponsorService;


	// Simple CRUD methods ----------------------------------------------------
	
	public void sendAll(Message message) {
		Assert.notNull(message);
		
		Actor self = actorservice.findSelf();
		messageservice.sendMessage(actorservice.findAllUsername(self.getId()), message);
	}

	public Collection<Administrator> findAll() {
		Collection<Administrator> result;

		result = this.administratorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Collection<Actor> findSuspiciousActor() {
		final Collection<Actor> actors = new LinkedList<>();
		actors.addAll(this.actorservice.findSuspiciousActor());
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
			Collection<Message> messages = new LinkedList<>();
			Box inbox = new Box();
			inbox.setName("INBOX");
			inbox.setPredefined(true);
			inbox.setMessages(messages);
			Box outbox = new Box();
			outbox.setName("OUTBOX");
			outbox.setPredefined(true);
			outbox.setMessages(messages);
			Box trashbox = new Box();
			trashbox.setName("TRASHBOX");
			trashbox.setPredefined(true);
			trashbox.setMessages(messages);
			Box spambox = new Box();
			spambox.setName("INBOX");
			spambox.setPredefined(true);
			spambox.setMessages(messages);
			Collection<Box> boxes = new LinkedList<Box>();
			boxes.add(inbox);
			boxes.add(outbox);
			boxes.add(trashbox);
			boxes.add(spambox);
			administrator.setBoxes(boxes);

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
	
	public Warranty saveWarranty(Warranty warranty) {
		Warranty result, saved;
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		
		if(exists(warranty.getId()) && logedUserAccount.getAuthorities().contains(authority) && !warranty.isFinalMode()) {
			saved = this.warrantyService.findOne(warranty.getId());
			Assert.notNull(saved);
			result = this.warrantyService.save(warranty);
			Assert.notNull(result);
			return result;
		}
		
		result = this.warrantyService.findOne(warranty.getId());
		return result;
	}

	public List<Warranty> findAllWarranties() {
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));
		return warrantyService.findAll();
	}

	public Warranty findOneWarranty(Integer warrantyId) {
		Assert.isTrue(exists(warrantyId));
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));
		return warrantyService.findOne(warrantyId);
	}

	public void deleteWarranty(Warranty warranty) {
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority) && !warranty.isFinalMode());
		warrantyService.delete(warranty);
	}

	public Category saveCategory(Category category) {
		Category result, saved;
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		
		if(exists(category.getId()) && logedUserAccount.getAuthorities().contains(authority)) {
			saved = this.categoryService.findOne(category.getId());
			Assert.notNull(saved);
			result = this.categoryService.save(category);
			Assert.notNull(result);
			return result;
		}
		
		result = this.categoryService.findOne(category.getId());
		return result;
	}

	public List<Category> findAllCategories() {
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));
		return categoryService.findAll();
	}

	public Category findOneCategory(Integer categoryId) {
		Assert.isTrue(exists(categoryId));
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));
		return categoryService.findOne(categoryId);
	}

	public void deleteCategory(Category category) {
		Assert.notNull(category);
		Assert.isTrue(exists(category.getId()));
		
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));
		categoryService.delete(category);
	}

	public Sponsor saveSponsor(final Sponsor sponsor) {
		Sponsor result, saved;
		final UserAccount logedUserAccount;
		Authority authority1;
		Authority authority2;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		authority1 = new Authority();
		authority1.setAuthority("REFEREE");
		authority2 = new Authority();
		authority2.setAuthority("ADMINISTRATOR");
		Assert.notNull(sponsor, "sponsor.not.null");

		if (this.exists(sponsor.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "sponsor.notLogged ");
			Assert.isTrue(logedUserAccount.equals(sponsor.getUserAccount()), "sponsor.notEqual.userAccount");
			saved = this.sponsorService.findOne(sponsor.getId());
			Assert.notNull(saved, "referee.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(sponsor.getUserAccount().getUsername()), "sponsor.notEqual.username");
			Assert.isTrue(sponsor.getUserAccount().getPassword().equals(saved.getUserAccount().getPassword()), "sponsor.notEqual.password");
			Assert.isTrue(sponsor.getUserAccount().isAccountNonLocked() == saved.getUserAccount().isAccountNonLocked() && sponsor.isSuspicious() == saved.isSuspicious(), "referee.notEqual.accountOrSuspicious");

		} else {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "admin.notLogged ");
			Assert.isTrue(logedUserAccount.getAuthorities().contains(authority2), "admin.notEqual.userAccount");
			Assert.isTrue(sponsor.isSuspicious() == false, "admin.notSuspicious.false");
			sponsor.getUserAccount().setPassword(encoder.encodePassword(sponsor.getUserAccount().getPassword(), null));
			sponsor.getUserAccount().setEnabled(true);
			Collection<Message> messages = new LinkedList<>();
			Box inbox = new Box();
			inbox.setName("INBOX");
			inbox.setPredefined(true);
			inbox.setMessages(messages);
			Box outbox = new Box();
			outbox.setName("OUTBOX");
			outbox.setPredefined(true);
			outbox.setMessages(messages);
			Box trashbox = new Box();
			trashbox.setName("TRASHBOX");
			trashbox.setPredefined(true);
			trashbox.setMessages(messages);
			Box spambox = new Box();
			spambox.setName("INBOX");
			spambox.setPredefined(true);
			spambox.setMessages(messages);
			Collection<Box> boxes = new LinkedList<Box>();
			boxes.add(inbox);
			boxes.add(outbox);
			boxes.add(trashbox);
			boxes.add(spambox);
			sponsor.setBoxes(boxes);

		}

		result = this.sponsorService.save(sponsor);

		return result;

	}
	
	public Referee saveReferee(final Referee referee) {
		Referee result, saved;
		final UserAccount logedUserAccount;
		Authority authority1;
		Authority authority2;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		authority1 = new Authority();
		authority1.setAuthority("REFEREE");
		authority2 = new Authority();
		authority2.setAuthority("ADMINISTRATOR");
		Assert.notNull(referee, "referee.not.null");

		if (this.exists(referee.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "referee.notLogged ");
			Assert.isTrue(logedUserAccount.equals(referee.getUserAccount()), "referee.notEqual.userAccount");
			saved = this.refereeService.findOne(referee.getId());
			Assert.notNull(saved, "referee.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(referee.getUserAccount().getUsername()), "referee.notEqual.username");
			Assert.isTrue(referee.getUserAccount().getPassword().equals(saved.getUserAccount().getPassword()), "referee.notEqual.password");
			Assert.isTrue(referee.getUserAccount().isAccountNonLocked() == saved.getUserAccount().isAccountNonLocked() && referee.isSuspicious() == saved.isSuspicious(), "referee.notEqual.accountOrSuspicious");

		} else {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "admin.notLogged ");
			Assert.isTrue(logedUserAccount.getAuthorities().contains(authority2), "admin.notEqual.userAccount");
			Assert.isTrue(referee.isSuspicious() == false, "admin.notSuspicious.false");
			referee.getUserAccount().setPassword(encoder.encodePassword(referee.getUserAccount().getPassword(), null));
			referee.getUserAccount().setEnabled(true);
			Collection<Message> messages = new LinkedList<>();
			Box inbox = new Box();
			inbox.setName("INBOX");
			inbox.setPredefined(true);
			inbox.setMessages(messages);
			Box outbox = new Box();
			outbox.setName("OUTBOX");
			outbox.setPredefined(true);
			outbox.setMessages(messages);
			Box trashbox = new Box();
			trashbox.setName("TRASHBOX");
			trashbox.setPredefined(true);
			trashbox.setMessages(messages);
			Box spambox = new Box();
			spambox.setName("INBOX");
			spambox.setPredefined(true);
			spambox.setMessages(messages);
			Collection<Box> boxes = new LinkedList<Box>();
			boxes.add(inbox);
			boxes.add(outbox);
			boxes.add(trashbox);
			boxes.add(spambox);
			referee.setBoxes(boxes);

		}

		result = this.refereeService.save(referee);

		return result;

	}
}
