package searcher.utils;

public class CustomStringLogicTool {
	private String[] alphabet = new String[] {
		"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
	};
	
	public boolean isCompatible(String toCompare, String keyword) {
		if (isCompatibleWithOneLetterLess(toCompare, keyword)) {
			return true;
		}
		if (isCompatibleWithOneMoreLetter(toCompare, keyword)) {
			return true;
		}
		if (isCompatibleReplacingOneLetter(toCompare, keyword)) {
			return true;
		}
		return false;
	}
	
	private boolean isCompatibleWithOneMoreLetter(String toCompare, String keyword) {
		for (int index = 0; index < toCompare.length(); index++) {
			for (String character : alphabet) {
				if (putACharacterInPositionNInAString(toCompare, index, character)
						.equals(keyword)) {
					return true;
				}
			}
		}
		for (String character : alphabet) {
			if ((toCompare + character).equals(keyword)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isCompatibleWithOneLetterLess(String toCompare, String keyword) {
		for (int index = 0; index < toCompare.length(); index++) {
			if (removeACharacterInPositionNInAString(toCompare, index)
					.equals(keyword)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isCompatibleReplacingOneLetter(String toCompare, String keyword) {
		for (int index = 0; index < toCompare.length(); index++) {
			for (String character : alphabet) {
				if (replaceACharacterInPositionNInAString(toCompare, index, character)
						.equals(keyword)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private String putACharacterInPositionNInAString(String string, int position, String character) {
		String newString = (string.substring(0, position) 
				+ character 
				+ string.substring(position, string.length()));
//		System.out.println(newString);
		return newString;
	}
	
	private String removeACharacterInPositionNInAString(String string, int position) {
		String newString = (string.substring(0, position) 
				+ string.substring(position + 1, string.length()));
//		System.out.println(newString);
		return newString;
	}
	
	private String replaceACharacterInPositionNInAString(String string, int position, String character) {
		String newString = (string.substring(0, position) 
				+ character 
				+ string.substring(position + 1, string.length()));
//		System.out.println(newString);
		return newString;
	}
	
	public String[] getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(String[] alphabet) {
		this.alphabet = alphabet;
	}
}
