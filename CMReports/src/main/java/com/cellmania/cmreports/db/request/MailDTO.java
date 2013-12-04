package com.cellmania.cmreports.db.request;

import com.cellmania.cmreports.common.ObjectConvertor;

public class MailDTO {
	private Long mailId;
	private String fromAddress;
	private String fromName;
	private String replyToAddress;
	private String toAddress;
	private String ccAddress;
	private String subject;
	private String body;
	private Boolean includeFileLink;
	private Boolean attachment;
	private Boolean includeDefaultFooter;
	
	public MailDTO(){
		
	}

	public Long getMailId() {
		return mailId;
	}

	public void setMailId(Long mailId) {
		this.mailId = mailId;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getReplyToAddress() {
		return replyToAddress;
	}

	public void setReplyToAddress(String replyToAddress) {
		this.replyToAddress = replyToAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
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

	public Boolean getIncludeFileLink() {
		return includeFileLink;
	}

	public void setIncludeFileLink(Boolean includeFileLink) {
		this.includeFileLink = includeFileLink;
	}

	public Boolean getAttachment() {
		return attachment;
	}

	public void setAttachment(Boolean attachment) {
		this.attachment = attachment;
	}

	public Boolean getIncludeDefaultFooter() {
		return includeDefaultFooter;
	}

	public void setIncludeDefaultFooter(Boolean includeDefaultFooter) {
		this.includeDefaultFooter = includeDefaultFooter;
	}
	
	public String toString(){
		return ObjectConvertor.convertToString(this);
	}
	
}
