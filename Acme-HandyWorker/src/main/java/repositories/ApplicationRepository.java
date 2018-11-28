
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

	@Query("select t.applications from Customer c join c.fixUpTasks t where c.id= ?1")
	Collection<Application> findByCustomerId(int customerId);
	
	@Query("select h.applications from HandyWorker h where h.id = ?1")
	Collection<Application> findByHandyWorkerId(int handyWorkerId);
	
	@Query("select a from Application a join a.fixUpTask f where f.id= ?1 and a.handyWorker.id = ?2 and a.status = 'ACCEPTED'")
	Application findAcceptedHandyWorkerApplicationByFixUpTaskId(int fixUpTaskId, int handyWorkerId);
	
	
}
