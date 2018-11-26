
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import security.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

}
