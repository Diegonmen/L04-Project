
package services;

import java.util.Collection;
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
import repositories.FixUpTaskRepository;

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

	@PersistenceContext
	EntityManager entitymanager;

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

	public FixUpTask addPhases(FixUpTask fixUpTask) {
		
		return fixUpTask;
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
	
	public Collection<FixUpTask> findAllFixUpTaskWithAcceptedApplications(){
		Collection<FixUpTask> res;
		res = fixUpTaskRepository.findAllFixUpTaskWithAcceptedApplications();
		Assert.notEmpty(res);
		return res;
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
