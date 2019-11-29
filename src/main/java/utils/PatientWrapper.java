package utils;

import model.Patient;

public class PatientWrapper {
	boolean informationComplete;
	Patient user;
	/**
	 * @return the informationComplete
	 */
	public boolean isInformationComplete() {
		return informationComplete;
	}
	/**
	 * @param informationComplete the informationComplete to set
	 */
	public void setInformationComplete(boolean informationComplete) {
		this.informationComplete = informationComplete;
	}
	/**
	 * @return the user
	 */
	public Patient getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(Patient user) {
		this.user = user;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PatientWrapper [informationComplete=" + informationComplete + ", user=" + user + "]";
	}
	
	
		

}
