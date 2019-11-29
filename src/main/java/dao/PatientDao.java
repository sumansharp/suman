package dao;

 
import java.util.ArrayList;
 
import java.util.HashMap;


import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
 
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


 
import model.Patient;

import utils.PatientFacade;
import utils.PatientWrapper;
 

public class PatientDao {
	@Autowired
	private ApplicationContext ctx ;
	private PatientDao(){
		ctx = new GenericXmlApplicationContext("SpringConfig.xml");
	}

	private static class SingletonHelper{
		private static final PatientDao INSTANCE = new PatientDao();
	}

	public static PatientDao getInstance(){
		return SingletonHelper.INSTANCE;
	}

 

		
	//generic method to get all details of a patient
	public Patient getPatientDetails(String patientId)
	{
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		Query searchPatient = new Query(Criteria.where("_id")
				.is(new ObjectId(patientId)));
		return mongoOperation.findOne(searchPatient, Patient.class);

	}
	public PatientWrapper userLoginSignup(String firstName,
			String lastName,
			
			String email,String relationship,String deviceId,String osType,String userImgUrl) {
		
		PatientWrapper wrapperP = new PatientWrapper();
		 
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		
		Query searchUserQuery = new Query(Criteria.where("email").is(email));
 		boolean patientExists = mongoOperation.exists(searchUserQuery, Patient.class);
	 
 		
		if(patientExists == false)
		{
			Patient patient = new Patient(firstName,
					lastName,email,relationship,deviceId,osType,userImgUrl);
			// save
			mongoOperation.save(patient);
			wrapperP.setInformationComplete(false);
			wrapperP.setUser(patient);
		}
		else
		{
			//MongoOperations mongoOperation2 = (MongoOperations) ctx.getBean("mongoTemplate");
			Query searchUserQuery2 = new Query(Criteria.where("email").is(email));
			Update update = new Update();

			update.set("firstname", firstName);
			update.set("lastname", lastName);
			update.set("email", email); 
			update.set("relationshipId", relationship);
			update.set("relationship", relationship);
			update.set("deviceId", deviceId);
			update.set("ostype", osType);
			update.set("userImage", userImgUrl);
			mongoOperation.updateFirst(searchUserQuery2, update, "patient");
	//		mongoOperation.save(patient2);
			Patient patient2 = mongoOperation.findOne(
					searchUserQuery2, 
					Patient.class);
	
			if(null != patient2.getGender()
					&&
					null != patient2.getAddress()
					&&
					null != patient2.getDateofbirth() 
					&&
					null != patient2.getPhonenumber() )
			{
				wrapperP.setInformationComplete(true);
			}
			else
			{
				wrapperP.setInformationComplete(false);
			}
			wrapperP.setUser(patient2);
		}
		
		return wrapperP;
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
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		
		 

		Patient patient = getPatientDetails(patientId);
		//modify and update with save()
	
		/*
		 * 
							ostype,
							address,
							pincode,
							emcontact,
							email
		 */
		if(!firstName.equals("NA"))
		{
			patient.setFirstname(firstName);
		}
		if(!lastName.equals("NA"))
		{
			patient.setLastname(lastName);
			
		}
		if(!dateofbirth.equals("NA"))
		{
			patient.setDateofbirth(dateofbirth);
		}
		if(!gender.equals("NA"))
		{
			patient.setGender(gender);
		}
		if(!deviceid.equals("NA"))
		{
			patient.setDeviceId(deviceid);
		}
		if(!phonenumber.equals("NA"))
		{
			patient.setPhonenumber(phonenumber);
		}
		if(!ostype.equals("NA"))
		{
			patient.setOstype(ostype);
		}
		if(!address.equals("NA"))
		{
			patient.setAddress(address);
		}
		if(!pincode.equals("NA"))
		{
			
		}
		if(!emcontact.equals("NA"))
		{
			patient.setEmergencyContactNumber(emcontact);
		}

		if(!email.equals("NA"))
		{
			patient.setEmail(email);
		}
		if(!heightFt.equals("NA"))
		{
			patient.setHeightFt(heightFt);
		}
		if(!heightInches.equals("NA"))
		{
			patient.setHeightInches(heightInches);
		}
		if(!weight.equals("NA"))
		{
			patient.setWeight(weight);
		}
		//patient.setEmail(email);
		mongoOperation.save(patient);
		
		return "SUCCESS";
	}

	public PatientFacade getPatient(String patientId)
	{
		
		Patient patient = getPatientDetails(patientId);
		PatientFacade pf = new PatientFacade();
		pf.setId(patientId);
		pf.setName(patient.getFirstname() + " "+ patient.getLastname());
		
		return pf;
		
		
		
	}


	public String addReport(String patientId, String reportName, String pdfName) {
		// TODO Auto-generated method stub
	 	Update update = new Update();
		
		 Document publication3 = new Document();
		 publication3.append("title", reportName);
		 publication3.append("url", pdfName);

		 update.push("reports",  publication3);

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is( new ObjectId(patientId)));
		
		
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		mongoOperation.updateFirst(query, update, "patient");



		return "{\"responseCode\":\"success\",\"responseMessage\":\"\"}";

	}


	public HashMap<String,Object> getReports(String patientId) {
		// TODO Auto-generated method stub
		Patient patient = getPatientDetails(patientId);
		ArrayList<Document> reports = patient.getReports();
		HashMap<String,Object> reportMap = new HashMap<String,Object>();
		reportMap.put("responseCode", "success");
		reportMap.put("responseMessage", "");
		reportMap.put("reports", reports);
		return reportMap;
	}


	
	

}
