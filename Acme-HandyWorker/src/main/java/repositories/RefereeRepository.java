
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Referee;

@Repository
public interface RefereeRepository extends JpaRepository<Referee, Integer> {

	@Query("select c from Referee c where c.userAccount.id = ?1")
	Referee findByUserAccountId(int userAccountId);
	
	@Query("select r from Referee r join r.reports re where re.id=?1")
	Referee findRefereeByReportId(int reportId);
}
