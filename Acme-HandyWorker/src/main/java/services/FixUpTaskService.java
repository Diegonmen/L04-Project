
package services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Customer;
import domain.FixUpTask;
import repositories.FixUpTaskRepository;

@Service
@Transactional
public class FixUpTaskService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private FixUpTaskRepository fixUpTaskRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private CustomerService customerService;

	// Simple CRUD methods ----------------------------------------------------

	public FixUpTask save(FixUpTask entity) {
		return fixUpTaskRepository.save(entity);
	}

	public List<FixUpTask> findAll() {
		return fixUpTaskRepository.findAll();
	}

	public FixUpTask findOne(Integer id) {
		return fixUpTaskRepository.findOne(id);
	}

	public void delete(FixUpTask entity) {
		fixUpTaskRepository.delete(entity);
	}

	public boolean exists(final Integer id) {
		return this.fixUpTaskRepository.exists(id);
	}

	// Other business methods

	public Collection<FixUpTask> findFixUpTasksByCustomer(final Customer customer) {
		Assert.notNull(customer);
		Assert.isTrue(this.customerService.exists(customer.getId()));

		Collection<FixUpTask> result;
		result = this.fixUpTaskRepository.findFixUpTasksByCustomer(customer.getId());

		return result;
	}

	public Collection<FixUpTask> findAllFixUpTaskWithAcceptedApplications() {
		Collection<FixUpTask> res;
		res = fixUpTaskRepository.findAllFixUpTaskWithAcceptedApplications();
		Assert.notEmpty(res);
		return res;
	}

	public Collection<Double> findAvgMinMaxStdDvtFixUpTasksPerUser() {
		Collection<Double> res = fixUpTaskRepository.findAvgMinMaxStrDvtFixUpTaskPerUser();
		return res;
	}
	
	public Collection<Double> findAvgMinMaxStrDvtPerFixUpTask() {
		Collection<Double> res = fixUpTaskRepository.findAvgMinMaxStrDvtPerFixUpTask();
		return res;
	}

}
