
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Complaint;
import domain.Report;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

	@Query("select avg(f.complaints.size), min(f.complaints.size), max(f.complaints.size), sqrt(sum(f.complaints.size * f.complaints.size)/count(f.complaints.size) - (avg(f.complaints.size)*avg(f.complaints.size))) from FixUpTask f")
	Double[] computeAvgMinMaxStdvComplaintsPerFixUpTask();
	
	@Query("select c.complaints from Customer c where c.id = ?1")
	Collection<Complaint> findComplaintsByCustomer(int id);

//	@Query("select c from Complaint c join (select r from Report r) where r.complaints !contains c")
//	Report findReportWithComplaintsNoAsigned();

	@Query("select c from Referee re join re.reports r join r.complaints c where re.id = ?1")
	Collection<Complaint> findComplaintByReferee(int id);
}
