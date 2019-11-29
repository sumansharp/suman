package utils;

public class responseToPatient {

	String result;
	boolean wait;
	String sessionId;
	String token;
	String appointmentId;
	
	
	/**
	 * @return the appointmentId
	 */
	public String getAppointmentId() {
		return appointmentId;
	}
	/**
	 * @param appointmentId the appointmentId to set
	 */
	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}
	/**
	 * 
	 */
	public responseToPatient() {
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the wait
	 */
	public boolean isWait() {
		return wait;
	}
	/**
	 * @param wait the wait to set
	 */
	public void setWait(boolean wait) {
		this.wait = wait;
	}
	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "responseToPatient [result=" + result + ", wait=" + wait + ", sessionId=" + sessionId + ", token="
				+ token + ", appointmentId=" + appointmentId + "]";
	}
	
	
}
