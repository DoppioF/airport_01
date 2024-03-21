package searcher.dto;

public class QueryFromSearcher {

	private String query;
	private Class<?> entityType;
	private Class<?> dtoType;
	
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public Class<?> getEntityType() {
		return entityType;
	}
	public void setEntityType(Class<?> entityType) {
		this.entityType = entityType;
	}
	public Class<?> getDtoType() {
		return dtoType;
	}
	public void setDtoType(Class<?> dtoType) {
		this.dtoType = dtoType;
	}
	
	
}
