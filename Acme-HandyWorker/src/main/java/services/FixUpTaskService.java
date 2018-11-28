
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

import domain.Customer;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Phase;
import repositories.FixUpTaskRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
@SuppressWarnings("unchecked")
public class FixUpTaskService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private FixUpTaskRepository fixUpTaskRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private HandyWorkerService handyWorkerService;

	@PersistenceContext
	EntityManager entitymanager;

	// Simple CRUD methods ----------------------------------------------------

	public FixUpTask findOne(final int fixUpTaskId) {
		Assert.isTrue(fixUpTaskId != 0);
		final UserAccount logedUserAccount;
		Authority authority;
		FixUpTask result;
		authority = new Authority();
		authority.setAuthority("CUSTOMER");
		logedUserAccount = LoginService.getPrincipal();

		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));

		result = this.fixUpTaskRepository.findOne(fixUpTaskId);
		Assert.notNull(result);
		Assert.isTrue(this.customerService.findCustomerByFixUpTask(result).getUserAccount().equals(logedUserAccount));

		return result;
	}

	public List<FixUpTask> findAll() {
		return fixUpTaskRepository.findAll();
	}

	public FixUpTask saveCustomer(final FixUpTask fixUpTask) {
		FixUpTask result, saved;
		final UserAccount logedUserAccount;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("CUSTOMER");
		Assert.notNull(fixUpTask, "fixUpTask.not.null");
		final Customer customer = this.customerService.findCustomerByFixUpTask(fixUpTask);

		if (this.exists(fixUpTask.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "customer.notLogged ");
			Assert.isTrue(logedUserAccount.equals(customer.getUserAccount()), "customer.notEqual.userAccount");
			saved = this.fixUpTaskRepository.findOne(fixUpTask.getId());
			Assert.notNull(saved, "fixUpTask.not.null");
			Assert.isTrue(customer.getUserAccount().isAccountNonLocked() && !(customer.isSuspicious()),
					"customer.notEqual.accountOrSuspicious");
			result = this.fixUpTaskRepository.save(fixUpTask);
			Assert.notNull(result);

		} else {
			result = this.fixUpTaskRepository.save(fixUpTask);
			Assert.notNull(result);
		}
		return result;

	}

	public FixUpTask saveHandyWorker(final FixUpTask fixUpTask, Collection<Phase> phases) {
		FixUpTask result, saved;
		final UserAccount logedUserAccount;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("HANDYWORKER");
		Assert.notNull(fixUpTask, "fixUpTask.not.null");
		final HandyWorker handyWorker = this.handyWorkerService.findByFixUpTask(fixUpTask);

		if (this.exists(fixUpTask.getId()) && this.applicationService
				.findAcceptedHandyWorkerApplicationByFixUpTask(fixUpTask).getStatus().equals("ACCEPTED")) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "handyWorker.notLogged ");
			Assert.isTrue(logedUserAccount.equals(handyWorker.getUserAccount()), "handyWorker.notEqual.userAccount");
			saved = this.fixUpTaskRepository.findOne(fixUpTask.getId());
			Assert.notNull(saved, "fixUpTask.not.null");
			Assert.isTrue(handyWorker.getUserAccount().isAccountNonLocked() && !(handyWorker.isSuspicious()),
					"customer.notEqual.accountOrSuspicious");
			if(!phases.isEmpty()) {
			fixUpTask.getPhases().addAll(phases);
			}
			result = this.fixUpTaskRepository.save(fixUpTask);
			Assert.notNull(result);
			return result;
		}
		result = this.fixUpTaskRepository.findOne(fixUpTask.getId());
		return result;

	}
	
	public FixUpTask addPhases(FixUpTask fixUpTask) {
		
		return fixUpTask;
	}

	public void delete(final FixUpTask fixUpTask) {
		Assert.isTrue(fixUpTask.getId() != 0);
		UserAccount logedUserAccount;
		Authority authority;
		authority = new Authority();
		authority.setAuthority("CUSTOMER");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));
		Assert.isTrue(
				this.customerService.findCustomerByFixUpTask(fixUpTask).getUserAccount().equals(logedUserAccount));
		this.fixUpTaskRepository.delete(fixUpTask);
	}

	public boolean exists(final Integer id) {
		return this.fixUpTaskRepository.exists(id);
	}

	// Other business methods

	public Collection<FixUpTask> findByCustomer(final Customer customer) {
		Assert.notNull(customer);
		Assert.isTrue(this.customerService.exists(customer.getId()));

		Collection<FixUpTask> result;
		result = this.fixUpTaskRepository.findFixUpTasksByCustomer(customer.getId());

		return result;
	}

	public List<FixUpTask> filter(String command, int maxResults) {
		Query query = entitymanager.createQuery(
				"select c from FixUpTask c where c.ticker like CONCAT('%',:command,'%') or c.description like CONCAT('%',:command,'%') or c.address like CONCAT('%',:command,'%') or c.maxPrice = :command")
				.setMaxResults(maxResults);
		query.setParameter("command", command);

		List<FixUpTask> fixuptask = query.getResultList();

		return fixuptask;
	}

}
