
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

	
	@Query("select re from Report re where re.finalMode=false")
	Collection<Report> findNotFinalModeReports();
	
	@Query("select c from Customer cus join cus.complaints c where cus.userAccount.id=?1")
	Collection<Report> findReportByCustomerUserAccountId(int userAccointId);

}
