package models;

public class Attachment {

	private String name;
	private DocumentType documentType;
	private byte[] content;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public DocumentType getDocumentType() {
		return documentType;
	}
	
	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}
}