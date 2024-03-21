package models;

import java.util.List;

public class PdfStructureModel {

	private String documentName;
	private PdfHeader header;
	private List<String[]> body;
	private Object footer;
	
	
	public PdfHeader getHeader() {
		return header;
	}
	public void setHeader(PdfHeader header) {
		this.header = header;
	}
	public List<String[]> getBody() {
		return body;
	}
	public void setBody(List<String[]> body) {
		this.body = body;
	}
	public Object getFooter() {
		return footer;
	}
	public void setFooter(Object footer) {
		this.footer = footer;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	
}
