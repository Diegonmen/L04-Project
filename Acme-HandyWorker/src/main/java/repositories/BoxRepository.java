
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Box;

@Repository
public interface BoxRepository extends JpaRepository<Box, Integer> {

	@Query("select b from Actor a join a.boxes b where a.id = ?1 and b.name='OUTBOX'")
	Box getOutBoxFolderFromActorId(int id);

	@Query("select b from Actor a join a.boxes b where a.id = ?1 and b.name='INBOX'")
	Box getInBoxFolderFromActorId(int id);

	@Query("select b from Actor a join a.boxes b where a.id = ?1 and b.name='SPAMBOX'")
	Box getSpamBoxFolderFromActorId(int id);

	@Query("select b from Actor a join a.boxes b where a.id = ?1 and b.name='TRASHBOX'")
	Box getTrashBoxFolderFromActorId(int id);

	@Query("select b from Box b join b.messages m where m.id=?1")
	Box getFolderFromMessageId(int messageId);

	@Query("select b from Actor a join a.boxes b where a.userAccount.id = ?1")
	Collection<Box> findBoxesByUserAccountId(int userAccountId);

}
