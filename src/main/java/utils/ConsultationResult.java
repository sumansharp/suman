package utils;

public class ConsultationResult {
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
	 * @return the chatStatus
	 */
	public int getChatStatus() {
		return chatStatus;
	}
	/**
	 * @param chatStatus the chatStatus to set
	 */
	public void setChatStatus(int chatStatus) {
		this.chatStatus = chatStatus;
	}
	String chatText;
	int chatStatus;
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConsultationResult [appointmentId=" + appointmentId + ", chatText=" + chatText + ", chatStatus="
				+ chatStatus + "]";
	}
	
	
	
}
