package searcher.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.search.SearchException;

import customUtils.classes.CustomStringUtils;
import customUtils.constants.strings.SearcherErrors;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.SearcherException;
import searcher.dto.DbTableStructure;
import searcher.dto.QueriesInfo;

public class QueryBuilder {
	
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
	private final String QUERY_SEPARATOR = " ### ";
	private final int INDEX_OF_TABLE_NAME = 0;
	private final int INDEX_OF_COLUMN_TYPE = 1;
	private final int QUERY_INDEX_OF_FROM = 2;
	private List<String> tablesAlreadyJoined = new ArrayList<>();
	
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
	
	public QueriesInfo buildQueries(List<String> finalFlow
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
				return queriesInfoFactory(calculateQueriesOnePKeywordNoSKeywords(finalFlow
																				, foundKeywords
																				, keywords
																				, prefixSecondary
																				, prefixPrimary
																				, secondaryKeywords
																				, tableList)
											, true);
			} else if (-1 == indexOfNextSecondaryKeyword) {
				return queriesInfoFactory(new String[] {builder.toString()}) ;
			} else {
				return queriesInfoFactory(completeQuery(builder
									, indexOfNextSecondaryKeyword
									, finalFlow
									, foundKeywords
									, keywords
									, prefixSecondary
									, prefixPrimary
									, secondaryKeywords
									, tableList));
			}
		} else {
			return queriesInfoFactory(chooseQuery(finalFlow, tableList, prefixSecondary, secondaryKeywords));
		}
	}
	
	private String[] calculateQueriesOnePKeywordNoSKeywords(List<String> finalFlow
															, List<String> foundKeywords
															, Map<String, String> keywords
															, final String prefixSecondary
															, final String prefixPrimary
															, Map<String, String> secondaryKeywords
															, List<DbTableStructure> tableList) throws SearcherException {
		String cleanedValue = cleanWordOfFinalFlow(finalFlow.get(1), prefixPrimary, prefixSecondary);
		String tableName = keywords.get(foundKeywords.get(0));
		
		//appendo la query base sulla tabella
		StringBuilder builder = queryEntireTable(new StringBuilder()
												, tableName
												, tableList
												, cleanedValue);
		appendSeparator(builder);
		
		//appendo la query joinando con le tabelle madri rispetto a quella tabella
		String oneJoinQuery = appendNewOnAndNewWherePartsToExistingQuery(builder
																		, tableList
																		, cleanedValue
																		, builder.toString().split(" ")
																		, QUERY_INDEX_OF_FROM + 1);
		appendSeparator(builder);
		
		//appendo la query continuando a joinare tabelle finché non arrivo all'ultima madre
		String[] splittedQuery = oneJoinQuery.split(QUERY_SEPARATOR);
		splittedQuery = splittedQuery[splittedQuery.length - 1].split(" ");
		appendQueryWithMultipleJoins(builder, splittedQuery, tableList, cleanedValue);
		
		
		return builder.toString().split(QUERY_SEPARATOR);
	}
	
	private void appendQueryWithMultipleJoins(StringBuilder builder, String[] splittedQuery, List<DbTableStructure> tableList, final String value) throws SearcherException {
		for (int index = 0; index < splittedQuery.length; index++) {
			if (splittedQuery[index].equals("JOIN")) {
				appendNewOnAndNewWherePartsToExistingQuery(builder, tableList, value, splittedQuery, index + 1);
				appendSeparator(builder);
			}
		}
		cleanQuery(builder);
	}
	
	private void appendSeparator(StringBuilder builder) {
		builder.append(QUERY_SEPARATOR);
	}
	
	private String appendNewOnAndNewWherePartsToExistingQuery(StringBuilder builder
															, List<DbTableStructure> tableList
															, final String value
															, String[] splittedQuery
															, int startingIndex) throws SearcherException {
		
		
		List<DbTableStructure> listOfFathersOfATable = getListOfTFathersOfATable(splittedQuery[startingIndex], tableList);
		DbTableStructure currentTable = tableList.stream().filter(table -> table.getTableName().equals(splittedQuery[startingIndex])).findFirst().orElse(null);
		String joinsToAppend = joinsToAppend(listOfFathersOfATable, currentTable);
		
		if (!"".equals(joinsToAppend)) {
			String whereConditionsToAppend = whereConditionsToAppend(listOfFathersOfATable, value);
			appendNewQuery(builder, splittedQuery, joinsToAppend, whereConditionsToAppend, startingIndex);
		}
		return builder.toString();
	}
	
	//fa parte delle istruzioni in caso di pk e no sk
	private void appendNewQuery(StringBuilder builder, String[] splittedQuery, final String joinsToAppend, final String whereToAppend, int startingIndex) throws SearcherException {
		for (int index = 0; index < findIndexOfWhere(splittedQuery); index++) {
			builder.append(splittedQuery[index] + " ");
		}
		deleteFromBuilderEnd(builder, " ");
		builder.append(joinsToAppend + WHERE + whereToAppend);
	}
	
	private int findIndexOfWhere(String[] splittedQuery) throws SearcherException {
		for (int index = 0; index < splittedQuery.length; index++) {
			if (splittedQuery[index].equals("WHERE")) {
				return index;
			}
		}
		throw new SearcherException("WHERE CONDITION NOT FOUND, METHOD findIndexOfWhere");
	}
	
	private String whereConditionsToAppend(List<DbTableStructure> listOfFathersOfATable, final String value) {
		StringBuilder builder = new StringBuilder();
		for (DbTableStructure table : listOfFathersOfATable) {
			appendNextWhereConditions(table, value, builder);
		}
		cleanQuery(builder);
		return builder.toString();
	}
	
	private void appendNextWhereConditions(DbTableStructure table, final String value, StringBuilder builder) {
		for (Entry<String, Class<?>> column : table.getColumns().entrySet()) {
			if (!stringValueOnColumnNumber(column.getValue(), value)) {
				builder.append(table.getTableName()
								+ "."
								+ column.getKey()
								+ calculateQuerySignAndPutValue(column.getValue(), value)
								+ OR);
			}
		}
	}
	
	private void appendNextWhereConditions(DbTableStructure table, List<String> finalFlow, StringBuilder builder) {
		for (Entry<String, Class<?>> column : table.getColumns().entrySet()) {
			for (String value : finalFlow) {
				if (!stringValueOnColumnNumber(column.getValue(), value)) {
					builder.append(table.getTableName()
									+ "."
									+ column.getKey()
									+ calculateQuerySignAndPutValue(column.getValue(), value)
									+ OR);
				}
			}
		}
	}
	
	private boolean stringValueOnColumnNumber(Class<?> columnClass, final String value) {
		return isNumericClass(columnClass) 
				&& !CustomStringUtils.isStringParsableToNumber(value); 
	}
	
	//TODO: smembrare questo metodo
	private String joinsToAppend(List<DbTableStructure> listOfFathersOfATable, DbTableStructure joiningTable) {
		StringBuilder builder = new StringBuilder();
		for (DbTableStructure table : listOfFathersOfATable) {
			if (null != joiningTable.getForeignKeys()) {
				for (String foreignKey : joiningTable.getForeignKeys()) {
					if (!tablesAlreadyJoined.contains(table.getTableName())
						&& foreignKey.equals(table.getTableName())) {
						builder.append(JOIN + table.getTableName() + ON);
						for (String foreignKeyPrefix : joiningTable.getFkPrefixes()) {
							builder.append(joiningTable.getTableName()
											+ "."
											+ foreignKeyPrefix
											+ foreignKey
											+ " = "
											+ table.getTableName()
											+ "."
											+ table.getPkPrefix()
											+ table.getPkName()
											+ OR);
						}
						cleanQuery(builder);
						tablesAlreadyJoined.add(table.getTableName());
					}
				}
			}
		}
		return builder.toString();
	}
	
	private List<DbTableStructure> getListOfTFathersOfATable(final String tableName, List<DbTableStructure> tableList) throws SearcherException {
		List<DbTableStructure> fatherList = new ArrayList<>();
		List<String> foreignKeys = getTableStructureByTableName(tableName, tableList).getForeignKeys();
		if (null != foreignKeys) {
			for (String foreignKey : foreignKeys) {
				fatherList.add(getTableStructureByTableName(foreignKey, tableList));
			}
		}
		return fatherList;
	}
	
	private DbTableStructure getTableStructureByTableName(final String tableName, List<DbTableStructure> tableList) throws SearcherException {
		for (DbTableStructure table : tableList) {
			if (tableName.equals(table.getTableName())) {
				return table;
			}
		}
		throw new SearcherException("TABLE " + tableName + " NOT FOUND");
	}
	
	private String cleanWordOfFinalFlow(String word, final String prefixPrimary, final String prefixSecondary) {
		if (isAPrimaryKeyword(word, prefixPrimary)) {
			return word.substring(prefixPrimary.length());
		} else if (isASecondaryKeyword(word, prefixSecondary)) {
			return word.substring(prefixSecondary.length());
		} else {
			return word;
		}
	}
	
	private StringBuilder queryEntireTable(StringBuilder builder, final String tableName, List<DbTableStructure> tableList, final String value) {
		String[] tableColumnsName = getColumnsNameOfATable(tableName, tableList);
//		String[] queries = new String[tableColumnsName.length];
		
		builder.append(GENERIC_QUERY_START + tableName + WHERE);
		for (int index = 0; index < tableColumnsName.length; index++) {
			Class<?> currentColumnType = (Class<?>) deduceTableNameAndColumnType(tableList, tableColumnsName[index])[INDEX_OF_COLUMN_TYPE];
			if (!stringValueOnColumnNumber(currentColumnType, value)) {
				builder.append(tableName
								+ "."
								+ tableColumnsName[index] 
								+ calculateQuerySignAndPutValue(currentColumnType, value)
								+ OR);
			}
		}
		cleanQuery(builder);
		return builder;
	}
	
	private String[] getColumnsNameOfATable(final String tableName, List<DbTableStructure> tableList) {
		return tableList.stream()
				.filter(table -> table.getTableName().equals(tableName))
				.flatMap(table -> table.getColumns().entrySet().stream().map(Entry::getKey))
				.toArray(String[]::new);
	}
	
	private String[] completeQuery(StringBuilder builder
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
		return manageExceptionFkAndGetQuery(builder.toString());
	}
	
	private String[] manageExceptionFkAndGetQuery(String currentQuery) {
		if (currentQuery.contains("flight_route.id_departure_airport")) {
			return new String[] {
				currentQuery.replace("_airport", "_airport OR airport.id = flight_route.id_arrival_airport")
			};
		} else {
			return new String[] { currentQuery };
		}
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
	
	private String[] chooseQuery(List<String> finalFlow
								, List<DbTableStructure> tableList
								, final String prefixSecondary
								, Map<String, String> secondaryKeywords) {
		
		int indexOfNextSecondaryKeyword = indexOfNextSecondaryKeyword(finalFlow, prefixSecondary, 0);
		if (-1 == indexOfNextSecondaryKeyword) {
			return queryAllDb(tableList, finalFlow);
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
	
	private String[] queryAllDb(List<DbTableStructure> tableList, List<String> finalFlow) {
//		ESEMPIO DI ATOMIC INTEGER
//		AtomicInteger numberOfQueries = new AtomicInteger(0);
//		tableList.forEach(table -> {
//			numberOfQueries.addAndGet(table.getColumns().size());
//		});
//		String[] queries = new String[numberOfQueries.get()];
		
//		String[] queries = new String[tableList
//		                              .stream()
//		                              .mapToInt(table -> table.getColumns().size())
//		                              .sum()];
//		int index = 0;
		StringBuilder builder = new StringBuilder();
		for (DbTableStructure table : tableList) {
			builder.append(GENERIC_QUERY_START + table.getTableName() + WHERE);
			appendNextWhereConditions(table, finalFlow, builder);
//			for (Entry<String, Class<?>> column : table.getColumns().entrySet()) {
//				queries[index] = genericQueryBuilder(ALL
//													, table.getTableName()
//													, column.getKey()
//													, column.getValue()
//													, value);
//				index++;
//			}
			if (builder.toString().endsWith(WHERE)) {
				deleteFromBuilderEnd(builder, GENERIC_QUERY_START + table.getTableName() + WHERE); 
			} else {
				cleanQuery(builder);
				appendSeparator(builder);
			}
		}
		return builder.toString().split(QUERY_SEPARATOR);
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
			deleteFromBuilderEnd(builder, whatToClean);
		} else if (queryToClean.endsWith((whatToClean = AND))) {
			deleteFromBuilderEnd(builder, whatToClean);
		} else if (queryToClean.endsWith((whatToClean = JOIN))) {
			deleteFromBuilderEnd(builder, whatToClean);
		} else if (queryToClean.endsWith((whatToClean = OR))) {
			deleteFromBuilderEnd(builder, whatToClean);
		} else if (queryToClean.endsWith((whatToClean = QUERY_SEPARATOR))) {
			deleteFromBuilderEnd(builder, whatToClean);
		}
	}
	
	private void deleteFromBuilderEnd(StringBuilder builder, String whatToClean) {
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
				indexFinalFlow = appendPartOfWhereCondition(builder
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
	
	private int appendPartOfWhereCondition(StringBuilder builder
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
		builder.append(calculateQuerySignAndPutValue((Class<?>) deduceTableNameAndColumnType(tableList
																							, fromKeywordToColumnName(finalFlow.get(indexNextSecondaryKeyword)
																														, prefixSecondary
																														, secondaryKeywords))[INDEX_OF_COLUMN_TYPE]
																							, finalFlow.get(indexNextNonKeyword)) 
					+ AND);
		return indexNextNonKeyword + 1;
	}
	
//	private String getTableName(String keyword) {
//		return keywords.get(keyword);
//	}
	
	//TODO: questo metodo devo spostarlo nelle custom utils
	private boolean isNumericClass(Class<?> classType) {
		return Integer.class.equals(classType)
				|| Float.class.equals(classType)
				|| Byte.class.equals(classType)
				|| Long.class.equals(classType)
				|| Double.class.equals(classType);
	}
	
	private QueriesInfo queriesInfoFactory(String[] queries) {
		QueriesInfo queriesInfo = new QueriesInfo();
		queriesInfo.setQueries(queries);
		return queriesInfo;
	}
	
	private QueriesInfo queriesInfoFactory(String[] queries, boolean executeOnlyOneQuery) {
		QueriesInfo queriesInfo = queriesInfoFactory(queries);
		queriesInfo.setExecuteOnlyOneQuery(executeOnlyOneQuery);
		return queriesInfo;
	}

	public int getIndexOfFirstPrimaryKeyword() {
		return indexOfFirstPrimaryKeyword;
	}

	public void setIndexOfFirstPrimaryKeyword(int indexOfFirstPrimaryKeyword) {
		this.indexOfFirstPrimaryKeyword = indexOfFirstPrimaryKeyword;
	}
}
