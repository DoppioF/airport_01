package customUtils.logicTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.SearcherException;

public class Searcher {

	private SearcherController searcherController;
	private Map<String, String> keywords;
	private Map<String, String> secondaryKeywords;
	private String[] keywordsArray;
	private String[] tableNamesArray;
	private List<String> finalFlow;
	private List<DbTableStructure> tableList;
	
	public Searcher(final String input, Map<String, String> keywords, List<DbTableStructure> tableList) throws SearcherException {
		searcherController = new SearcherController();
		this.tableList = tableList;
		this.keywords = keywords;
		keywordsArray = keywords.keySet().toArray(new String[0]);
		setTableNamesArray(keywords.values().toArray(new String[0]));
		searcherController.initCleanedInput(input);
		searcherController.initRecognizedWords(keywordsArray);
		
	}

	@SuppressWarnings("serial")
	public Searcher withTheseSecondaryKeywords(Map<String, String> secondaryKeywords) {
		this.secondaryKeywords = secondaryKeywords;
		searcherController.completeRecognizedWords(secondaryKeywords.keySet().toArray(new String[0]));
		setFinalFlow(new ArrayList<>() {
			{
				for (String string : searcherController.getRecognizedWords()) {
					add(string);
				}
			}
		});
		return this;
	}

	public Map<String, String> getSecondaryKeywords() {
		return secondaryKeywords;
	}

	public void setSecondaryKeywords(Map<String, String> secondaryKeywords) {
		this.secondaryKeywords = secondaryKeywords;
	}
	
	public List<QueryFromSearcher> buildQuery() throws SearcherException {
		return searcherController.buildQuery(finalFlow, keywords, secondaryKeywords, tableList);
	}
	

	public SearcherController getSearcherController() {
		return searcherController;
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
