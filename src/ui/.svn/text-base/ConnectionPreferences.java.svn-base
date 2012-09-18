package ui;

import java.io.Serializable;

/**
 * A helper for connection preferences for ConsumerUI
 * @author mayank
 *
 */
public class ConnectionPreferences implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8439631079696761855L;
	private String port;
	private String address;
	
	/**
	 * Constructs the ConnectionPreferences object using arguments.
	 * @param p
	 * @param a
	 */
	public ConnectionPreferences(String a, String p){
		this.address = a;
		this.port = p;
	}
	
	/**
	 * Gets the port
	 * @return port
	 */
	public String getPort(){
		return this.port;
	}
		
	/**
	 * Gets the address
	 * @return address
	 */
	public String getAddress(){
		return this.address;
	}
	
	public String toString(){
		return "Address: " + this.address + " Port: " + this.port;
	}

}
