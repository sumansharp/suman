/**
 * 
 */
package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.bson.Document;
import org.springframework.data.annotation.Id;

/**
 * @author Efficenz
 *
 */
public class Patient {
	@Id
	private String id;
	 String firstname;
	String lastname;  
	//Changed to name as per Aditya on 19Feb2017
	
	//String name; 
	//Reverted back on 20Feb2017
	String dateofbirth;
	String gender;
	String deviceId;
	String phonenumber;
	String ostype;
	String password;
	//String location; changed to address as per Aditya on 20Feb2017 
	String address; 
	String token; // Facebook / Google Token
	String dateRegistered;
	String emergencyContactNumber;
	String userImage; // Image Path
	ArrayList<Document> reports;
	HashMap<String,String> prescriptions; 
	String refId;
	String relationship; // Self by default
	HashMap<String,String> chatHistories;
	HashMap<String,String> paymentHistories;
	ArrayList<String> notificationSettings;
	ArrayList<String> alertsSubscribed;
	ArrayList<String> offersSubscribed;
	String city;
	String state;
	String bloodgroup;
	String email;
	String relationshipId;
	String pincode;
	String heightFt;
	String heightInches;
	String weight;
	


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
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}



	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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



	/**
	 * @return the ostype
	 */
	public String getOstype() {
		return ostype;
	}



	/**
	 * @param ostype the ostype to set
	 */
	public void setOstype(String ostype) {
		this.ostype = ostype;
	}



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
	 * @return the dateRegistered
	 */
	public String getDateRegistered() {
		return dateRegistered;
	}



	/**
	 * @param dateRegistered the dateRegistered to set
	 */
	public void setDateRegistered(String dateRegistered) {
		this.dateRegistered = dateRegistered;
	}



	/**
	 * @return the emergencyContactNumber
	 */
	public String getEmergencyContactNumber() {
		return emergencyContactNumber;
	}



	/**
	 * @param emergencyContactNumber the emergencyContactNumber to set
	 */
	public void setEmergencyContactNumber(String emergencyContactNumber) {
		this.emergencyContactNumber = emergencyContactNumber;
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
	 * @return the reports
	 */
	public ArrayList<Document> getReports() {
		return reports;
	}



	/**
	 * @param reports the reports to set
	 */
	public void setReports(ArrayList<Document> reports) {
		this.reports = reports;
	}



	/**
	 * @return the prescriptions
	 */
	public HashMap<String, String> getPrescriptions() {
		return prescriptions;
	}



	/**
	 * @param prescriptions the prescriptions to set
	 */
	public void setPrescriptions(HashMap<String, String> prescriptions) {
		this.prescriptions = prescriptions;
	}




	/**
	 * @return the relationship
	 */
	public String getRelationship() {
		return relationship;
	}



	/**
	 * @param relationship the relationship to set
	 */
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}



	/**
	 * @return the chatHistories
	 */
	public HashMap<String, String> getChatHistories() {
		return chatHistories;
	}



	/**
	 * @param chatHistories the chatHistories to set
	 */
	public void setChatHistories(HashMap<String, String> chatHistories) {
		this.chatHistories = chatHistories;
	}



	/**
	 * @return the paymentHistories
	 */
	public HashMap<String, String> getPaymentHistories() {
		return paymentHistories;
	}



	/**
	 * @param paymentHistories the paymentHistories to set
	 */
	public void setPaymentHistories(HashMap<String, String> paymentHistories) {
		this.paymentHistories = paymentHistories;
	}



	/**
	 * @return the notificationSettings
	 */
	public ArrayList<String> getNotificationSettings() {
		return notificationSettings;
	}



	/**
	 * @param notificationSettings the notificationSettings to set
	 */
	public void setNotificationSettings(ArrayList<String> notificationSettings) {
		this.notificationSettings = notificationSettings;
	}



	/**
	 * @return the alertsSubscribed
	 */
	public ArrayList<String> getAlertsSubscribed() {
		return alertsSubscribed;
	}



	/**
	 * @param alertsSubscribed the alertsSubscribed to set
	 */
	public void setAlertsSubscribed(ArrayList<String> alertsSubscribed) {
		this.alertsSubscribed = alertsSubscribed;
	}



	/**
	 * @return the offersSubscribed
	 */
	public ArrayList<String> getOffersSubscribed() {
		return offersSubscribed;
	}



	/**
	 * @param offersSubscribed the offersSubscribed to set
	 */
	public void setOffersSubscribed(ArrayList<String> offersSubscribed) {
		this.offersSubscribed = offersSubscribed;
	}



	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}



	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}



	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}



	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}



	/**
	 * @return the bloodgroup
	 */
	public String getBloodgroup() {
		return bloodgroup;
	}



	/**
	 * @param bloodgroup the bloodgroup to set
	 */
	public void setBloodgroup(String bloodgroup) {
		this.bloodgroup = bloodgroup;
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
	 * @return the refId
	 */
	public String getRefId() {
		return refId;
	}



	/**
	 * @param refId the refId to set
	 */
	public void setRefId(String refId) {
		this.refId = refId;
	}



	/**
	 * @return the relationshipId
	 */
	public String getRelationshipId() {
		return relationshipId;
	}



	/**
	 * @param relationshipId the relationshipId to set
	 */
	public void setRelationshipId(String relationshipId) {
		this.relationshipId = relationshipId;
	}





	public Patient() {
		}


	

	/**
	 * @param firstname
	 * @param lastname
	 * @param dateofbirth
	 * @param gender
	 * @param deviceId
	 * @param ostype 
	 * @param phonenumber 
	 */
	public Patient(String firstName,
				String lastName,
			String email,String relationship,String deviceId,String osType,String userImgUrl) {
		this.firstname = firstName;
		this.lastname = lastName; 
		 Date today = Calendar.getInstance().getTime();

		 SimpleDateFormat dateFormatIST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		this.dateRegistered = dateFormatIST.format(today);
		this.emergencyContactNumber= "";
		this.userImage = userImgUrl;
		HashMap h = new HashMap();
		this.reports = new ArrayList<Document>();
		this.prescriptions = h;
		
		this.dateofbirth = "";
		this.gender = "";
		this.address = "";
		this.relationship = relationship;
		this.chatHistories = h;
		this.paymentHistories = h ;
		this.notificationSettings = new ArrayList();
		this.alertsSubscribed = new ArrayList();
		this.offersSubscribed = new ArrayList();
		this.city = "";
		this.state= "";
		this.bloodgroup = "";
		this.email = email;
		this.relationshipId = relationship ; 
		this.deviceId = deviceId;
		this.ostype = osType;
		this.heightFt = "";
		this.pincode = "";
		this.heightInches = "";
		this.weight = "";
		this.phonenumber = "";
		  
	}


	
/*
	public Patient(String firstname2, 
			String lastname2, 
			String dateofbirth2, 
			String gender2, 
			String deviceid2,
			String phonenumber2, 
			String ostype2, 
			String password2, 
			String relationship2, 
			String parentid,
			String email) {
		// TODO Auto-generated constructor stub
		this.firstname = firstname2;
		this.lastname = lastname2;
		this.dateofbirth = dateofbirth2;
		this.gender = gender2;
		this.deviceId = deviceid2;
		this.phonenumber = phonenumber2;
		this.ostype = ostype2;
		this.password = password2;
		this.location = "";
		this.token = "";
		
		 Date today = Calendar.getInstance().getTime();

		 SimpleDateFormat dateFormatIST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		this.dateRegistered = dateFormatIST.format(today);
		this.emergencyContactNumber= "";
		this.userImage = "";
		HashMap h = new HashMap();
		this.reports = h;
		this.prescriptions = h;
		
		
		this.relationship = relationship2;
		this.chatHistories = h;
		this.paymentHistories = h ;
		this.notificationSettings = new ArrayList();
		this.alertsSubscribed = new ArrayList();
		this.offersSubscribed = new ArrayList();
		this.city = "";
		this.state= "";
		this.bloodgroup = "";
		this.email = "";
		this.relationshipId = parentid ; 

	}
*/
	

		/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}



	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}



	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}



	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}




	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}



	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}





	/**
	 * @return the pincode
	 */
	public String getPincode() {
		return pincode;
	}



	/**
	 * @param pincode the pincode to set
	 */
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}



	 

	/**
	 * @return the heightFt
	 */
	public String getHeightFt() {
		return heightFt;
	}



	/**
	 * @param heightFt the heightFt to set
	 */
	public void setHeightFt(String heightFt) {
		this.heightFt = heightFt;
	}



	/**
	 * @return the heightInches
	 */
	public String getHeightInches() {
		return heightInches;
	}



	/**
	 * @param heightInches the heightInches to set
	 */
	public void setHeightInches(String heightInches) {
		this.heightInches = heightInches;
	}



	/**
	 * @return the weight
	 */
	public String getWeight() {
		return weight;
	}



	/**
	 * @param weight the weight to set
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Patient [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", dateofbirth="
				+ dateofbirth + ", gender=" + gender + ", deviceId=" + deviceId + ", phonenumber=" + phonenumber
				+ ", ostype=" + ostype + ", password=" + password + ", address=" + address + ", token=" + token
				+ ", dateRegistered=" + dateRegistered + ", emergencyContactNumber=" + emergencyContactNumber
				+ ", userImage=" + userImage + ", reports=" + reports + ", prescriptions=" + prescriptions + ", refId="
				+ refId + ", relationship=" + relationship + ", chatHistories=" + chatHistories + ", paymentHistories="
				+ paymentHistories + ", notificationSettings=" + notificationSettings + ", alertsSubscribed="
				+ alertsSubscribed + ", offersSubscribed=" + offersSubscribed + ", city=" + city + ", state=" + state
				+ ", bloodgroup=" + bloodgroup + ", email=" + email + ", relationshipId=" + relationshipId
				+ ", pincode=" + pincode + ", heightFt=" + heightFt + ", heightInches=" + heightInches + ", weight="
				+ weight + "]";
	}
	
	
	
	
	

}
