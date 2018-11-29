
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	@Query("select a from Actor a where a.userAccount.id = ?1")
	Actor findByUserAccountId(int userAccountId);

	@Query("select a from Actor a where a.suspicious=true")
	Collection<Actor> findSuspiciousActor();
	
	@Query("select c from Actor c where c.userAccount.username = ?1")
	Actor findSelf(String username);
	
	@Query("select c.userAccount.username from Actor c where c.id <> ?1")
	Collection<String> findAllUsername(int adminId);
}
