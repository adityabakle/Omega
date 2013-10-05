package net.ab.dal.nosql.hbase.ops;

@SuppressWarnings("serial")
public class HBaseOperationException extends Exception {
	private String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public HBaseOperationException() {
		super();
	}

	public HBaseOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public HBaseOperationException(String message) {
		super(message);
	}

	public HBaseOperationException(Throwable cause) {
		super(cause);
	}
	
	public HBaseOperationException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		// TODO Auto-generated constructor stub
	}
	
	public HBaseOperationException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		// TODO Auto-generated constructor stub
	}
}
