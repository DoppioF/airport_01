package model;

import models.Attachment;

public class EmailStructure {

	private String sender;
	private String[] recipients;
	private String subject;
	private String body;
	private String[] attachPaths;
	private Attachment[] attachments;
	
	public String getSender() {
		return sender;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String[] getRecipients() {
		return recipients;
	}
	
	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String[] getAttachPaths() {
		return attachPaths;
	}
	
	public void setAttachPaths(String[] attachPaths) {
		this.attachPaths = attachPaths;
	}

	public Attachment[] getAttachments() {
		return attachments;
	}

	public void setAttachments(Attachment[] attachments) {
		this.attachments = attachments;
	}
}
