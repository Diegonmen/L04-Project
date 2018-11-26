
package utilities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.spi.PersistenceProvider;

import org.hibernate.jpa.HibernatePersistenceProvider;

import utilities.internal.ConsoleReader;
import utilities.internal.SchemaPrinter;

@SuppressWarnings("unchecked")
public class FixUpTaskSearcher {

	public static void main(final String[] args) throws Throwable {
		final PersistenceProvider persistenceProvider = new HibernatePersistenceProvider();
		final EntityManagerFactory entityManagerFactory = persistenceProvider.createEntityManagerFactory(DatabaseConfig.PersistenceUnit, null);
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.setFlushMode(FlushModeType.AUTO);

		final ConsoleReader reader = new ConsoleReader();

		String command;

		System.out.println("Enter the FixUpTask parameter: ");
		while ((command = reader.readCommand()) != null)
			try {
				final Query query = entityManager.createQuery("select c from FixUpTask c where c.ticker like CONCAT('%',:command,'%') or c.description like CONCAT('%',:command,'%') or c.address like CONCAT('%',:command,'%') or c.maxPrice = :command");
				query.setParameter("command", command);
				final List<Object> result = query.getResultList();

				if (result.isEmpty())
					System.out.println("0 objects found");
				else
					SchemaPrinter.print(result);
			} catch (final Exception e) {
				e.printStackTrace();
				continue;
			} finally {
				System.out.println("Enter the FixUpTask parameter: ");
			}
	}
}
