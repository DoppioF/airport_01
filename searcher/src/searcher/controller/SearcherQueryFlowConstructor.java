package searcher.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import customUtils.classes.CustomStringUtils;
import customUtils.constants.strings.SearcherErrors;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.SearcherException;
import searcher.dto.DbTableStructure;

public class SearcherQueryFlowConstructor {
	
	private int indexOfFirstPrimaryKeyword = -1;
	private final String WHERE = " WHERE ";
	private final String AND = " AND ";
	private final String JOIN = " INNER JOIN ";
	private final String SELECT = "SELECT ";
	private final String ALL = "*";
	private final String FROM = " FROM ";
	private final String OR = " OR ";
	private final String ON = " ON ";
	private final String LIKE = " LIKE ";
	private final String GENERIC_QUERY_START = SELECT + ALL + FROM;
	private final int INDEX_OF_TABLE_NAME = 0;
	private final int INDEX_OF_COLUMN_TYPE = 1;
	
	public void initIndexOfFirstPrimaryKeyword(List<String> finalFlow, String prefix) throws SearcherException {
		int index = 0;
		for (String word : finalFlow) {
			if (isAPrimaryKeyword(word, prefix)) {
				setIndexOfFirstPrimaryKeyword(index);
				break;
			}
			index++;
		}
	}
	
	public int indexOfNextSecondaryKeyword(List<String> finalFlow, String prefix, int startingIndex) {
		for (; startingIndex < finalFlow.size(); startingIndex++) {
			if (isASecondaryKeyword(finalFlow.get(startingIndex), prefix)) {
				return startingIndex;
			}
		}
		return -1;
	}
	
	public int indexOfNextNonKeyword(List<String> finalFlow
									, String prefixPrimary
									, String prefixSecondary
									, int startingIndex) throws SearcherException {
		
		for (; startingIndex < finalFlow.size(); startingIndex++) {
			if (!isASecondaryKeyword(finalFlow.get(startingIndex), prefixSecondary)
				&& !isAPrimaryKeyword(finalFlow.get(startingIndex), prefixPrimary)) {
				return startingIndex;
			}
		}
		throw new SearcherException(SearcherErrors.MISSING_INFO);
	}

	public boolean isAPrimaryKeyword(String word, String prefix) {
		return isAKeyword(word, prefix);
	}
	
	public boolean isASecondaryKeyword(String word, String prefix) {
		return isAKeyword(word, prefix);
	}
	
	private boolean isAKeyword(String word, String prefix) {
		return word.startsWith(prefix);
	}
	
	public String[] buildQuery(List<String> finalFlow
								, List<String> foundKeywords
								, Map<String, String> keywords
								, final String prefixSecondary
								, final String prefixPrimary
								, Map<String, String> secondaryKeywords
								, List<DbTableStructure> tableList) throws SearcherException {
		
		StringBuilder builder = new StringBuilder();
		if (0 < foundKeywords.size()) {
			builder.append(GENERIC_QUERY_START + keywords.get(foundKeywords.get(0)));
			int indexOfNextSecondaryKeyword = indexOfNextSecondaryKeyword(finalFlow
																			, prefixSecondary
																			, finalFlow.indexOf(prefixPrimary + foundKeywords.get(0)));
			if (-1 == indexOfNextSecondaryKeyword && 1 < finalFlow.size()) {
				return queryEntireTable(keywords.get(foundKeywords.get(0)), tableList, cleanWordOfFinalFlow(finalFlow.get(1), prefixPrimary, prefixSecondary));
			} else if (-1 == indexOfNextSecondaryKeyword) {
				return new String[] {builder.toString()} ;
			} else {
				return completeQueryAndReturnEntireString(builder
															, indexOfNextSecondaryKeyword
															, finalFlow
															, foundKeywords
															, keywords
															, prefixSecondary
															, prefixPrimary
															, secondaryKeywords
															, tableList);
			}
		} else {
			return chooseRightQuery(finalFlow, tableList, prefixSecondary, secondaryKeywords);
		}
	}
	
	private String cleanWordOfFinalFlow(String word, String prefixPrimary, String prefixSecondary) {
		if (isAPrimaryKeyword(word, prefixPrimary)) {
			return word.substring(prefixPrimary.length());
		} else if (isASecondaryKeyword(word, prefixSecondary)) {
			return word.substring(prefixSecondary.length());
		} else {
			return word;
		}
	}
	
	private String[] queryEntireTable(final String tableName, List<DbTableStructure> tableList, final String value) {
		String[] tableColumnsName = tableList.stream()
										.filter(table -> table.getTableName().equals(tableName))
										.flatMap(table -> table.getColumns().entrySet().stream().map(Entry::getKey))
										.toArray(String[]::new);
		String[] queries = new String[tableColumnsName.length];
		for (int index = 0; index < queries.length; index++) {
			queries[index] = GENERIC_QUERY_START 
							+ tableName 
							+ WHERE 
							+ tableColumnsName[index] 
							+ calculateQuerySignAndPutValue((Class<?>) deduceTableNameAndColumnType(tableList
																									, tableColumnsName[index])[INDEX_OF_COLUMN_TYPE]
															, value);
		}
		return queries;
	}
	
	private String[] completeQueryAndReturnEntireString(StringBuilder builder
														, int indexOfNextSecondaryKeyword
														, List<String> finalFlow
														, List<String> foundKeywords
														, Map<String, String> keywords
														, final String prefixSecondary
														, final String prefixPrimary
														, Map<String, String> secondaryKeywords
														, List<DbTableStructure> tableList) throws SearcherException {
		
		appendJoinIfNecessary(builder
								, foundKeywords
								, keywords
								, tableList
								, finalFlow.get(indexOfNextSecondaryKeyword)
								, prefixSecondary
								, secondaryKeywords);
		appendWhereCondition(builder
							, finalFlow
							, prefixSecondary
							, prefixPrimary
							, secondaryKeywords
							, tableList);
		cleanQuery(builder);
		return new String[] { builder.toString()};
	}
	
	private void appendJoinIfNecessary(StringBuilder builder
										, List<String> foundKeywords
										, Map<String
										, String> keywords
										, List<DbTableStructure> tableList
										, final String keyword
										, final String prefixSecondary
										, Map<String, String> secondaryKeywords) throws SearcherException {
		
		String tableNameOfTheSecondaryKeyword = (String) deduceTableNameAndColumnType(tableList
																						, fromKeywordToColumnName(keyword
																													, prefixSecondary
																													, secondaryKeywords))[INDEX_OF_TABLE_NAME];
		String tableNameOfThePrimaryKeyword = "";
		if (tableNameOfTheSecondaryKeyword.equals((tableNameOfThePrimaryKeyword = keywords.get(foundKeywords.get(0))))) {
			builder.append("");
		} else {
			appendJoins(builder
						, foundKeywords
						, keywords
						, tableList
						, keyword
						, prefixSecondary
						, secondaryKeywords
						, tableNameOfTheSecondaryKeyword
						, tableNameOfThePrimaryKeyword);
		}
	}
	
	private void appendJoins(StringBuilder builder
							, List<String> foundKeywords
							, Map<String, String> keywords
							, List<DbTableStructure> tableList
							, final String keyword
							, final String prefixSecondary
							, Map<String, String> secondaryKeywords
							, String tableNameOfTheSecondaryKeyword
							, String tableNameOfThePrimaryKeyword) throws SearcherException {
		
		builder.append(JOIN);
		DbTableStructure tableStructureOfPrimaryKeyword = findTableStructureByTableName(tableNameOfThePrimaryKeyword, tableList);
		DbTableStructure tableStructureOfSecondaryKeyword = findTableStructureByTableName(tableNameOfTheSecondaryKeyword, tableList);
		String foreignKey = null;
		if (null != (foreignKey = checkIfJustOneJoinIsNecessary(tableStructureOfPrimaryKeyword
																, tableStructureOfSecondaryKeyword
																, tableNameOfThePrimaryKeyword
																, tableNameOfTheSecondaryKeyword
																, keyword
																, builder))) {
			
			appendOnCondition(tableStructureOfPrimaryKeyword
								, tableStructureOfSecondaryKeyword
								, tableNameOfThePrimaryKeyword
								, tableNameOfTheSecondaryKeyword
								, keyword
								, builder
								, foreignKey);
		} else {
			//implementare join multiple
		}
	}
	
	private void buildTwoOnConditionsOrThrowException(DbTableStructure tableStructureOfPrimaryKeyword, DbTableStructure tableStructureOfSecondaryKeyword, final String tableNameOfThePrimaryKeyword, final String tableNameOfTheSecondaryKeyword) {
		
	}
	
	private String checkIfJustOneJoinIsNecessary(DbTableStructure tableStructureOfPrimaryKeyword
												, DbTableStructure tableStructureOfSecondaryKeyword
												, final String tableNameOfThePrimaryKeyword
												, final String tableNameOfTheSecondaryKeyword
												, final String keyword
												, StringBuilder builder) throws SearcherException {
		
		if (null != tableStructureOfPrimaryKeyword.getForeignKeys()) {
			for (String foreignKey : tableStructureOfPrimaryKeyword.getForeignKeys()) {
				if (foreignKey.equals(tableNameOfTheSecondaryKeyword)) {
					return foreignKey;
				}
			}
			return null;
		}
		return null;
	}
	
	private void appendOnCondition(DbTableStructure tableStructureOfPrimaryKeyword
									, DbTableStructure tableStructureOfSecondaryKeyword
									, final String tableNameOfThePrimaryKeyword
									, final String tableNameOfTheSecondaryKeyword
									, final String keyword
									, StringBuilder builder
									, final String foreignKey) {
		
		builder.append(tableNameOfTheSecondaryKeyword
				+ ON
				+ tableNameOfTheSecondaryKeyword
				+ "."
				+ tableStructureOfSecondaryKeyword.getPkPrefix()
				+ tableStructureOfSecondaryKeyword.getPkName()
				+ " = "
				+ tableNameOfThePrimaryKeyword
				+ "."
				+ tableStructureOfPrimaryKeyword.getFkPrefixes()[0]
				+ foreignKey
				);
	}
		
	private DbTableStructure findTableStructureByTableName(final String tableName, List<DbTableStructure> tableList) throws SearcherException {
		for (DbTableStructure tableStructure : tableList) {
			if (tableStructure.getTableName().equals(tableName)) {
				return tableStructure;
			}
		}
		throw new SearcherException(SearcherErrors.UNEXPECTED_ERROR);
	}
	
	private String[] chooseRightQuery(List<String> finalFlow
										, List<DbTableStructure> tableList
										, final String prefixSecondary
										, Map<String, String> secondaryKeywords) {
		
		int indexOfNextSecondaryKeyword = indexOfNextSecondaryKeyword(finalFlow, prefixSecondary, 0);
		if (-1 == indexOfNextSecondaryKeyword) {
			return queryAllDb(tableList, finalFlow.get(0));
		} else if (1 + indexOfNextSecondaryKeyword < finalFlow.size()) {
			return queryOneColumn(tableList
									, fromKeywordToColumnName(finalFlow.get(indexOfNextSecondaryKeyword)
																, prefixSecondary
																, secondaryKeywords)
									, finalFlow.get(1 + indexOfNextSecondaryKeyword));
		} else {
			return queryOneColumn(tableList
									, fromKeywordToColumnName(finalFlow.get(indexOfNextSecondaryKeyword)
																, prefixSecondary
																, secondaryKeywords)
									, null);
		}
	}
	
	private String fromKeywordToColumnName(final String keyword
											, final String prefix
											, Map<String, String> secondaryKeywords) {
		
		return secondaryKeywords.get(keyword.substring(prefix.length()));
	}
	
	private String[] queryOneColumn(List<DbTableStructure> tableList, final String columnName, final String value) {
		Object[] tableNameAndColumnType = deduceTableNameAndColumnType(tableList, columnName);
		return new String[] {
			genericQueryBuilder(ALL
								, (String) tableNameAndColumnType[INDEX_OF_TABLE_NAME]
								, columnName
								, (Class<?>) tableNameAndColumnType[INDEX_OF_COLUMN_TYPE]
								, value)
		};
	}
	
	private Object[] deduceTableNameAndColumnType(List<DbTableStructure> tableList, final String columnName) {
		for (DbTableStructure table : tableList) {
			for (Entry<String, Class<?>> column : table.getColumns().entrySet()) {
				if (column.getKey().equals(columnName)) {
					return new Object[] {
						table.getTableName()
						, column.getValue()
					};
				}
			}
		}
		return null;
	}
	
	private String[] queryAllDb(List<DbTableStructure> tableList, final String value) {
//		ESEMPIO DI ATOMIC INTEGER
//		AtomicInteger numberOfQueries = new AtomicInteger(0);
//		tableList.forEach(table -> {
//			numberOfQueries.addAndGet(table.getColumns().size());
//		});
//		String[] queries = new String[numberOfQueries.get()];
		
		String[] queries = new String[tableList
		                              .stream()
		                              .mapToInt(table -> table.getColumns().size())
		                              .sum()];
		int index = 0;
		for (DbTableStructure table : tableList) {
			for (Entry<String, Class<?>> column : table.getColumns().entrySet()) {
				queries[index] = genericQueryBuilder(ALL
													, table.getTableName()
													, column.getKey()
													, column.getValue()
													, value);
				index++;
			}
		}
		return queries;
	}
	
	private String genericQueryBuilder(final String whatToSelect
										, final String table
										, final String column
										, Class<?> columnType
										, final String value) {
		
		return SELECT 
				+ whatToSelect 
				+ FROM 
				+ table 
				+ (null != value ? this.WHERE + column + calculateQuerySignAndPutValue(columnType, value) : "");
	}
	
	private String calculateQuerySignAndPutValue(Class<?> classType, final String value) {
		if (classType.equals(String.class) 
			|| classType.equals(LocalDateTime.class)
			|| classType.equals(LocalDate.class)
			|| classType.equals(Enum.class)) {
			
			return LIKE 
					+ "'%" 
					+ (CustomStringUtils.isADate(value) 
							? CustomStringUtils.formatADateToDbCompatible(value) 
							: value) 
					+ "%'";
		} else {
			return " = " + value;
		}
	}
	
	private void cleanQuery(StringBuilder builder) {
		String queryToClean = builder.toString();
		String whatToClean = "";
		if (queryToClean.endsWith((whatToClean = WHERE))) {
			deleteFromBuilder(builder, whatToClean);
		} else if (queryToClean.endsWith((whatToClean = AND))) {
			deleteFromBuilder(builder, whatToClean);
		} else if (queryToClean.endsWith((whatToClean = JOIN))) {
			deleteFromBuilder(builder, whatToClean);
		}
	}
	
	private void deleteFromBuilder(StringBuilder builder, String whatToClean) {
		builder.delete(builder.length() - whatToClean.length(), builder.length());
	}
	
	private void appendWhereCondition(StringBuilder builder
										, List<String> finalFlow
										, final String prefixSecondary
										, final String prefixPrimary
										, Map<String, String> secondaryKeywords
										, List<DbTableStructure> tableList) throws SearcherException {
		
		builder.append(WHERE);
		for (int indexFinalFlow = 1 + indexOfFirstPrimaryKeyword; indexFinalFlow < finalFlow.size();) {
			int indexNextSecondaryKeyword = indexOfNextSecondaryKeyword(finalFlow, prefixSecondary, indexFinalFlow);
			if (indexNextSecondaryKeyword != -1) {
				indexFinalFlow = appendPartOfWhereConditionAndReturnNextIndex(builder
																				, indexFinalFlow
																				, indexNextSecondaryKeyword
																				, finalFlow
																				, secondaryKeywords
																				, prefixPrimary
																				, prefixSecondary
																				, tableList);
			} else {
				break;
			}
		}
	}
	
	private int appendPartOfWhereConditionAndReturnNextIndex(StringBuilder builder
															, int indexFinalFlow
															, int indexNextSecondaryKeyword
															, final List<String> finalFlow
															, final Map<String, String> secondaryKeywords
															, final String prefixPrimary
															, final String prefixSecondary
															, List<DbTableStructure> tableList) throws SearcherException {
		
		builder.append(secondaryKeywords.get(finalFlow.get(indexNextSecondaryKeyword)
						.substring(prefixSecondary.length())));
		int indexNextNonKeyword = indexOfNextNonKeyword(finalFlow
														, prefixPrimary
														, prefixSecondary
														, 1 + indexNextSecondaryKeyword);
		builder.append(" " 
					+ calculateQuerySignAndPutValue((Class<?>) deduceTableNameAndColumnType(tableList
																				, fromKeywordToColumnName(finalFlow.get(indexNextSecondaryKeyword)
																											, prefixSecondary
																											, secondaryKeywords))[INDEX_OF_COLUMN_TYPE]
																				, finalFlow.get(indexNextNonKeyword)) 
					+ AND);
		return indexNextNonKeyword + 1;
	}
	
	private boolean isADate(final String string) {
		
		
		
		return false;
	}
	
//	private String getTableName(String keyword) {
//		return keywords.get(keyword);
//	}

	public int getIndexOfFirstPrimaryKeyword() {
		return indexOfFirstPrimaryKeyword;
	}

	public void setIndexOfFirstPrimaryKeyword(int indexOfFirstPrimaryKeyword) {
		this.indexOfFirstPrimaryKeyword = indexOfFirstPrimaryKeyword;
	}
}
