
package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ApplicationRepository;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Customer;

@Service
@Transactional
public class ApplicationService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ApplicationRepository	applicationRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private CustomerService			customerService;


	// Simple CRUD methods ----------------------------------------------------

	public boolean exists(final Integer id) {
		return this.applicationRepository.exists(id);
	}

	public Application save(final Application application) {
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		final UserAccount userAccount = LoginService.getPrincipal();
		final Date currentMoment = new Date(System.currentTimeMillis() - 1);

		if (this.exists(application.getId()) && application.getStatus().equals("PENDING") && userAccount.getAuthorities().contains("CUSTOMER") && this.findByCustomer(this.customerService.findCustomerByApplication(application)).contains(application)) {
			if (application.getApplicationMoment().compareTo(currentMoment) < 0) {
				//update con status rejected
			} else {
				//update con status accepted
			}
		} else {

			Application result;
			result = this.applicationRepository.save(application);

			return result;
		}
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

	public Collection<Application> findByCustomer(final Customer customer) {
		Assert.notNull(customer);
		Assert.isTrue(this.customerService.exists(customer.getId()));
		final Collection<Application> res = this.applicationRepository.findByCustomerId(customer.getId());
		return res;
	}

}
