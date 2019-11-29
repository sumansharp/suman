/**
 * 
 */
package utils;

import java.util.ArrayList;

import org.bson.Document;

/**
 * @author Efficenz
 *
 */
public class PatientsinQueue {
	String id;

	String firstName;

	String lastName;
	String name;
	String dateofbirth;
	String gender;
	String userImage;
	String appointmentId;
	int chatStatus;
	String phonenumber;
	ArrayList<Document> reports;
	
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
	 * @return the dateofbirth
	 */
	public String getDateofbirth() {
		return dateofbirth;
	}
	/**
	 * @param dateofbirth the dateofbirth to set
	 */
	public void setDateofbirth(String dateofbirth) {
		this.dateofbirth = dateofbirth;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the userImage
	 */
	public String getUserImage() {
		return userImage;
	}
	/**
	 * @param userImage the userImage to set
	 */
	public void setUserImage(String userImage) {
		this.userImage = userImage;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the phonenumber
	 */
	public String getPhonenumber() {
		return phonenumber;
	}
	/**
	 * @param phonenumber the phonenumber to set
	 */
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public ArrayList<Document> getReports() {
		return reports;
	}
	public void setReports(ArrayList<Document> reports) {
		this.reports = reports;
	}
	
	 
	

	
	
}
