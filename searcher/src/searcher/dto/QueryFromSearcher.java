package searcher.dto;

public class QueryFromSearcher {

	private String query;
	private Class<?> entityType;
	private Class<?> dtoType;
	private Boolean executeOnlyOneQuery;
	
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
	public Boolean getExecuteOnlyOneQuery() {
		return executeOnlyOneQuery;
	}
	public void setExecuteOnlyOneQuery(Boolean executeOnlyOneQuery) {
		this.executeOnlyOneQuery = executeOnlyOneQuery;
	}
	@Override
	public String toString() {
		return "QueryFromSearcher [query=" + query + 
				", entityType=" + (null == entityType ? "null" : entityType.getCanonicalName()) + 
				", dtoType=" + (null == dtoType ? "null" : dtoType.getCanonicalName()) +
				", executeOnlyOneQuery=" + executeOnlyOneQuery + "]";
	}
}
