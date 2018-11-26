
package services;

import java.util.Collection;

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

	public Collection<FixUpTask> findAll() {
		Collection<FixUpTask> result;

		result = fixUpTaskRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public FixUpTask findOne(final int fixUpTaskId) {
		Assert.isTrue(fixUpTaskId != 0);

		FixUpTask result;

		result = fixUpTaskRepository.findOne(fixUpTaskId);
		Assert.notNull(result);

		return result;
	}

	public FixUpTask save(final FixUpTask fixUpTask) {
		Assert.notNull(fixUpTask);
		Assert.isTrue(fixUpTask.getId() != 0);

		FixUpTask result;

		result = fixUpTaskRepository.save(fixUpTask);

		return result;
	}

	public void delete(final FixUpTask fixUpTask) {
		Assert.notNull(fixUpTask);
		Assert.isTrue(fixUpTask.getId() != 0);
		Assert.isTrue(fixUpTaskRepository.exists(fixUpTask.getId()));

		fixUpTaskRepository.delete(fixUpTask);
	}

	public boolean exists(Integer id) {
		return fixUpTaskRepository.exists(id);
	}
	
	//Other business methods
	
		public Collection<FixUpTask> findByCustomer(Customer customer) {
			Assert.notNull(customer);
			Assert.isTrue(customerService.exists(customer.getId()));
			
			Collection<FixUpTask> result;
			result = this.fixUpTaskRepository.findFixUpTasksByCustomer(customer.getId());
			
			return result;
		}

}
