
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Complaint;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

	@Query("select avg(f.complaints.size), min(f.complaints.size), max(f.complaints.size), sqrt(sum(f.complaints.size * f.complaints.size)/count(f.complaints.size) - (avg(f.complaints.size)*avg(f.complaints.size))) from FixUpTask f")
	Double[] computeAvgMinMaxStdvComplaintsPerFixUpTask();
	
	@Query("select c.complaints from Customer c where c.id = ?1")
	Collection<Complaint> findComplaintsByCustomer(int id);

	@Query("select p from Complaint p where p.id not in (select o.id from Report c join c.complaints o)")
	Collection<Complaint> findComplaintsNoAsigned();

	@Query("select c from Referee re join re.reports r join r.complaints c where re.id = ?1")
	Collection<Complaint> findComplaintByReferee(int id);
	
	@Query("select c from Referee re join re.reports r join r.complaints c where re.id = ?1 and c.selfAsigned=true")
	Collection<Complaint> findSelfAsignedComplaintsByRefereeId(int refereeId);

}
