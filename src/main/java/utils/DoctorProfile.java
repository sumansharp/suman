package utils;

import java.util.ArrayList;

import org.bson.Document;
import org.springframework.data.annotation.Id;

public class DoctorProfile {
	@Id 
	private String id;
	
	String speciality;
	String bio;
	ArrayList<String> experience;
	ArrayList<String> medicalEducation;
	ArrayList<String> fellowships;
	int years;
	ArrayList<Document> publications ;
	Document  socialMedia ;
	ArrayList<String> awardsRecognition;
	ArrayList<String> otherAssociations;
	String profilePic;
	String degree;
	String clinicalInterest;
	
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
	 * @return the speciality
	 */
	public String getSpeciality() {
		return speciality;
	}
	/**
	 * @param speciality the speciality to set
	 */
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	/**
	 * @return the bio
	 */
	public String getBio() {
		return bio;
	}
	/**
	 * @param bio the bio to set
	 */
	public void setBio(String bio) {
		this.bio = bio;
	}
	/**
	 * @return the experience
	 */
	public ArrayList<String> getExperience() {
		return experience;
	}
	/**
	 * @param experience the experience to set
	 */
	public void setExperience(ArrayList<String> experience) {
		this.experience = experience;
	}
	/**
	 * @return the medicalEducation
	 */
	public ArrayList<String> getMedicalEducation() {
		return medicalEducation;
	}
	/**
	 * @param medicalEducation the medicalEducation to set
	 */
	public void setMedicalEducation(ArrayList<String> medicalEducation) {
		this.medicalEducation = medicalEducation;
	}
	/**
	 * @return the fellowships
	 */
	public ArrayList<String> getFellowships() {
		return fellowships;
	}
	/**
	 * @param fellowships the fellowships to set
	 */
	public void setFellowships(ArrayList<String> fellowships) {
		this.fellowships = fellowships;
	}
	/**
	 * @return the years
	 */
	public int getYears() {
		return years;
	}
	/**
	 * @param years the years to set
	 */
	public void setYears(int years) {
		this.years = years;
	}
	/**
	 * @return the publications
	 */
//	public HashMap<String, String> getPublications() {
//		return publications;
//	}
	/**
	 * @param publications the publications to set
	 */
//	public void setPublications(HashMap<String, String> publications) {
//		this.publications = publications;
//	}
	/**
	 * @return the awardsRecognition
	 */
	public ArrayList<String> getAwardsRecognition() {
		return awardsRecognition;
	}
	/**
	 * @param awardsRecognition the awardsRecognition to set
	 */
	public void setAwardsRecognition(ArrayList<String> awardsRecognition) {
		this.awardsRecognition = awardsRecognition;
	}
	/**
	 * @return the otherAssociations
	 */
	public ArrayList<String> getOtherAssociations() {
		return otherAssociations;
	}
	/**
	 * @param otherAssociations the otherAssociations to set
	 */
	public void setOtherAssociations(ArrayList<String> otherAssociations) {
		this.otherAssociations = otherAssociations;
	}
	
	
	/**
	 * @return the socialMedia
	 */
	public Document getSocialMedia() {
		return socialMedia;
	}
	/**
	 * @param socialMedia the socialMedia to set
	 */
	public void setSocialMedia(Document socialMedia) {
		this.socialMedia = socialMedia;
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
	 * @return the clinicalInterest
	 */
	public String getClinicalInterest() {
		return clinicalInterest;
	}
	/**
	 * @param clinicalInterest the clinicalInterest to set
	 */
	public void setClinicalInterest(String clinicalInterest) {
		this.clinicalInterest = clinicalInterest;
	}
	/**
	 * @return the publications
	 */
	ArrayList<Document> getPublications() {
		return publications;
	}
	/**
	 * @param publications the publications to set
	 */
	void setPublications(ArrayList<Document> publications) {
		this.publications = publications;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DoctorProfile [id=" + id + ", speciality=" + speciality + ", bio=" + bio + ", experience=" + experience
				+ ", medicalEducation=" + medicalEducation + ", fellowships=" + fellowships + ", years=" + years
				+ ", publications=" + publications + ", socialMedia=" + socialMedia + ", awardsRecognition="
				+ awardsRecognition + ", otherAssociations=" + otherAssociations + ", profilePic=" + profilePic
				+ ", degree=" + degree + ", clinicalInterest=" + clinicalInterest + "]";
	}
	
	

}
