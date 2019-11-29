/**
 * 
 */
package utils;

import org.springframework.data.annotation.Id;

/**
 * @author Efficenz
 *
 */
public class TnC {
	@Id
	private String id;
	private String version;
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
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @param version
	 */
	public TnC(String version) {
		this.version = version;
	}
	
	

}
