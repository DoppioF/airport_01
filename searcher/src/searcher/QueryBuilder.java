package searcher;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.SearcherException;
import searcher.controller.QueryBuilderController;
import searcher.dto.DbTableStructure;
import searcher.dto.QueryFromSearcher;

public class QueryBuilder {

	private QueryBuilderController queryBuilderController;
	private Map<String, String> keywords;
	private Map<String, String> secondaryKeywords;
	private String[] keywordsArray;
	private String[] tableNamesArray;
	private List<String> finalFlow;
	private List<DbTableStructure> tableList;
	
	public QueryBuilder(final String input, Map<String, String> keywords, List<DbTableStructure> tableList) throws SearcherException {
		init(input, keywords, tableList);
	}
	
	private void init(final String input, Map<String, String> keywords, List<DbTableStructure> tableList) throws SearcherException {
		this.queryBuilderController = new QueryBuilderController();
		this.tableList = tableList;
		this.keywords = keywords;
		this.keywordsArray = keywords.keySet().toArray(new String[0]);
		this.tableNamesArray = keywords.values().toArray(new String[0]);
		queryBuilderController.initCleanedInput(input);
		queryBuilderController.initRecognizedWords(keywordsArray);
	}

	@SuppressWarnings("serial")
	public QueryBuilder withTheseSecondaryKeywords(Map<String, String> secondaryKeywords) {
		this.secondaryKeywords = secondaryKeywords;
		queryBuilderController.completeRecognizedWords(secondaryKeywords.keySet().toArray(new String[0]));
		setFinalFlow(new ArrayList<>() {
			{
				for (String string : queryBuilderController.getRecognizedWords()) {
					add(string);
				}
			}
		});
		return this;
	}

	public List<QueryFromSearcher> buildQuery() throws SearcherException {
		return queryBuilderController.buildQuery(finalFlow, keywords, secondaryKeywords, tableList);
	}
	
	public Map<String, String> getSecondaryKeywords() {
		return secondaryKeywords;
	}
	
	public void setSecondaryKeywords(Map<String, String> secondaryKeywords) {
		this.secondaryKeywords = secondaryKeywords;
	}

	public QueryBuilderController getSearcherController() {
		return queryBuilderController;
	}

	public List<String> getFinalFlow() {
		return finalFlow;
	}

	public void setFinalFlow(List<String> finalFlow) {
		this.finalFlow = finalFlow;
	}

	public Map<String, String> getKeywords() {
		return keywords;
	}

	public void setKeywords(Map<String, String> keywords) {
		this.keywords = keywords;
	}

	public String[] getTableNamesArray() {
		return tableNamesArray;
	}

	public void setTableNamesArray(String[] tableNamesArray) {
		this.tableNamesArray = tableNamesArray;
	}

	public List<DbTableStructure> getTableList() {
		return tableList;
	}

	public void setTableList(List<DbTableStructure> tableList) {
		this.tableList = tableList;
	}


}
