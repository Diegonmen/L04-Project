
package services;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
import domain.Message;
import domain.Phase;
import domain.Report;
import domain.SocialIdentity;
import domain.Tutorial;
import repositories.HandyWorkerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
@SuppressWarnings("unchecked")
public class HandyWorkerService {

	@PersistenceContext
	EntityManager entitymanager;
	// Managed repository -----------------------------------------------------

	@Autowired
	private HandyWorkerRepository handyWorkerRepository;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private FixUpTaskService fixUpTaskService;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private ReportService reportService;

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
			Assert.isTrue(saved.getUserAccount().getUsername().equals(handyWorker.getUserAccount().getUsername()),
					"handyWorker.notEqual.username");
			Assert.isTrue(handyWorker.getUserAccount().getPassword().equals(saved.getUserAccount().getPassword()),
					"handyWorker.notEqual.password");
			Assert.isTrue(
					handyWorker.getUserAccount().isAccountNonLocked() == saved.getUserAccount().isAccountNonLocked()
							&& handyWorker.isSuspicious() == saved.isSuspicious(),
					"handyWorker.notEqual.accountOrSuspicious");

		} else {
			Assert.isTrue(handyWorker.isSuspicious() == false, "handyWorker.notSuspicious.false");
			handyWorker.getUserAccount()
					.setPassword(encoder.encodePassword(handyWorker.getUserAccount().getPassword(), null));
			handyWorker.getUserAccount().setEnabled(true);
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
			handyWorker.setBoxes(boxes);

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
		res = this.customerService.findCustomerByFixUpTask(fixUpTask);
		Assert.notNull(res);
		return res;
	}

	public Collection<FixUpTask> allCustomerFixUpTask(Customer customer) {
		Collection<FixUpTask> res = new LinkedList<>();
		Assert.notNull(customer);
		res = fixUpTaskService.findFixUpTasksByCustomer(customer);
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

	public FixUpTask saveHandyWorkerFixUpTask(final FixUpTask fixUpTask, Collection<Phase> phases) {
		FixUpTask result, saved;
		final UserAccount logedUserAccount;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("HANDYWORKER");
		Assert.notNull(fixUpTask, "fixUpTask.not.null");
		final HandyWorker handyWorker = findByFixUpTask(fixUpTask);

		if (this.exists(fixUpTask.getId()) && this.applicationService
				.findAcceptedHandyWorkerApplicationByFixUpTask(fixUpTask).getStatus().equals("ACCEPTED")) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "handyWorker.notLogged ");
			Assert.isTrue(logedUserAccount.equals(handyWorker.getUserAccount()), "handyWorker.notEqual.userAccount");
			saved = this.fixUpTaskService.findOne(fixUpTask.getId());
			Assert.notNull(saved, "fixUpTask.not.null");
			Assert.isTrue(handyWorker.getUserAccount().isAccountNonLocked() && !(handyWorker.isSuspicious()),
					"customer.notEqual.accountOrSuspicious");
			if (!phases.isEmpty()) {
				fixUpTask.getPhases().addAll(phases);
			}
			result = this.fixUpTaskService.save(fixUpTask);
			Assert.notNull(result);
			return result;
		} else {
			result = this.fixUpTaskService.findOne(fixUpTask.getId());
			return result;
		}
	}

	public Application saveHandyWorkerApplication(final Application application, String comment) {
		final Application result, saved;
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		final UserAccount userAccount = LoginService.getPrincipal();
		final Date currentMoment = new Date(System.currentTimeMillis() - 1);
		final Authority authority;
		final UserAccount logedUserAccount;
		authority = new Authority();
		authority.setAuthority("HANDYWORKER");

		if (this.exists(application.getId()) && application.getStatus().equals("PENDING")
				&& userAccount.getAuthorities().contains(authority)
				&& applicationService
						.findApplicationsByCustomer(this.customerService.findCustomerByApplication(application))
						.contains(application)) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "customer.notLogged ");
			Assert.isTrue(
					logedUserAccount
							.equals(this.customerService.findCustomerByApplication(application).getUserAccount()),
					"handyWorker.notEqual.userAccount");
			if (application.getApplicationMoment().compareTo(currentMoment) < 0) {
				saved = this.applicationService.findOne(application.getId());
				Assert.notNull(saved, "application.not.null");
				if (!comment.equals(null)) {
					application.getComments().add(logedUserAccount.getUsername() + ": - " + comment);
				}
				result = this.applicationService.save(application);
				return result;
			} else {
				saved = this.applicationService.findOne(application.getId());
				Assert.notNull(saved, "application.not.null");
				if (!comment.equals(null)) {
					application.getComments().add(logedUserAccount.getUsername() + ": - " + comment);
				}
				application.setStatus("ACCEPTED");
				result = this.applicationService.save(application);
				return result;
			}
		} else {

			result = this.applicationService.save(application);
			return result;
		}
	}

	public List<FixUpTask> filter(String command, int maxResults) {
		Query query = entitymanager.createQuery(
				"select c from FixUpTask c where c.ticker like CONCAT('%',:command,'%') or c.description like CONCAT('%',:command,'%') or c.address like CONCAT('%',:command,'%') or c.maxPrice = :command")
				.setMaxResults(maxResults);
		query.setParameter("command", command);

		List<FixUpTask> fixuptask = query.getResultList();

		return fixuptask;
	}

	public Report findReport(int reportId) {
		Assert.notNull(reportId);
		Assert.isTrue(reportService.exists(reportId));
		Report res = reportService.findOne(reportId);
		Assert.isTrue(res.isFinalMode() == false);
		return res;
	}

	public Collection<HandyWorker> handyWorkersWith10PercentMoreAvgApplicatios() {
		Collection<HandyWorker> res = this.handyWorkerRepository.handyWorkersWith10PercentMoreAvgApplicatios();
		return res;
	}
}
