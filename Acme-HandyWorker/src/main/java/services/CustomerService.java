
package services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Application;
import domain.Box;
import domain.Complaint;
import domain.Customer;
import domain.Endorsement;
import domain.FixUpTask;
import domain.SocialIdentity;
import repositories.CustomerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class CustomerService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CustomerRepository	customerRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private FixUpTaskService	fixUpTaskService;


	// Simple CRUD methods ----------------------------------------------------

	public Collection<Customer> findAll() {
		Collection<Customer> result;

		result = this.customerRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public boolean exists(final Integer arg0) {
		return this.customerRepository.exists(arg0);
	}

	public Customer findOne(final int customerId) {
		Assert.isTrue(customerId != 0);

		Customer result;

		result = this.customerRepository.findOne(customerId);
		Assert.notNull(result);

		return result;
	}

	public Customer save(final Customer customer) {
		Customer result, saved;
		final UserAccount logedUserAccount;
		Authority authority;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		authority = new Authority();
		authority.setAuthority("CUSTOMER");
		Assert.notNull(customer, "customer.not.null");

		if (this.exists(customer.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "customer.notLogged ");
			Assert.isTrue(logedUserAccount.equals(customer.getUserAccount()), "customer.notEqual.userAccount");
			saved = this.customerRepository.findOne(customer.getId());
			Assert.notNull(saved, "customer.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(customer.getUserAccount().getUsername()), "customer.notEqual.username");
			Assert.isTrue(customer.getUserAccount().getPassword().equals(saved.getUserAccount().getPassword()), "customer.notEqual.password");
			Assert.isTrue(customer.getUserAccount().isAccountNonLocked() == saved.getUserAccount().isAccountNonLocked() && customer.isSuspicious() == saved.isSuspicious(), "customer.notEqual.accountOrSuspicious");

		} else {
			Assert.isTrue(customer.isSuspicious() == false, "customer.notSuspicious.false");
			customer.getUserAccount().setPassword(encoder.encodePassword(customer.getUserAccount().getPassword(), null));
			customer.getUserAccount().setEnabled(true);

		}

		result = this.customerRepository.save(customer);

		return result;

	}

	public Customer create() {

		Customer result;
		UserAccount userAccount;
		Authority authority;

		result = new Customer();
		userAccount = new UserAccount();
		authority = new Authority();

		result.setSuspicious(false);

		authority.setAuthority("CUSTOMER");
		userAccount.addAuthority(authority);
		userAccount.setEnabled(true);

		Collection<FixUpTask> fixUpTasks = new LinkedList<>();
		result.setFixUpTasks(fixUpTasks);
		Collection<Box> boxes = new LinkedList<>();
		result.setBoxes(boxes);
		Collection<Endorsement> endorsements = new LinkedList<>();
		result.setEndorsements(endorsements);
		Collection<SocialIdentity> socialIdentity = new LinkedList<>();
		result.setSocialIdentity(socialIdentity);
		Collection<Complaint> complaints = new LinkedList<>();
		result.setComplaints(complaints);
		result.setUserAccount(userAccount);

		return result;

	}

	public void delete(final Customer customer) {
		Assert.notNull(customer);
		Assert.isTrue(this.customerRepository.exists(customer.getId()));
		this.customerRepository.delete(customer);
	}

	public Customer findCustomerByApplication(final Application application) {
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		final Customer res = this.customerRepository.findCustomerByApplicationId(application.getId());
		return res;
	}

	public Customer findCustomerByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Assert.isTrue(userAccount.getId() != 0);
		final Customer res = this.customerRepository.findByUserAccountId(userAccount.getId());
		return res;
	}

	public Customer findCustomerByFixUpTask(final FixUpTask fixUpTask) {
		Assert.notNull(fixUpTask);
		Assert.isTrue(fixUpTask.getId() != 0);
		final Customer res = this.customerRepository.findCustomerByFixUpTaskId(fixUpTask.getId());
		
		return res;
	}
	
	public FixUpTask findOneFixUptask(final int fixUpTaskId) {
		Assert.isTrue(fixUpTaskId != 0);
		final UserAccount logedUserAccount;
		Authority authority;
		FixUpTask result;
		authority = new Authority();
		authority.setAuthority("CUSTOMER");
		logedUserAccount = LoginService.getPrincipal();

		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));

		result = this.fixUpTaskService.findOne(fixUpTaskId);
		Assert.notNull(result);
		Assert.isTrue(findCustomerByFixUpTask(result).getUserAccount().equals(logedUserAccount));

		return result;
	}

	public List<FixUpTask> findAllFixUpTask() {
		return fixUpTaskService.findAll();
	}

	public FixUpTask saveCustomerFixUpTask(final FixUpTask fixUpTask) {
		FixUpTask result, saved;
		final UserAccount logedUserAccount;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("CUSTOMER");
		Assert.notNull(fixUpTask, "fixUpTask.not.null");
		final Customer customer = findCustomerByFixUpTask(fixUpTask);

		if (this.exists(fixUpTask.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "customer.notLogged ");
			Assert.isTrue(logedUserAccount.equals(customer.getUserAccount()), "customer.notEqual.userAccount");
			saved = this.fixUpTaskService.findOne(fixUpTask.getId());
			Assert.notNull(saved, "fixUpTask.not.null");
			Assert.isTrue(customer.getUserAccount().isAccountNonLocked() && !(customer.isSuspicious()),
					"customer.notEqual.accountOrSuspicious");
			result = this.fixUpTaskService.save(fixUpTask);
			Assert.notNull(result);

		} else {
			result = this.fixUpTaskService.save(fixUpTask);
			Assert.notNull(result);
		}
		return result;

	}
	
	public void deleteFixUpTask(final FixUpTask fixUpTask) {
		Assert.isTrue(fixUpTask.getId() != 0);
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("CUSTOMER");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));
		Assert.isTrue(
			findCustomerByFixUpTask(fixUpTask).getUserAccount().equals(logedUserAccount));
			this.fixUpTaskService.delete(fixUpTask);
	}
	
	
}
