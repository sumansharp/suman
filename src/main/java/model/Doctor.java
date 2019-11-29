/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.annotation.Id;

/**
 * @author Efficenz
 *
 */
public class Doctor {

	@Id 
	private String id;
	String doctorName;

	String gender;
	String email;
	String dateofbirth;
	
	String sessionid; // OpenTok specific field . Admin to fill. 
	String token; // OpenTok specific field . Admin to fill. 
	
	String degree;
	String hospitalName;
	String publisherid; // Defaults to Admin as of now 
	String status; 
	String typeofdoctor; // Could be doctors/specialist/admin. 
						  //Admin role can not do a Video Chat. 
	
	

	String profilePic;
	
	ArrayList<String> firebaseTokens;
	
	 




	/**
	 * @return the doctorName
	 */
	public String getDoctorName() {
		return doctorName;
	}




	/**
	 * @param doctorName the doctorName to set
	 */
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}





	/**
						 * 
						 */
						public Doctor() {
						}




	String password;	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}




	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}




	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * @return the sessionid
	 */
	public String getSessionid() {
		return sessionid;
	}




	/**
	 * @param sessionid the sessionid to set
	 */
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
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




	/**
	 * @return the degree
	 */
	public String getDegree() {
		return degree;
	}




	/**
	 * @param degree the degree to set
	 */
	public void setDegree(String degree) {
		this.degree = degree;
	}




	/**
	 * @return the hospitalname
	 */
	public String getHospitalName() {
		return hospitalName;
	}




	/**
	 * @param hospitalname the hospitalname to set
	 */
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}




	/**
	 * @return the publisherid
	 */
	public String getPublisherid() {
		return publisherid;
	}




	/**
	 * @param publisherid the publisherid to set
	 */
	public void setPublisherid(String publisherid) {
		this.publisherid = publisherid;
	}




	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}




	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}




	/**
	 * @return the typeofdoctor
	 */
	public String getTypeofdoctor() {
		return typeofdoctor;
	}




	/**
	 * @param typeofdoctor the typeofdoctor to set
	 */
	public void setTypeofdoctor(String typeofdoctor) {
		this.typeofdoctor = typeofdoctor;
	}




	/**
	 * @return the profilePic
	 */
	public String getProfilePic() {
		return profilePic;
	}




	/**
	 * @param profilePic the profilePic to set
	 */
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}




	public Doctor(String doctorName, String gender, String email,Date dateofbirth,String degree ) {
		// TODO Auto-generated constructor stub
		super();
		this.doctorName = doctorName;
		this.gender = gender;
		this.email = email;
		//this.dateofbirth=dateofbirth;
		this.hospitalName = "NA";
		this.publisherid = "Admin";
		this.degree = degree;
		this.sessionid="Admin Please Fill In";
		this.token="Admin Please Fill In";
		this.status = "Not Logged In";
		this.typeofdoctor = "specialist";
		this.profilePic="http://placeholder.it/500X500";
		this.firebaseTokens = new ArrayList<String>();
		
	}




	public ArrayList<String> getFirebaseTokens() {
		return firebaseTokens;
	}




	public void setFirebaseTokens(ArrayList<String> firebaseTokens) {
		this.firebaseTokens = firebaseTokens;
	}




	 








	 
	
	
	
	
	
	
}
