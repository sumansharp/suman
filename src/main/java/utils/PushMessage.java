/**
 * 
 */
package utils;

/**
 * @author Efficenz
 *
 */
public class PushMessage {
	String notificationType;
	String title;
	String message;
	MessageBody body;
	String appointmentId ;
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
	//	Doctor doctor;
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
	 * @return the doctor
	 */
//	public Doctor getDoctor() {
//		return doctor;
//	}
	/**
	 * @param doctor the doctor to set
	 */
//	public void setDoctor(Doctor doctor) {
//		this.doctor = doctor;
//	}
	/**
	 * 
	 */
	public PushMessage() {
	}
	
	/**
	 * @return the body
	 */
	public MessageBody getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(MessageBody body) {
		this.body = body;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PushMessage [notificationType=" + notificationType + ", title=" + title + ", message=" + message
				+ ", body=" + body + ", appointmentId=" + appointmentId + "]";
	}
	
	
	
	
	
}
