package net.ab.dal.nosql.hbase.common;

@SuppressWarnings("serial")
public class AWHBaseException extends Exception {
	private String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public AWHBaseException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AWHBaseException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AWHBaseException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public AWHBaseException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		// TODO Auto-generated constructor stub
	}
	
	public AWHBaseException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		// TODO Auto-generated constructor stub
	}

	public AWHBaseException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
