
package services;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Application;
import domain.CreditCard;
import domain.Customer;
import domain.FixUpTask;
import domain.HandyWorker;
import repositories.ApplicationRepository;
import repositories.HandyWorkerRepository;

@Service
@Transactional
public class ApplicationService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private HandyWorkerRepository handyWorkerRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private CustomerService customerService;

	// Simple CRUD methods ----------------------------------------------------

	public boolean exists(final Integer id) {
		return this.applicationRepository.exists(id);
	}

	public Application addComment(final Application aplication, final String... comments) {
		Assert.notNull(aplication);
		if (aplication.getComments() == null)
			aplication.setComments(new LinkedList<String>());

		final List<String> commments = new LinkedList<String>(aplication.getComments());
		commments.addAll(Arrays.asList(comments));
		aplication.setComments(commments);

		return this.applicationRepository.saveAndFlush(aplication);
	}

	public Application addCreditCard(final Application application, final CreditCard creditCard) {
		Assert.notNull(application);
		if (application.getCreditCard() == null)
			application.setCreditCard(creditCard);
		;

		return this.applicationRepository.saveAndFlush(application);
	}

	

	public List<Application> findAll() {
		return this.applicationRepository.findAll();
	}

	public Application findOne(final Integer id) {
		return this.applicationRepository.findOne(id);
	}

	public void delete(final Application entity) {
		this.applicationRepository.delete(entity);
	}
	
	public Application save(Application entity) {
		return applicationRepository.save(entity);
	}

	public Collection<Application> findApplicationsByCustomer(final Customer customer) {
		Assert.notNull(customer);
		Assert.isTrue(this.customerService.exists(customer.getId()));
		final Collection<Application> res = this.applicationRepository.findByCustomerId(customer.getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Application> findApplicationsByHandyWorker(final HandyWorker handyWorker) {
		Assert.notNull(handyWorker);
		Assert.isTrue(handyWorker.getId() != 0);
		final Collection<Application> res = this.applicationRepository.findByHandyWorkerId(handyWorker.getId());
		Assert.notNull(res);
		return res;
	}

	public Application findAcceptedHandyWorkerApplicationByFixUpTask(final FixUpTask fixUpTask) {
		Assert.notNull(fixUpTask);
		Assert.isTrue(fixUpTask.getId() != 0);
		Application res = applicationRepository.findAcceptedHandyWorkerApplicationByFixUpTaskId(fixUpTask.getId(),
				this.handyWorkerRepository.findByFixUpTaskId(fixUpTask.getId()).getId());
		Assert.isTrue(res.getStatus().equals("ACCEPTED"));
		return res;
	}
}
