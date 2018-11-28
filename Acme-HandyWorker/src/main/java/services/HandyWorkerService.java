
package services;

import java.util.Collection;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Application;
import domain.Box;
import domain.Customer;
import domain.Endorsement;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.SocialIdentity;
import domain.Tutorial;
import repositories.CustomerRepository;
import repositories.FixUpTaskRepository;
import repositories.HandyWorkerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class HandyWorkerService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private HandyWorkerRepository	handyWorkerRepository;

	@Autowired
	private CustomerRepository		customerRepository;
	
	@Autowired
	private FixUpTaskRepository		fixUpTaskRepository;
	
	// Supporting services ----------------------------------------------------

	// Simple CRUD methods ----------------------------------------------------

	public Collection<HandyWorker> findAll() {
		Collection<HandyWorker> result;

		result = this.handyWorkerRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public boolean exists(final Integer arg0) {
		return this.handyWorkerRepository.exists(arg0);
	}

	public HandyWorker findOne(final int handyWorkerId) {
		Assert.isTrue(handyWorkerId != 0);

		HandyWorker result;

		result = this.handyWorkerRepository.findOne(handyWorkerId);
		Assert.notNull(result);

		return result;
	}

	public HandyWorker save(final HandyWorker handyWorker) {
		HandyWorker result, saved;
		final UserAccount logedUserAccount;
		Authority authority;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		authority = new Authority();
		authority.setAuthority("HANDYWORKER");
		Assert.notNull(handyWorker, "handyWorker.not.null");

		if (this.exists(handyWorker.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "handyWorker.notLogged ");
			Assert.isTrue(logedUserAccount.equals(handyWorker.getUserAccount()), "handyWorker.notEqual.userAccount");
			saved = this.handyWorkerRepository.findOne(handyWorker.getId());
			Assert.notNull(saved, "handyWorker.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(handyWorker.getUserAccount().getUsername()), "handyWorker.notEqual.username");
			Assert.isTrue(handyWorker.getUserAccount().getPassword().equals(saved.getUserAccount().getPassword()), "handyWorker.notEqual.password");
			Assert.isTrue(handyWorker.getUserAccount().isAccountNonLocked() == saved.getUserAccount().isAccountNonLocked() && handyWorker.isSuspicious() == saved.isSuspicious(), "handyWorker.notEqual.accountOrSuspicious");

		} else {
			Assert.isTrue(handyWorker.isSuspicious() == false, "handyWorker.notSuspicious.false");
			handyWorker.getUserAccount().setPassword(encoder.encodePassword(handyWorker.getUserAccount().getPassword(), null));
			handyWorker.getUserAccount().setEnabled(true);

		}

		result = this.handyWorkerRepository.save(handyWorker);

		return result;

	}

	public HandyWorker create() {
		HandyWorker result;
		UserAccount userAccount;
		Authority authority;

		result = new HandyWorker();
		userAccount = new UserAccount();
		authority = new Authority();

		result.setSuspicious(false);

		authority.setAuthority("HANDYWORKER");
		userAccount.addAuthority(authority);
		userAccount.setEnabled(true);
		
		Collection<Application> applications = new LinkedList<>();
		result.setApplications(applications);
		Collection<Box> boxes = new LinkedList<>();
		result.setBoxes(boxes);
		Collection<Endorsement> endorsements = new LinkedList<>();
		result.setEndorsements(endorsements);
		Collection<SocialIdentity> socialIdentity = new LinkedList<>();
		result.setSocialIdentity(socialIdentity);
		Collection<Tutorial> tutorials = new LinkedList<>();
		result.setTutorials(tutorials);
		result.setUserAccount(userAccount);

		return result;
	}

	public void delete(final HandyWorker handyWorker) {
		Assert.notNull(handyWorker);
		Assert.isTrue(handyWorker.getId() != 0);

		this.handyWorkerRepository.delete(handyWorker);
	}

	public HandyWorker findHandyWorkerByUserAccount(final UserAccount userAccount) {
		HandyWorker result;

		Assert.isTrue(userAccount.getId() != 0);

		result = this.handyWorkerRepository.findByUserAccountId(userAccount.getId());

		return result;
	}

	public HandyWorker findByApplication(final Application application) {
		HandyWorker result;

		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);

		result = this.handyWorkerRepository.findByApplicationId(application.getId());

		return result;
	}

	public HandyWorker findByFixUpTask(final FixUpTask fixUpTask) {
		HandyWorker result;
		Assert.notNull(fixUpTask);
		Assert.isTrue(fixUpTask.getId() != 0);
		result = this.handyWorkerRepository.findByFixUpTaskId(fixUpTask.getId());
		return result;
	}
	
	public Customer findCustomerProfile(FixUpTask fixUpTask) {
		Customer res;
		Assert.notNull(fixUpTask);
		res = this.customerRepository.findCustomerByFixUpTaskId(fixUpTask.getId());
		Assert.notNull(res);
		return res;
	}
	
	public Collection<FixUpTask> allCustomerFixUpTask(Customer customer){
		Collection<FixUpTask> res = new LinkedList<>();
		Assert.notNull(customer);
		res = fixUpTaskRepository.findFixUpTasksByCustomer(customer.getId());
		Assert.notNull(res);
		return res;
	}
	
	public HandyWorker findHandyWorkerByApplication(Application application) {
		HandyWorker res;
		Assert.notNull(application);
		res = handyWorkerRepository.findByApplicationId(application.getId());
		Assert.notNull(res);
		return res;
	}
	
}
