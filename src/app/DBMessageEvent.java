package app;

import java.util.EventObject;

public class DBMessageEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2156442000443398955L;
	private String message; 

	public DBMessageEvent(Object source,String msg) {
		super(source);
		this.message=msg;		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	

}
