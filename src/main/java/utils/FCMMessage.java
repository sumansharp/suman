/**
 * 
 */
package utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Efficenz
 *
 */
public class FCMMessage {
	String to;
	ArrayList<String> registration_ids;
	//HashMap<String,Body> notification;
	HashMap<String,Object> data;
	
	
	/**
	 * 
	 */
	public FCMMessage() {
	}
	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}
	
	public byte[] getBytes(String string) {
		// TODO Auto-generated method stub
		return null;
	}
		
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FCMMessage [to=" + to + ", data=" + data + "]";
	}
	/**
	 * @return the data
	 */
	public HashMap<String, Object> getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}
	public ArrayList<String> getRegistration_ids() {
		return registration_ids;
	}
	public void setRegistration_ids(ArrayList<String> registration_ids) {
		this.registration_ids = registration_ids;
	}
	
	
	
	
	
}
