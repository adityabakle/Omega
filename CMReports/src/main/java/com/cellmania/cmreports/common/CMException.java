package com.cellmania.cmreports.common;

@SuppressWarnings("serial")
public class CMException extends Exception {

	private int errCode = 0;
	private String errorCode;
	private String errMsg;
	
	public int getErrCode() {
		return errCode;
	}

	protected void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}


	public CMException() {
		super();
	}
	
	public CMException(int errCode, String message) {
		super();
		this.errCode = errCode;
		this.errMsg = message;
	}
	
	public CMException(String errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.errMsg = message;
	}

	public CMException(String message) {
		super(message);
		this.errCode = -1;
		this.errMsg = message;
	}

	public CMException(Throwable cause) {
		super(cause);
		this.errCode = -1;
		this.errMsg = cause.getLocalizedMessage();
	}

	public CMException(String message, Throwable cause) {
		super(message, cause);
		this.errCode = -1;
		this.errMsg = message;
	}

}
