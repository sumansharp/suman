/**
 * 
 */
package utils;

import java.util.HashMap;

import model.Doctor;

/**
 * @author Efficenz
 *
 */
public class Body {
	public HashMap<String,String> doctor;
	public String notificationType;
	public String title;
	public String message;
	public String appointmentId;
	public String apiKey;
	public String secret;
	
	int patients;
	
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
	public Body() {
	}

	 

	/**
	 * @return the notificationType
	 */
	public String getNotificationType() {
		return notificationType;
	}

	/**
	 * @param notificationType the notificationType to set
	 */
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	
	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param secret the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Body [doctor=" + doctor + ", notificationType=" + notificationType + ", title=" + title + ", message="
				+ message + ", appointmentId=" + appointmentId + ", apiKey=" + apiKey + ", secret=" + secret + "]";
	}

	public int getPatients() {
		return patients;
	}

	public void setPatients(int patients) {
		this.patients = patients;
	}

	public void setDoctor(HashMap<String, String> doctor) {
		this.doctor = doctor;
	}

	 
	

	
	
}
