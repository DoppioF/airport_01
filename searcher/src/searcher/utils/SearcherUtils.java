package searcher.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import customUtils.classes.CustomStringUtils;
import customUtils.constants.strings.SearcherErrors;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.SearcherException;
import searcher.QueryBuilderEx;
import searcher.dto.DbTableStructure;
import searcher.dto.QueryFromSearcher;

@SuppressWarnings("serial")
public class SearcherUtils {
	private final int IGNORABLE_WORDS_LENGTH = 2;
	private final int MAX_AMOUNT_OF_KEYWORDS_ACCEPTED = 3;
	private final String PRIMARY_KEYWORD_PREFIX = "kw1-";
	private final String SECONDARY_KEYWORD_PREFIX = "kw2-";
	private final SearcherStringParser stringParser = new SearcherStringParser();
	private final QueryBuilder queryBuilder = new QueryBuilder();
	private List<String> foundKeywords = new ArrayList<>();
	private List<String> cleanedInput;
	private String[] recognizedWords;
	
	private Map<String, String> keywords;
	private Map<String, String> secondaryKeywords;
	private String[] keywordsArray;
//	private String[] tableNamesArray;
	private List<String> finalFlow;
	private List<DbTableStructure> tableList;
	
	public List<QueryFromSearcher> getQueriesBasedOnTheseKeywords(final String input, Map<String, String> keywords, List<DbTableStructure> tableList) throws SearcherException {
		initBaseParameters(input, keywords, tableList);
		return queryFromSearcherListFactory(getQueries());
	}
	
	public List<QueryFromSearcher> getQueriesBasedOnTheseKeywords(final String input, Map<String, String> keywords, List<DbTableStructure> tableList, Map<String, String> secondaryKeywords) throws SearcherException {
		initBaseParameters(input, keywords, tableList);
		useTheseSecondaryKeywords(secondaryKeywords);
		return queryFromSearcherListFactory(getQueries());
	}
	
	private List<QueryFromSearcher> queryFromSearcherListFactory(String[] queries) throws SearcherException {
		List<QueryFromSearcher> queryList = new ArrayList<>();
		for (String query : queries) {
			queryList.add(queryFromSearcherFactory(tableList, query));
		}
		return queryList;
	}
	
	private String[] getQueries() throws SearcherException {
		return queryBuilder.buildQueries(finalFlow
				, foundKeywords
				, keywords
				, SECONDARY_KEYWORD_PREFIX
				, PRIMARY_KEYWORD_PREFIX
				, secondaryKeywords
				, tableList);
	}
	
	private void initBaseParameters(final String input, Map<String, String> keywords, List<DbTableStructure> tableList) throws SearcherException {
		this.tableList = tableList;
		this.keywords = keywords;
		this.keywordsArray = keywords.keySet().toArray(new String[0]);
//		this.tableNamesArray = keywords.values().toArray(new String[0]);
		initCleanedInput(input);
		initRecognizedWords(keywordsArray);
	}
	
	private void useTheseSecondaryKeywords(Map<String, String> secondaryKeywords) {
		this.secondaryKeywords = secondaryKeywords;
		completeRecognizedWords(secondaryKeywords.keySet().toArray(new String[0]));
		finalFlow = new ArrayList<>() {
			{
				for (String string : recognizedWords) {
					add(string);
				}
			}
		};
	}
	
	public void initCleanedInput(String input) {
		cleanedInput = new ArrayList<>() {
			{
				for (String string : stringParser.logicSplit(input)) {
					if (string.length() > IGNORABLE_WORDS_LENGTH
						|| CustomStringUtils.isStringParsableToNumber(string)) {
						add(string);
					}
				}
			}
		};
	}
	
	public void initRecognizedWords(String[] keywords) throws SearcherException {
		recognizedWords = new String[cleanedInput.size()];
		checkCorrectWords(keywords);
		checkAmountofKeywords();
		fillRecognizedWordsWithCompatibleWords(keywords);
		checkAmountofKeywords();
	}
	
	public void completeRecognizedWords(String[] secondaryKeywords) {
		fillRecognizedWordsWithSecondaryKeywords(secondaryKeywords);
		fillRecognizedWordWithNonKeywords();
	}
	
	private void checkAmountofKeywords() throws SearcherException {
		if (MAX_AMOUNT_OF_KEYWORDS_ACCEPTED <= foundKeywords.size()) {
			throw new SearcherException(SearcherErrors.TOO_MANY_KEYWORDS);
		}
	}
	
	private void fillRecognizedWordsWithSecondaryKeywords(String[] secondaryKeywords) {
		for (int index = 0; index < recognizedWords.length; index++) {
			if (null == recognizedWords[index]) {
				for (String word : secondaryKeywords) {
//					System.out.println(cleanedInput.get(index) + " " + word);
					if (null != cleanedInput.get(index)
						&& cleanedInput.get(index).startsWith(word)) {
						
						transferSecondaryKeyword(index, word);
					}
				}
			}
		}
	}
	
	private void fillRecognizedWordWithNonKeywords() {
		for (int index = 0; index < recognizedWords.length; index++) {
			if (null == recognizedWords[index]) {
				transferWord(index, cleanedInput.get(index));
			}
		}
	}
	
	private void checkCorrectWords(String[] keywords) {
		for (int index = 0; index < recognizedWords.length; index++) {
			for (String word : keywords) {
				if (word.equals(cleanedInput.get(index))) {
					transferPrimaryKeyword(index, word);
					foundKeywords.add(word);
				}
			}
		}
	}
	
	private void fillRecognizedWordsWithCompatibleWords(String[] keywords) {
		int index = 0;
		for (String word : cleanedInput) {
			if (null != word) {
				String recognizedWord = tryRecognizeWord(word, keywords);
				if (null != recognizedWord) {
					transferPrimaryKeyword(index, recognizedWord);
					foundKeywords.add(recognizedWord);
				}
			}
			index++;
		}
	}
	
	private void transferPrimaryKeyword(int index, String word) {
		transferWord(index, PRIMARY_KEYWORD_PREFIX + word);
	}
	
	private void transferSecondaryKeyword(int index, String word) {
		transferWord(index, SECONDARY_KEYWORD_PREFIX + word);
	}
	
	private void transferWord(int index, String word) {
		recognizedWords[index] = word;
		cleanedInput.set(index, null);
	}
	
	private String tryRecognizeWord(final String word, String[] keywords) {
		for (String keyword : keywords) {
			if (stringParser.isCompatible(word, keyword)) {
				return keyword;
			}
		}
		return null;
	}
	
	private QueryFromSearcher queryFromSearcherFactory(List<DbTableStructure> tableList, String query) throws SearcherException {
		String tableNameOfCurrentQuery = query.split(" ")[3];
		for (DbTableStructure tableStructure : tableList) {
			if (tableNameOfCurrentQuery.equals(tableStructure.getTableName())) {
				QueryFromSearcher queryFromSearcher = new QueryFromSearcher();
				queryFromSearcher.setQuery(query);
				queryFromSearcher.setEntityType(tableStructure.getEntityClass());
				queryFromSearcher.setDtoType(tableStructure.getDtoClass());
				return queryFromSearcher;
			}
		}
		throw new SearcherException(SearcherErrors.UNEXPECTED_ERROR);
	}

//	private List<String> getCleanedInput() {
//		return cleanedInput;
//	}
//
//	private void setCleanedInput(List<String> cleanedInput) {
//		this.cleanedInput = cleanedInput;
//	}
//
//	private String[] getRecognizedWords() {
//		return recognizedWords;
//	}
//
//	private String getPRIMARY_KEYWORD_PREFIX() {
//		return PRIMARY_KEYWORD_PREFIX;
//	}
//
//	private String getSECONDARY_KEYWORD_PREFIX() {
//		return SECONDARY_KEYWORD_PREFIX;
//	}
//
//	private QueryBuilder getInterpreter() {
//		return queryBuilder;
//	}
//
//	private List<String> getFoundKeywords() {
//		return foundKeywords;
//	}
//
//	private void setFoundKeywords(List<String> foundKeywords) {
//		this.foundKeywords = foundKeywords;
//	}
//
//	private int getMAX_AMOUNT_OF_KEYWORDS_ACCEPTED() {
//		return MAX_AMOUNT_OF_KEYWORDS_ACCEPTED;
//	}
//
//	private Map<String, String> getSecondaryKeywords() {
//		return secondaryKeywords;
//	}
//	
//	private void setSecondaryKeywords(Map<String, String> secondaryKeywords) {
//		this.secondaryKeywords = secondaryKeywords;
//	}
//
//	private List<String> getFinalFlow() {
//		return finalFlow;
//	}
//
//	private void setFinalFlow(List<String> finalFlow) {
//		this.finalFlow = finalFlow;
//	}
//
//	private Map<String, String> getKeywords() {
//		return keywords;
//	}
//
//	private void setKeywords(Map<String, String> keywords) {
//		this.keywords = keywords;
//	}
//
//	private String[] getTableNamesArray() {
//		return tableNamesArray;
//	}
//
//	private void setTableNamesArray(String[] tableNamesArray) {
//		this.tableNamesArray = tableNamesArray;
//	}
//
//	private List<DbTableStructure> getTableList() {
//		return tableList;
//	}
//
//	private void setTableList(List<DbTableStructure> tableList) {
//		this.tableList = tableList;
//	}
}
