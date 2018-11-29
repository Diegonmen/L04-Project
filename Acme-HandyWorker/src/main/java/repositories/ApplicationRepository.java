
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
	
	@Query("select avg(a.applications.size), min(a.applications.size), max(a.applications.size), sqrt(sum(a.applications.size * a.applications.size)/count(a.applications.size) - (avg(a.applications.size)*avg(a.applications.size))) from FixUpTask a")
	Collection<Double> findAvgMinMaxStrDvtApplicationPerFixUpTask();
	
	@Query("select avg(a.offeredPrice), min(a.offeredPrice), max(a.offeredPrice), sqrt(sum(a.offeredPrice * a.offeredPrice)/count(a.offeredPrice) - (avg(a.offeredPrice)*avg(a.offeredPrice))) from Application a")
	Collection<Double> findAvgMinMaxStrDvtPerApplication();
	
	@Query("select count(a)*1.0 /(select count(ap) from Application ap) from Application a where a.status = 'PENDING'")
	Double ratioOfPendingApplications();
	
	@Query("select count(a)*1.0/(select count(ap) from Application ap) from Application a where a.status = 'ACCEPTED'")
	Double ratioOfAcceptedApplications();
	
	@Query("select count(a)*1.0/(select count(ap) from Application ap) from Application a where a.status = 'REJECTED'")
	Double ratioOfRejectedApplications();
	
	@Query("select count(a)*1.0 /(select count(ap) from Application ap) from FixUpTask f join f.applications a where a.status = 'PENDING' and f.startDate<CURRENT_DATE)")
	Double ratioOfRejectedApplicationsCantChange();
}
