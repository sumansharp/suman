/**
 * 
 */
package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.EfficenzMongoDao;
import model.Doctor;
import utils.DoctorFacade;
import utils.DoctorProfile;
import utils.Keys;
import utils.PatientsinQueue;
import utils.filteredUser;

/**
 * @author Efficenz
 *
 */
 
@Service

public class DoctorServiceMobile {
	 
Hashtable<String,Doctor> doctors = new Hashtable<String,Doctor>();

ApplicationContext ctx; 
/**
 * 
 */
public DoctorServiceMobile() {
	 
	ctx = new GenericXmlApplicationContext("SpringConfig.xml"); 
}


/**
 * Inserts a new record representing a Doctor in MongoDO and  
 * returns SUCCESS upon successful insertion.
 * 
 * @param  firstName  First Name  
 * @param  lastName   Last  Name  
 * @param  gender 	  Gender
 * @param  email      Email
 * @param  location   Location
 * @return            SUCCESS
 */

/*public String addDoctor(String doctorName, String gender, String email,String dateofbirth,String degree) {
	
	MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	
	Query searchUserQuery = new Query(Criteria.where("email").is(email));
	Doctor doctorExists = mongoOperation.findOne(searchUserQuery, Doctor.class);
	if(doctorExists.getId().length() > 0 )
	{
		return doctorExists.getId();
	}
	else
	{
	
	Doctor doctor = new Doctor(doctorName,gender,email,dateofbirth,email);
	// save
	mongoOperation.save(doctor);
	// now user object got the created id.
	System.out.println("1. doctor : " + doctor.getId());
	return "Success";
	}
}
*/


public HashMap<String,List<filteredUser>> getDocList() {
	// TODO Auto-generated method stub
	
	List<filteredUser> users = EfficenzMongoDao.getInstance().getDocList();
	HashMap<String,List<filteredUser>> hmap = new HashMap<String,List<filteredUser>>();
	hmap.put("Doctors", users);
 	return hmap;
}


public List<Keys> getKeys2(String userId) {
	// TODO Auto-generated method stub
	
	Keys keys = EfficenzMongoDao.getInstance().getKeys2(userId);
	
	List<Keys> l =  new ArrayList<Keys>();
	l.add(keys);
	return l;
}

public HashMap<String, Object> loginM(String email,String password) {
	// TODO Auto-generated method stub
	return EfficenzMongoDao.getInstance().loginM(email, password);
	
}
/*
public Status login2(String email,String password) {
	
	MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

	Query searchUserQuery = new Query(Criteria.where("email").is(email).and("password").is(password));
	
	
	
	Status status = new Status();
	Doctor doctor = mongoOperation.findOne(searchUserQuery, Doctor.class);
 	
	doctor.setStatus(PropertyHotReloader.getProperty("doctor.available"));
	mongoOperation.save(doctor);
	status.setNotifier(doctor.getId());
	
	
	return status;
} 
*/
public String addToQueue(String patientId, 
		String doctorId, 
		String previousAppointmentId) {
	return EfficenzMongoDao.getInstance().addToQueue(patientId, doctorId, previousAppointmentId);
	}


public List<PatientsinQueue> listPatients(String doctorId) {

	return EfficenzMongoDao.getInstance().listPatients(doctorId);
}

public HashMap<String, String> sendOpenTokParametersToPatient(String patientId, String doctorId, String appointmentId) {
	
	return EfficenzMongoDao.getInstance().sendOpenTokParametersToPatient(patientId, doctorId,appointmentId);
	 
}

public String changeDoctor(String appointmentId, String doctorId) {
	return EfficenzMongoDao.getInstance().changeDoctor(appointmentId, doctorId);
	
}

public String logout(String doctorId) {

	 return EfficenzMongoDao.getInstance().logout(doctorId);
	
}

public String leaveQueue(String appointmentId) {

	return EfficenzMongoDao.getInstance().leaveQueue(appointmentId);
	
	}


public String chatStart(String appointmentId) {
	
return EfficenzMongoDao.getInstance().chatStart(appointmentId);	

}

public String changeAppointmentStatus(String appointmentId, int chatStatus) {
	return EfficenzMongoDao.getInstance().changeAppointmentStatus(appointmentId, chatStatus);
}


public String changeDoctorStatus(String doctorId, String statusCode) {

	return EfficenzMongoDao.getInstance().changeDoctorStatus(doctorId,statusCode);
}


public DoctorFacade getDoctor(String doctorId)
{

	return EfficenzMongoDao.getInstance().getDoctor(doctorId);
}


public String KillOpenTok(String patientId, String doctorId) {

return EfficenzMongoDao.getInstance().KillOpenTok(patientId, doctorId);

}


public String endConsultation(String appointmentId, String chatText, int chatStatus) {
	return EfficenzMongoDao.getInstance().endConsultation(appointmentId, chatText, chatStatus);
	
}


public DoctorProfile getDoctor2(String doctorId) {
	return EfficenzMongoDao.getInstance().getDoctor2(doctorId);
		
}


public String login2(String email, String password) {
	// TODO Auto-generated method stub
	return EfficenzMongoDao.getInstance().login2(email,password);
}


public String changeDoctorStatusM(String doctorId, boolean available) {
	// TODO Auto-generated method stub
	return EfficenzMongoDao.getInstance().changeDoctorStatusM(doctorId,available);
}


public HashMap<String,Object> listPatientsM(String doctorId) {
	// TODO Auto-generated method stub
	return EfficenzMongoDao.getInstance().listPatientsM(doctorId);
	
}


public String updateFirebaseToken(String doctorId, String firebaseToken) {
	// TODO Auto-generated method stub
	return EfficenzMongoDao.getInstance().updateFireBaseToken(doctorId,firebaseToken);

}


public String uploadDoctorImage(MultipartFile file) {
	// TODO Auto-generated method stub
	return EfficenzMongoDao.getInstance().uploadDoctorImage(file);
	//return null;
}


public HashMap<String,Object> listPatientsA() {
	// TODO Auto-generated method stub
	return EfficenzMongoDao.getInstance().listPatientsA();
}


public String doctorDoneForTheDay(String doctorId) {
	// TODO Auto-generated method stub
	return EfficenzMongoDao.getInstance().doctorDoneForTheDay(doctorId);
}

public HashMap<String, Object> getPastAppointments(String doctorId) {
	// TODO Auto-generated method stub
	HashMap<String,Object> hMap = new HashMap<String,Object>();
	hMap.put("responseCode","");
	hMap.put("responseMessage","");
	hMap.put("completedAppointments",EfficenzMongoDao.getInstance().getPastAppointments(doctorId));
	return hMap;
}








}
