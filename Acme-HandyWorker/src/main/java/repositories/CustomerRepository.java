
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	@Query("select c from Customer c where c.userAccount.id = ?1")
	Customer findByUserAccountId(int userAccountId);

	@Query("select c from Customer c where c.id = ?1")
	Customer findByCustomerId(int customerId);

	@Query("select c from Customer c join c.fixUpTasks t join t.applications a where a.id= ?1")
	Customer findCustomerByApplicationId(int applicationId);

	@Query("select c from Customer c join c.fixUpTasks f where f.id = ?1")
	Customer findCustomerByFixUpTaskId(int fixUpTaskId);
	@Query("select t from Customer t order by t.complaints.size")
	Collection<Customer> topThreeCustomersInTermsOfComplaints();

}
