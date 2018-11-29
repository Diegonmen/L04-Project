
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.FixUpTask;

@Repository
public interface FixUpTaskRepository extends JpaRepository<FixUpTask, Integer> {

	@Query("select a from FixUpTask a where a.id = ?1")
	FixUpTask findByFixUpTaskId(int fixUpTaskId);

	@Query("select c.fixUpTasks from Customer c where c.id = ?1")
	Collection<FixUpTask> findFixUpTasksByCustomer(int customerId);

	@Query("select (count(f) * 1.0 / (select count(fi) from FixUpTask fi)) from FixUpTask f where f.complaints.size!=0")
	Double ratioFixUpTasksWithComplaints();

	@Query("select f from FixUpTask f join f.applications a where a.status = 'ACCEPTED'")
	Collection<FixUpTask> findAllFixUpTaskWithAcceptedApplications();
	
	@Query("select avg(c.fixUpTasks.size), min(c.fixUpTasks.size), max(c.fixUpTasks.size), sqrt(sum(c.fixUpTasks.size * c.fixUpTasks.size)/count(c.fixUpTasks.size) - (avg(c.fixUpTasks.size)*avg(c.fixUpTasks.size))) from Customer c")
	Collection<Double> findFixUpTaskAvgMinMaxStrDvt();
}
