package utils;

import java.util.ArrayList;

public class DoctorForFCM {
	
	private String id;
	private String doctorName;

	
	private String sessionid; 
	ArrayList<String> firebaseTokens;
	
	private String degree;

	private String profilePic;

	private String token;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	
	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	@Override
	public String toString() {
		return "DoctorForFCM [id=" + id + ", doctorName=" + doctorName + ", sessionid=" + sessionid + ", token=" + token
				+ ", degree=" + degree + ", profilePic=" + profilePic + "]";
	}

	public ArrayList<String> getFirebaseTokens() {
		return firebaseTokens;
	}

	public void setFirebaseTokens(ArrayList<String> firebaseTokens) {
		this.firebaseTokens = firebaseTokens;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}


	
	
}
