package searcher.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import customUtils.classes.CustomStringUtils;
import customUtils.constants.strings.SearcherErrors;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.SearcherException;
import searcher.dto.DbTableStructure;
import searcher.dto.QueryFromSearcher;
import searcher.utils.StringParser;

@SuppressWarnings("serial")
public class QueryBuilderController {
	private final int IGNORABLE_WORDS_LENGTH = 2;
	private final int MAX_AMOUNT_OF_KEYWORDS_ACCEPTED = 3;
	private final String PRIMARY_KEYWORD_PREFIX = "kw1-";
	private final String SECONDARY_KEYWORD_PREFIX = "kw2-";
	private final StringParser stringParser = new StringParser();
	private final QueryBuilderLogic interpreter = new QueryBuilderLogic();
	private List<String> foundKeywords = new ArrayList<>();
	private List<String> cleanedInput;
	private String[] recognizedWords;
	
	public void initCleanedInput(String input) {
		setCleanedInput(new ArrayList<>() {
			{
				for (String string : stringParser.logicSplit(input)) {
					if (string.length() > IGNORABLE_WORDS_LENGTH
						|| CustomStringUtils.isStringParsableToNumber(string)) {
						add(string);
					}
				}
			}
		});
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
	
	public List<QueryFromSearcher> buildQuery(List<String> finalFlow
												, Map<String, String> keywords
												, Map<String, String> secondaryKeywords
												, List<DbTableStructure> tableList) throws SearcherException {
		
		List<QueryFromSearcher> queryList = new ArrayList<>();
		String[] queries = interpreter.buildQuery(finalFlow
													, foundKeywords
													, keywords
													, SECONDARY_KEYWORD_PREFIX
													, PRIMARY_KEYWORD_PREFIX
													, secondaryKeywords
													, tableList);
		for (String query : queries) {
			queryList.add(queryFromSearcherFactory(tableList, query));
		}
		return queryList;
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

	public List<String> getCleanedInput() {
		return cleanedInput;
	}

	public void setCleanedInput(List<String> cleanedInput) {
		this.cleanedInput = cleanedInput;
	}

	public String[] getRecognizedWords() {
		return recognizedWords;
	}

	public String getPRIMARY_KEYWORD_PREFIX() {
		return PRIMARY_KEYWORD_PREFIX;
	}

	public String getSECONDARY_KEYWORD_PREFIX() {
		return SECONDARY_KEYWORD_PREFIX;
	}

	public QueryBuilderLogic getInterpreter() {
		return interpreter;
	}

	public List<String> getFoundKeywords() {
		return foundKeywords;
	}

	public void setFoundKeywords(List<String> foundKeywords) {
		this.foundKeywords = foundKeywords;
	}

	public int getMAX_AMOUNT_OF_KEYWORDS_ACCEPTED() {
		return MAX_AMOUNT_OF_KEYWORDS_ACCEPTED;
	}

}
