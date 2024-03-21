package models;

public enum DocumentType {

	PDF (".pdf"),
	WORD_2007 (".docx"),
	WORD_2003 (".doc");
	
	private String extension;
	
	DocumentType(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return extension;
	}
	
}
