/**
 * 
 */
package utils;

import java.util.Date;

import org.springframework.data.annotation.Id;

/**
 * @author Efficenz
 *
 */
public class SessionLogger {
	@Id
	private String id;
	Date sessionStart;
	String sessionState;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the sessionStart
	 */
	public Date getSessionStart() {
		return sessionStart;
	}
	/**
	 * @param sessionStart the sessionStart to set
	 */
	public void setSessionStart(Date sessionStart) {
		this.sessionStart = sessionStart;
	}
	/**
	 * @return the sessionState
	 */
	public String getSessionState() {
		return sessionState;
	}
	/**
	 * @param sessionState the sessionState to set
	 */
	public void setSessionState(String sessionState) {
		this.sessionState = sessionState;
	}
	/**
	 * @param id
	 * @param sessionState
	 */
	public SessionLogger() {
		 
	}
	

}
