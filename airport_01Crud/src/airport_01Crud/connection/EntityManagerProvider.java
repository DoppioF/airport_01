package airport_01Crud.connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
/**
 * Classe per la gestione degli {@code EntityManager} e delle {@code EntityTransaction}.
 * 
 * @author Alfonso
 * 
 * @see {@link EntityManagerFactory}
 * @see {@link EntityManager}
 * @see {@link EntityTransaction}
 */
public class EntityManagerProvider {

	/**
	 * Nome del persitence-unit specificato nel file <b>{@code persistence.xml}.</b>
	 * Nella quasi totalità dei casi corrisponde al nome del {@code JPA Project} contenente i models.
	 * <br>
	 */
	private static final String PERSISTENCE_UNIT_NAME = "airport_01Model";
	
	/**
	 * Implementazione dell'interfaccia {@link EntityManagerFactory} ottenuta a partire
	 * dal metodo {@link Persistence#createEntityManagerFactory(String persistenceUnitName)} passando come parametro
	 * la Stringa {@link #PERSISTENCE_UNIT_NAME} 
	 */
	private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	
	/**
	 * Variabile di tipo {@link EntityManager}
	 */
	private static EntityManager entityManager;
	
	/**
	 * Variabile di tipo {@link EntityTransaction}
	 */
	private static EntityTransaction entityTransaction;

	/**
	 * Costruttore privato
	 */
	private EntityManagerProvider() {}

	/**
	 * 
	 * @return Ritorna un'implementazione della interfaccia {@link EntityManager} se quella già esistente è stata chiusa.
	 * 
	 * @see {@link #entityManager}
	 */
	public static EntityManager getEntityManager() {
		if(entityManager == null || !entityManager.isOpen()) {
			entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();			
		}
		return entityManager;
	}

	/**
	 * 	Metodo che ottiene un'implementazione dell'interfaccia {@link EntityTransaction} a partire 
	 * 	dall' {@link #entityManager} (utilizzo il metodo {@link #getEntityManager}
	 * perche in questo punto la variabile potrebbe essere null)
	 *  e richiama il metodo {@code begin()} di quest'ultimo.
	 * <p>
	 * 	{@code EntityTransaction} assegnato ad una variabile (e non referenziato tramite {@code entityManager.getTransaction()})
	 * 	per 2 motivi:
	 * 	<ol>
	 * 		<li>Assicurarmi di eseguire le operazioni nella <b>STESSA ISTANZA</b> dell'implementazione di {@code EntityTransaction}.</li>
	 * 		<li>Assicurarmi di eseguire {@link #rollbackTransaction()} e {@link #commitTransaction()} solo se presente quest'ultima.</li>
	 * 	</ol>
	 * </p>
	 * 
	 * @throws Exception Obbligo a gestire la transazione in un blocco {@code try-catch}
	 */
	public static void beginEntityTransaction() throws Exception {
		entityTransaction = getEntityManager().getTransaction();
		entityTransaction.begin();
	}
	
	/**
	 * Metodo che, se presente un'istanza dell'oggetto {@code EntityTransaction}, richiama il metodo {@code commit()} 
	 * di quest'ultimo e setta l'istanza su null così da poterne ottenere una nuova in seguito.
	 */
	public static void commitEntityTransaction() {
		if(entityTransaction != null ) {
			entityTransaction.commit();
		}
	}
	
	/**
	 * Metodo che, se presente un'istanza dell'oggetto {@code EntityTransaction}, richiama il metodo {@code rollback()} 
	 * di quest'ultimo e setta l'istanza su null così da poterne ottenere una nuova in seguito.
	 */
	public static void rollbackEntityTransaction() {
		if(entityTransaction != null) {
			entityTransaction.rollback();
		}
	}
}