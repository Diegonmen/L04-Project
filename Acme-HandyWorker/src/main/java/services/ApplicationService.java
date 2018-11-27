
package services;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ApplicationRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.CreditCard;
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

	public Application save(final Application application) {
		final Application result, saved;
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		final UserAccount userAccount = LoginService.getPrincipal();
		final Date currentMoment = new Date(System.currentTimeMillis() - 1);
		final Authority authority;
		final UserAccount logedUserAccount;
		authority = new Authority();
		authority.setAuthority("CUSTOMER");

		if (this.exists(application.getId()) && application.getStatus().equals("PENDING") && userAccount.getAuthorities().contains("CUSTOMER")
			&& this.findApplicationsByCustomer(this.customerService.findCustomerByApplication(application)).contains(application)) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "customer.notLogged ");
			Assert.isTrue(logedUserAccount.equals(this.customerService.findCustomerByApplication(application).getUserAccount()), "customer.notEqual.userAccount");
			if (application.getApplicationMoment().compareTo(currentMoment) < 0) {
				//TODO update con status rejected y userAccount
				saved = this.applicationRepository.findOne(application.getId());
				Assert.notNull(saved, "application.not.null");
				final Application application2 = this.addComment(application, "Comentario");
				application2.setStatus("REJECTED");
				result = this.applicationRepository.save(application2);
				return result;
			} else {
				//TODO update con status accepted y userAccount
				saved = this.applicationRepository.findOne(application.getId());
				Assert.notNull(saved, "application.not.null");
				final CreditCard creditCard = new CreditCard();
				creditCard.setBrandName("VISA");
				creditCard.setCVV(123);
				creditCard.setExpirationMonth(12);
				creditCard.setExpirationYear(2020);
				creditCard.setHolderName("Paco Asencio");
				creditCard.setNumber("1234567812345678");
				final Application application2 = this.addCreditCard(application, creditCard);
				final Application application3 = this.addComment(application2, "Comentario");
				application3.setStatus("ACCEPTED");
				result = this.applicationRepository.save(application3);
				return result;
			}
		} else {

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

	public Collection<Application> findApplicationsByCustomer(final Customer customer) {
		Assert.notNull(customer);
		Assert.isTrue(this.customerService.exists(customer.getId()));
		final Collection<Application> res = this.applicationRepository.findByCustomerId(customer.getId());
		return res;
	}

}
