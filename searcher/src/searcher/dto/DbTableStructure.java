package searcher.dto;

import java.util.List;
import java.util.Map;

public class DbTableStructure {
	
	private String tableName;
	private Map<String, Class<?>> columns;
	private Class<?> entityClass;
	private Class<?> dtoClass;
	private String pkPrefix;
	private String pkName;
	private String[] fkPrefixes;
	private List<String> foreignKeys;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Map<String, Class<?>> getColumns() {
		return columns;
	}
	public void setColumns(Map<String, Class<?>> columns) {
		this.columns = columns;
	}
	public Class<?> getEntityClass() {
		return entityClass;
	}
	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}
	public Class<?> getDtoClass() {
		return dtoClass;
	}
	public void setDtoClass(Class<?> dtoClass) {
		this.dtoClass = dtoClass;
	}
	public String[] getFkPrefixes() {
		return fkPrefixes;
	}
	public void setFkPrefixes(String[] fkPrefixes) {
		this.fkPrefixes = fkPrefixes;
	}
	public List<String> getForeignKeys() {
		return foreignKeys;
	}
	public void setForeignKeys(List<String> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}
	public String getPkPrefix() {
		return pkPrefix;
	}
	public void setPkPrefix(String pkPrefix) {
		this.pkPrefix = pkPrefix;
	}
	public String getPkName() {
		return pkName;
	}
	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

}
