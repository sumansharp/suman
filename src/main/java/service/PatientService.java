/**
 * 
 */
package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.bson.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

//import dao.PatientDao;
import dao.EfficenzMongoDao;
import model.Patient;
import utils.PatientFacade;
import utils.PatientWrapper;
/**
 * @author Efficenz
 *
 */

@Service 

public class PatientService {
 
	Hashtable<String,Patient> patients = new Hashtable<String,Patient>();
	ApplicationContext ctx; 	 

/*	public String addPatient(String firstname, 
			String lastname, 
			String dateofbirth, 
			String gender, 
			String deviceid, 
			String phonenumber, 
			String ostype,
			String password,
			String relationship, 
			String email) {
 		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");
		ctx.registerShutdownHook();

		 
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		
		Query searchUserQuery = new Query(Criteria.where("email").is(email));
		boolean patientExists = mongoOperation.exists(searchUserQuery, Patient.class);
		System.out.println(patientExists);
		if(patientExists == false)
		{
			Patient patient = new Patient(firstname,
					lastname,
					dateofbirth, 
					gender,
					deviceid,
					phonenumber,
					ostype,
					password,
					relationship,
					email);
			// save
			mongoOperation.save(patient);
			// now user object got the created id.

			ctx.close();
			return patient.getId();

		}
		else
		{
			System.out.println("AAAA");
			MongoOperations mongoOperation2 = (MongoOperations) ctx.getBean("mongoTemplate");
			System.out.println("1");
			Query searchUserQuery2 = new Query();
			searchUserQuery2.addCriteria(Criteria.where("email").is(email));
			System.out.println(searchUserQuery2.toString());
			Patient p = mongoOperation2.findOne(searchUserQuery2, Patient.class);

			//modify and update with save()
			p.setFirstname(firstname);
			p.setLastname(lastname);
			p.setDateofbirth(dateofbirth);
			p.setGender(gender);
			p.setOstype(ostype);
			p.setDeviceId(deviceid);
			p.setPassword(password);
			 
			mongoOperation.save(p);
			ctx.close();
			return p.getId();
		}
 	}
*/

	public PatientWrapper userLoginSignup(String firstName,
			String lastName,
			
			String email,String relationship,String deviceId,String osType,String userImgUrl) {
		
		return EfficenzMongoDao.getInstance().userLoginSignup(firstName, lastName, email, relationship, deviceId, osType,userImgUrl);
		//return PatientDao.getInstance().userLoginSignup(firstName, lastName, email, relationship, deviceId, osType,userImgUrl);
		
 	}

public PatientService() {
}

	public String updatePatient(String firstName,
			String lastName,
			String dateofbirth,
			String gender,
			String deviceid,
			String phonenumber,
			String ostype,
			String address,
			String pincode,
			String emcontact,
			String email,
			String patientId,
			String heightFt,
			String heightInches,
			String weight) {
		return EfficenzMongoDao.getInstance().updatePatient(firstName, lastName, dateofbirth, gender, deviceid, phonenumber, ostype, address, pincode, emcontact, email, patientId, heightFt, heightInches, weight);
//		return PatientDao.getInstance().updatePatient(firstName, lastName, dateofbirth, gender, deviceid, phonenumber, ostype, address, pincode, emcontact, email, patientId, heightFt, heightInches, weight);
			}

	
		
	 /*
	public String addChild(String name, 
							String dateofbirth, 
							String gender, 
							String deviceid,
							String phonenumber, 
							String ostype, 
							String password, 
							String relationship, 
							String parentid) {
		// TODO Auto-generated method stub
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");
		ctx.registerShutdownHook();

		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		Patient patient = new Patient(name,
				dateofbirth, 
				gender,
				deviceid,phonenumber,
				ostype,
				password,
				relationship,
				parentid);
		// save
		mongoOperation.save(patient);
		// now user object got the created id.

		ctx.close();	
		return patient.getId();
	}
*/
	public PatientFacade getPatient(String patientId)
	{
		return EfficenzMongoDao.getInstance().getPatient(patientId);
		//return PatientDao.getInstance().getPatient(patientId);		
		
	}
	

public String AddReport(String patientId, String reportName, String pdfName) {
	// TODO Auto-generated method stub
	return EfficenzMongoDao.getInstance().addReport(patientId,
													reportName,
													pdfName);
													
	

}

public HashMap<String,Object> getReports(String patientId) {
	// TODO Auto-generated method stub
	return EfficenzMongoDao.getInstance().getReports(patientId);
	
}

	
}
