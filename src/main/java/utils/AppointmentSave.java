package utils;

import org.springframework.data.annotation.Id;

public class AppointmentSave {
	@Id 
	private String id;
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
	String appointmentId;
	String chatText;
	int statusCode;

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
	 * @return the chatText
	 */
	public String getChatText() {
		return chatText;
	}
	/**
	 * @param chatText the chatText to set
	 */
	public void setChatText(String chatText) {
		this.chatText = chatText;
	}
	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppointmentSave [id=" + id + ", appointmentId=" + appointmentId + ", chatText=" + chatText
				+ ", statusCode=" + statusCode + "]";
	}
	 
	
	

}
