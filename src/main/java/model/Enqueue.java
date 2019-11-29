/**
 * 
 */
package model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.data.annotation.Id;

/**
 * @author Efficenz
 *
 */
public class Enqueue {

	String patientId;
	String doctorId;
	LocalDateTime requestDateAndTime;
	
	int chatStatus; // 22 - IN QUEUE 
					// 33 - TALKING  
					// 44 - CHAT ENDED NORMALLY 
					// 55 - CHAT ENDED ABRUPTLY
					// 66 - WILL CHAT LATER
					// 77 - PATIENT LEFT CHAT
					// 88 - DOCTOR LEFT ABRUPTLY
	LocalDateTime chatStartedAt ;
	LocalDateTime chatEndAt;
	int patientNumber; // Patient queue position as per the doctor
	String chatText;
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
	/**
	 * @return the patientId
	 */
	public String getPatientId() {
		return patientId;
	}
	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	/**
	 * @return the doctorId
	 */
	public String getDoctorId() {
		return doctorId;
	}
	/**
	 * @param doctorId the doctorId to set
	 */
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	
	/**
	 * @return the requestDateAndTime
	 */
	public LocalDateTime getRequestDateAndTime() {
		return requestDateAndTime;
	}
	/**
	 * @param requestDateAndTime the requestDateAndTime to set
	 */
	public void setRequestDateAndTime(LocalDateTime requestDateAndTime) {
		this.requestDateAndTime = requestDateAndTime;
	}
	/**
	 * @param patientId
	 * @param doctorId
	 */
	public Enqueue(String patientId, String doctorId) {
		this.patientId = patientId;
		this.doctorId = doctorId;
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		 
		this.requestDateAndTime = currentTime;


		
		
		this.chatStatus = 22;
/*Patient is talking , so populate chatStartedAt
		{
			this.chatStartedAt =currentTime;
		} */
		
		this.chatText = "";
	
		
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
	/**
	 * @return the chatStartedAt
	 */
	public LocalDateTime  getChatStartedAt() {
		return chatStartedAt;
	}
	/**
	 * @param chatStartedAt the chatStartedAt to set
	 */
	public void setChatStartedAt(LocalDateTime  chatStartedAt) {
		this.chatStartedAt = chatStartedAt;
	}
	/**
	 * @return the chatEndAt
	 */
	public LocalDateTime  getChatEndAt() {
		return chatEndAt;
	}
	/**
	 * @param chatEndAt the chatEndAt to set
	 */
	public void setChatEndAt(LocalDateTime  chatEndAt) {
		this.chatEndAt = chatEndAt;
	}
	/**
	 * @return the patientNumber
	 */
	public int getPatientNumber() {
		return patientNumber;
	}
	/**
	 * @param patientNumber the patientNumber to set
	 */
	public void setPatientNumber(int patientNumber) {
		this.patientNumber = patientNumber;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Enqueue [patientId=" + patientId + ", doctorId=" + doctorId + ", requestDateAndTime="
				+ requestDateAndTime + ", chatStatus=" + chatStatus + ", chatStartedAt=" + chatStartedAt
				+ ", chatEndAt=" + chatEndAt + ", patientNumber=" + patientNumber + ", chatText=" + chatText + ", id="
				+ id + "]";
	}
	 
	
	
	
	
}
