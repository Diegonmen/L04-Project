
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Complaint;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

	@Query("select avg(f.complaints.size), min(f.complaints.size), max(f.complaints.size), sqrt(sum(f.complaints.size * f.complaints.size)/count(f.complaints.size) - (avg(f.complaints.size)*avg(f.complaints.size))) from FixUpTask f")
	Double[] computeAvgMinMaxStdvComplaintsPerFixUpTask();
}
