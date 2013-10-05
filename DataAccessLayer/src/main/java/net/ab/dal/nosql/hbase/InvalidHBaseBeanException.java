package net.ab.dal.nosql.hbase;


@SuppressWarnings("serial")
public class InvalidHBaseBeanException extends Exception {
	private String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public InvalidHBaseBeanException() {
		super();
	}

	public InvalidHBaseBeanException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidHBaseBeanException(String message) {
		super(message);
	}

	public InvalidHBaseBeanException(Throwable cause) {
		super(cause);
	}
	
	public InvalidHBaseBeanException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		// TODO Auto-generated constructor stub
	}
	
	public InvalidHBaseBeanException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		// TODO Auto-generated constructor stub
	}
	
}
