package airport_01Rest.config;

import javax.naming.InitialContext;


public class EJBFactory<T> {
	
	private final static String PREFIX = "java:global/airport_01Ear/airport_01Ejb/"; // percorso e nome del progetto con gli EJB

	private Class<T> interfaceClass;
	
	public EJBFactory(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	
	
	@SuppressWarnings("unchecked")
	public T getEJB() throws Exception {
		InitialContext context = new InitialContext(); //punto iniziale per la risoluzione del naming
		return (T) context.lookup(PREFIX + interfaceClass.getSimpleName() + "!" + interfaceClass.getName());
	}
}