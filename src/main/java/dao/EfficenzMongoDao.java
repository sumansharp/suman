package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Doctor;
import model.Enqueue;
import model.Patient;
import service.NotificationService;
import utils.Body;
import utils.DoctorComplete;
import utils.DoctorFacade;
import utils.DoctorForFCM;
import utils.DoctorProfile;
import utils.FCMMessage;
import utils.Keys;
import utils.PatientFacade;
import utils.PatientWrapper;
import utils.PatientsinQueue;
import utils.PropertyHotReloader;
import utils.filteredUser;


public class EfficenzMongoDao {
	@Autowired
	private ApplicationContext ctx ;
	
	@Autowired
	NotificationService notificationService;
	
	private EfficenzMongoDao(){
		ctx = new GenericXmlApplicationContext("SpringConfig.xml");
		notificationService = new NotificationService();
	}

	private static class SingletonHelper{
		private static final EfficenzMongoDao INSTANCE = new EfficenzMongoDao();
	}

	public static EfficenzMongoDao getInstance(){
		return SingletonHelper.INSTANCE;
	}

	private final int WAITING = 22;
	private final int TALKING = 33;
	private final int NORMALCOMPLETE = 44;
	private final int TALKLATER = 66;
	private final int DOCTORLEFTCHAT = 88;
	private final int PATIENTREJECTED = 99;
	private final int PATIENTLEFTVOLUNTARILY = 77;
	private final String GOOGLEAPIKEY = "key=AIzaSyBYFAasqfwMYS9cLdbTVruKQckNcwXAm9Q";

	private final List<Integer> listOfChatStatus = new ArrayList<Integer>(Arrays.asList(22,33));
	 

	public List<filteredUser> getDocList() {
		// TODO Auto-generated method stub

	
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		Query searchUserQuery = new Query();
	//	searchUserQuery.with(new Sort(Sort.Direction.ASC, "status"));
	//	searchUserQuery.with(new Sort(Sort.Direction.ASC, "_id"));
		List<Doctor> d = mongoOperation.find(searchUserQuery, Doctor.class);
		List<filteredUser> users = new ArrayList<filteredUser>();
		Iterator<Doctor> itr = d.iterator();
		int counter = 0 ;

		while(itr.hasNext())
		{

			
			counter = counter + 1;
			filteredUser f = new filteredUser();
			Doctor tempD = itr.next();
			//System.out.println(tempD);
			String tempStatus = tempD.getStatus(); 
			f.setStatus(tempStatus);
			f.setDoctorName(tempD.getDoctorName());
			String doctorId = tempD.getId();
			f.setDoctorId(doctorId);
			f.setDegree(tempD.getDegree());
			 
			//System.out.println(tempD.getDateofbirth().toString()); 
			//f.setDateOfBirth(tempD.getDateofbirth());
			f.setHospitalName(tempD.getHospitalName());
			f.setPublisherId(tempD.getPublisherid());

			 Query searchPatientsWaitingQuery = new Query(Criteria.where("doctorId")
					.is(doctorId)
					.and("chatStatus").in(listOfChatStatus)
					); 
			 
			 
			int noOfPatients = mongoOperation.find(searchPatientsWaitingQuery, 
					Enqueue.class).size();
			 

			if(tempStatus.equals("Not Available") || tempStatus.equals("Away"))
			{
				f.setEWT(-1);
			}
			else
			{
				f.setEWT(noOfPatients); 
			}
			f.setDocImg(tempD.getProfilePic());
			users.add(f);
			System.out.println(users.toString());
			f = null;
			searchPatientsWaitingQuery = null;
		}
		return users;
	}

	public Keys getKeys2(String userId) {
		// TODO Auto-generated method stub


		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		Query searchUserQuery = new Query(Criteria.where("_id").is( new ObjectId(userId)));


		Keys k = new Keys();
		Doctor doctor = getDoctorDetails(userId);
		//mongoOperation.findOne(searchUserQuery, Doctor.class);
		k.setSessionId(doctor.getSessionid());
		k.setToken(doctor.getToken());
		k.setApiKey(PropertyHotReloader.getProperty("opentok.apikey"));
		k.setSecret(PropertyHotReloader.getProperty("opentok.secret"));


		return k;
	}

	public String login(String email,String password) {
		String result = "";

		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		Query searchUserQuery = new Query(Criteria.
				where("email").
				is(email).
				and("password").
				is(password)
				);


		boolean doctorExists = mongoOperation.exists(searchUserQuery, 
				Doctor.class);

		if(doctorExists)
		{
			// Set Doctor status to Available

			Doctor doctor = mongoOperation.findOne(searchUserQuery, 
					Doctor.class);
			if(doctor.getStatus().equals(PropertyHotReloader.getProperty("doctor.available")))
			{
				result="{\"notifier\":\"Already Logged In\"}";
			}
			else
			{
				Update update = new Update();
				update.set("status", "Available");

 				mongoOperation.updateFirst(searchUserQuery, update, "doctor");

				result="{\"notifier\":\"Available\"}";
			}
		}
		else
		{
			result="{\"notifier\":\"Invalid User\"}";
		}


		return result;
	}


	public HashMap<String, String> sendOpenTokParametersToPatient(String patientId, 
			String doctorId, String appointmentId) {


		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		DoctorForFCM d = getDoctorDetailsFCM(doctorId);
		
		HashMap <String,String> doctorMap = new HashMap<String,String>();
		
		doctorMap.put("sessionid", d.getSessionid());
		doctorMap.put("token", d.getToken());
		
		doctorMap.put("doctorName", d.getDoctorName());
		doctorMap.put("degree", d.getDegree());
		doctorMap.put("profilePic", d.getProfilePic());
		doctorMap.put("id", d.getId());

		
		Patient p = getPatientDetails(patientId);
	
		HashMap<String,String> returnData = new HashMap<String,String>();
		returnData.put("responseCode","success");
		returnData.put("responseMessage","");
		returnData.put("apiKey",PropertyHotReloader.getProperty("opentok.apikey")
				);
		returnData.put("secret",PropertyHotReloader.getProperty("opentok.secret")
				);
		String yy = d.getSessionid();
		returnData.put("sessionId",yy);
		returnData.put("token", d.getToken());
		
		//System.out.println(returnData.toString());
		Query appointmentQuery = new Query(
				Criteria.where("_id")
				.is(new ObjectId(appointmentId)
								));

		
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Calcutta"));
		 
		
				 
		Update update = new Update();
		update.set("chatStatus", TALKING);
		update.set("chatStartedAt", currentTime); 
		//work on this after Aditya fixes
		mongoOperation.updateFirst(appointmentQuery, update, "enqueue");
		
		Body b = new Body();
		b.doctor = doctorMap;
        b.setNotificationType("1");
		b.setTitle("Doctor is ready");
		b.setMessage("Happy New Year");
		b.setAppointmentId(appointmentId);
		b.setApiKey(
				PropertyHotReloader.getProperty("opentok.apikey")
				);
		b.setSecret(
				PropertyHotReloader.getProperty("opentok.secret")
				);


		HashMap <String,Object> hMap = new HashMap <String,Object>();
		hMap.put("body", b);
		
		ArrayList<String> sendTo = new ArrayList<>();
		sendTo.add(p.getDeviceId());
		
		notificationService.sendNotification(sendTo, hMap);
		
		return returnData;

	}


	public String changeDoctor(String appointmentId, 
			String doctorId) { 
		
	

		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is( new ObjectId(appointmentId)));

		Enqueue enqueue = mongoOperation.findOne(query, Enqueue.class);

		enqueue.setDoctorId(doctorId);
		//modify and update with save()

		mongoOperation.save(enqueue);

		//Moving to returning JSON Strings rather than Objects
		// In order to save time and
		// eliminate Unnecessary classes
		return
				"{\"result\" : \"success\",\"wait\" : true, \"sessionId\": \"\", \"token\" : \"\",\"appointmentId\" : " + "\"" + appointmentId +"\"" + "}";

	}


	public String logout(String doctorId) {


	
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		// 1. Set Status Not Available

		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is( new ObjectId(doctorId)));
		
		//Doctor doctor = getDoctorDetails(doctorId);
		
		
		Update update2 = new Update();
		update2.set("status", "Not Available");

		mongoOperation.updateFirst(query, update2, "doctor");

		//modify and update with save()
		//mongoOperation.save(doctor);


		// Remove all patients from the queue	

		Query query2 = new Query();
		query2.addCriteria(Criteria.where("doctorId")
				.is(doctorId)
				.and("chatStatus")
				.in(listOfChatStatus));
		Update update = new Update();
		List<Enqueue> l = mongoOperation.find(query2,Enqueue.class);
		Iterator itr = l.iterator();
		while(itr.hasNext())
		{
			Enqueue e = (Enqueue) itr.next();
			sendLogoutNotification(e.getPatientId(),e.getDoctorId(),e.getId());
		}

		update.set("chatStatus", DOCTORLEFTCHAT); // Doctor Left chat abruptly
		mongoOperation.updateMulti(query2, update, Enqueue.class);



		// Return Status Not Available



		return "{\"notifier\":\"Not Available\"}";

	}

	public String sendLogoutNotification(String patientId, String doctorId, String appointmentId) {

		DoctorForFCM d = getDoctorDetailsFCM(doctorId);
		
	HashMap <String,String> doctorMap = new HashMap<String,String>();
		
		doctorMap.put("sessionid", d.getSessionid());
		doctorMap.put("token", d.getToken());
		
		doctorMap.put("doctorName", d.getDoctorName());
		doctorMap.put("degree", d.getDegree());
		doctorMap.put("profilePic", d.getProfilePic());
		doctorMap.put("id", d.getId());

		//mongoOperation.findOne(searchDoctorQuery, Doctor.class);
		//	    	Patient p = mongoOperation.findOne(searchPatientQuery, Patient.class);
		//Refactored to get Patient Details from the generic method
		//getPatientDetails
		Patient p = getPatientDetails(patientId);

		String deviceId = p.getDeviceId();


		
		Body b = new Body();
		b.doctor = doctorMap;
		b.setNotificationType("3");
		b.setTitle("Appointment Update");
		b.setMessage("Dr had to leave on emergency. Please select next available doctor.");
		b.setAppointmentId(appointmentId);


		HashMap <String,Object> hMap = new HashMap <String,Object>();
		List<Body> bodies = new ArrayList<Body>();
		bodies.add(b);
		hMap.put("body", b);
		
		ArrayList<String> sendToList = new ArrayList<>();
		sendToList.add(deviceId);
	    notificationService.sendNotification(sendToList, hMap);		

		return "{\"success\":1}";

	}


	public List<PatientsinQueue> listPatients(String doctorId) {
		// TODO Auto-generated method stub

		List<PatientsinQueue> plist = new ArrayList<PatientsinQueue>();
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	
		Query searchPatientsWaitingQuery = new Query(Criteria.where("doctorId").is(doctorId)
				.and("chatStatus").in(listOfChatStatus)
 
				); 

	//	searchPatientsWaitingQuery.with(new Sort(Sort.Direction.DESC, "chatStatus"));
	//	searchPatientsWaitingQuery.with(new Sort(Sort.Direction.ASC, "_id")); 
		
		// Changed DESC to ASC to list the patients by first on top 

		List<Enqueue> d = mongoOperation.find(searchPatientsWaitingQuery, Enqueue.class);

		Iterator<Enqueue> itr = d.iterator();

		while(itr.hasNext())
		{
			PatientsinQueue p = new PatientsinQueue();
			Enqueue e = itr.next();

			Patient p2 = getPatientDetails(e.getPatientId());
			p.setFirstName(p2.getFirstname());
			p.setLastName(p2.getLastname());

			p.setDateofbirth(p2.getDateofbirth());
			p.setGender(p2.getGender());
			p.setUserImage(p2.getUserImage());
			p.setId(p2.getId());
			p.setAppointmentId(e.getId()); // Added Appointment Id for a granular control over chat
			p.setChatStatus(e.getChatStatus());
			p.setPhonenumber(p2.getPhonenumber());
			p.setReports(p2.getReports());
			plist.add(p);
			p = null;
		}

		return plist;
	}



	public String leaveQueue(String appointmentId) {

		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is( new ObjectId(appointmentId)));
		Enqueue enqueue = mongoOperation.findOne(query, Enqueue.class);
		enqueue.setChatStatus(PATIENTLEFTVOLUNTARILY); // Patient Left the queue voluntarily
		mongoOperation.save(enqueue);


		return "SUCCESS";
	}


	public String chatStart(String appointmentId) {
		
		
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id")
				.is( new ObjectId(appointmentId)));
		
		Enqueue enqueue = mongoOperation.findOne(query, Enqueue.class);
		enqueue.setChatStatus(TALKING); // Patient is talking 
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Calcutta"));
		 
		enqueue.setChatStartedAt(currentTime);
			mongoOperation.save(enqueue);
			
		return "SUCCESS";
		

	}

	public String changeAppointmentStatus(String appointmentId, int chatStatus) {
		
		String result;
		
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is( new ObjectId(appointmentId)));

		Enqueue enqueue = mongoOperation.findOne(query, Enqueue.class);
	
	
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		switch(chatStatus)
		{
		case TALKING:
			enqueue.setChatStatus(TALKING); // Patient is talking 
			 
			enqueue.setChatStartedAt(currentTime);
			mongoOperation.save(enqueue);
			result = "SUCCESS";
		case NORMALCOMPLETE: // Chat ended Normally 
			 
			enqueue.setChatStatus(chatStatus);
			enqueue.setChatEndAt(currentTime);
			mongoOperation.save(enqueue);
			result = "SUCCESS";
			break;
		case 55: // Chat ended Abruptly
		 	enqueue.setChatStatus(chatStatus);
			enqueue.setChatEndAt(currentTime);
			mongoOperation.save(enqueue);
			result = "SUCCESS";
			break;
		case TALKLATER://Will talk later																																																																			::: 
			enqueue.setChatStatus(chatStatus);
			//enqueue.setChatEndAt(currentTime);
			//TODO write the logic of swapping patientnumber with the one next to it
			mongoOperation.save(enqueue);
			result = "SUCCESS";
			break;
		case PATIENTLEFTVOLUNTARILY: // Patient Left Chat
			enqueue.setChatStatus(chatStatus);
			enqueue.setChatEndAt(currentTime);
			mongoOperation.save(enqueue);
			result = "SUCCESS";
			break;
		
		case DOCTORLEFTCHAT: // Doctor Left Chat
			enqueue.setChatStatus(chatStatus);
			enqueue.setChatEndAt(currentTime);
			mongoOperation.save(enqueue);
			result = "SUCCESS";
			break;

		case PATIENTREJECTED: // Patient Rejected Chat
			enqueue.setChatStatus(chatStatus);
			enqueue.setChatEndAt(currentTime);
			mongoOperation.save(enqueue);
			result = "SUCCESS";
			break;	
			
		default:
			result = "FAILURE";
			break;
		}
		
		return result;
	}
	
	public String changeDoctorStatus(String doctorId, String statusCode) {

		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is( new ObjectId(doctorId)));
		
		Doctor doctor = mongoOperation.findOne(query, Doctor.class);
		
		doctor.setStatus(statusCode);

		mongoOperation.save(doctor);
		//return "SUCCESS";
		
		return "{\"responseCode\":\"success\",\"responseMessage\":\"\"}";
	}

	public DoctorFacade getDoctor(String doctorId)
	{
		Doctor doctor = getDoctorDetails(doctorId);
		DoctorFacade df = new DoctorFacade();
		df.setId(doctorId);
		df.setName(doctor.getDoctorName());
		
		return df;
		
		
		
	}
	
	
	public String KillOpenTok(String patientId, String doctorId) {
		//Doctor d = getDoctorDetails(doctorId);
		DoctorForFCM d = getDoctorDetailsFCM(doctorId);
		
		Enqueue enqueue = getOngoingAppointmentDetails(doctorId,patientId);
		String appointmentId = enqueue.getId();
		
	    HashMap <String,String> doctorMap = new HashMap<String,String>();
		doctorMap.put("sessionid", d.getSessionid());
		doctorMap.put("token", d.getToken());
		doctorMap.put("doctorName", d.getDoctorName());
		doctorMap.put("degree", d.getDegree());
		doctorMap.put("profilePic", d.getProfilePic());
		doctorMap.put("id", d.getId());
	
		Body b = new Body();
		b.doctor = doctorMap;
		b.setNotificationType("2");
		b.setTitle("Done");
		b.setMessage("Thanks");
		b.setAppointmentId(appointmentId);
	 	
		HashMap <String,Object> hMap = new HashMap <String,Object>();
		hMap.put("body", b);
		
		Patient p = getPatientDetails(patientId);
		
	 	ArrayList<String> sendToList = new ArrayList<>();
	 	sendToList.add(p.getDeviceId());
		notificationService.sendNotification(sendToList, hMap);
		
		return "{\"success\":1}";
	 
	}


public String endConsultation(String appointmentId, String chatText, int chatStatus) {

	
	Enqueue appointment = getAppointmentDetails(appointmentId);
	LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
	appointment.setChatStatus(chatStatus);
	appointment.setChatEndAt(currentTime);
	appointment.setChatText(chatText);
	saveAppointment(appointment);
	
	return "{\"success\":1}";
}


	private void saveAppointment(Enqueue appointment) {
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		mongoOperation.save(appointment);
	
}


	public String addToQueue(String patientId, 
			String doctorId, 
			String previousAppointmentId) {
	

		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");


		// Handle case when previous AppointmentId is there.
		// Remove previousAppointmentId .
		// Insert a new record.
		
		int noOfPatients = 0;
		
		String appointmentId ;
		if(!previousAppointmentId.equals("NA"))
		{
		
			//Drop the old one 
			dropAppointment(previousAppointmentId);

						
			//Doctor d = getDoctorDetails(doctorId);
					
			Query searchPatientsTalkingQuery = new Query(Criteria.where("doctorId")
						.is(doctorId)
						.and("chatStatus")
						.in(listOfChatStatus));
				
				noOfPatients = mongoOperation.find(searchPatientsTalkingQuery, 
						Enqueue.class).size();
			// Add a new Record
				Enqueue enqueue = new Enqueue(patientId, doctorId);
				enqueue.setPatientNumber(noOfPatients + 1); // First Patient
				mongoOperation.save(enqueue);
				appointmentId = enqueue.getId();



		}
		else
		{
			// Search if the patient has booked any appointment previously.
			// If yes, return appointmentId
			
			Query searchPatient = new Query(Criteria.where("patientId").is(patientId).and("chatStatus").is(22));
			
			boolean alreadyBooked = mongoOperation.exists(searchPatient,
					Enqueue.class);
			if(alreadyBooked)
			{
				Enqueue existingqueue = mongoOperation.findOne(searchPatient, 
						Enqueue.class);
				
				appointmentId = existingqueue.getId();

				//Doctor d = getDoctorDetails(doctorId);
				
				Query searchPatientsWaitingQuery = new Query(Criteria.where("doctorId")
						.is(doctorId)
						.and("chatStatus").in(listOfChatStatus)
						
						);   
				noOfPatients = mongoOperation.find(searchPatientsWaitingQuery, 
						Enqueue.class).size();

	 
				
			}
			else
			{
		//Create a new Appointment
				//Doctor d = getDoctorDetails(doctorId);

				Query searchPatientsWaitingQuery = new Query(Criteria.where("doctorId")
						.is(doctorId)
						.and("chatStatus")
						.in(listOfChatStatus));
		
				
				
				
				noOfPatients = mongoOperation.find(searchPatientsWaitingQuery, 
						Enqueue.class).size();

				Enqueue enqueue = new Enqueue(patientId, doctorId);
				enqueue.setPatientNumber(noOfPatients + 1); // First Patient
				enqueue
				.setChatStatus(WAITING);
				mongoOperation.save(enqueue);
				appointmentId = enqueue.getId();

			}
			
		}
		
	sendPatientExpressedInterestNotification(patientId, doctorId) ;

		
	return
				"{\"result\" : \"success\",\"wait\" : true, \"sessionId\": \"\", \"token\" : \"\",\"appointmentId\" : " + "\"" + appointmentId +"\"" + "}";

	}

	
	
	//generic method to get all details of a patient
	public Patient getPatientDetails(String patientId)
	{
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		Query searchPatient = new Query(Criteria.where("_id")
				.is(new ObjectId(patientId)));
		return mongoOperation.findOne(searchPatient, Patient.class);

	}

	//generic method to get all details of a Doctor
	public Doctor getDoctorDetails(String doctorId)
	{
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		Query searchDoctor = new Query(Criteria.where("_id")
				.is(new ObjectId(doctorId)));
		return mongoOperation.findOne(searchDoctor, Doctor.class);

	}
	
	//method to get details of ongoingappointment
	public Enqueue getOngoingAppointmentDetails(String doctorId, String patientId)
	{
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		Query appointmentQuery = new Query(
				Criteria.where("doctorId")
				.is(doctorId)
				.and("patientId")
				.is(patientId)
				.and("chatStatus")
				.is(TALKING)
				);
		//System.out.println(appointmentQuery.toString());
		return mongoOperation.findOne(appointmentQuery, Enqueue.class);
	}

	//method to get appointment details
	
	public Enqueue getAppointmentDetails(String appointmentId)
	{
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		Query searchAppointment = new Query(Criteria.where("_id")
				.is(new ObjectId(appointmentId)));
		return mongoOperation.findOne(searchAppointment, Enqueue.class);

	}
	
	private void dropAppointment(String appointmentId)
	{
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		Query searchAppointment = new Query(Criteria.where("_id")
				.is(new ObjectId(appointmentId)));
		mongoOperation.remove(searchAppointment, Enqueue.class);

	}
	
	//Method to return metadata about doctor
	//When user clicks on read more button
	//on doctorlist
	public DoctorProfile getDoctor2(String doctorId) {
	
		
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		DoctorProfile doctorProfile = mongoOperation.findById(new ObjectId(doctorId),DoctorProfile.class,"doctor");
		
		return doctorProfile;
		
	}
	
	//Patient Block starts 
	
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

	public String login2(String email, String password) {
		// TODO Auto-generated method stub
		
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		Query searchUserQuery = new Query(Criteria.where("email").is(email).and("password").is(password));
		
		
		
		Doctor doctor = mongoOperation.findOne(searchUserQuery, Doctor.class);
		Update update = new Update();
		update.set("status", "Available");

		mongoOperation.updateFirst(searchUserQuery, update, "doctor");
		return "{\"notifier\":\""+doctor.getId()+"\"}";
		
	}

	public HashMap<String,Object> loginM(String email, String password) {
		String result = "";

		HashMap <String,Object> h = new HashMap <String,Object>();
		
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		Query searchUserQuery = new Query(Criteria.
				where("email").
				is(email).
				and("password").
				is(password)
				);


		boolean doctorExists = mongoOperation.exists(searchUserQuery, 
				Doctor.class);

		Doctor doctor = mongoOperation.findOne(searchUserQuery, 
				Doctor.class);
		if(doctorExists)
		{
			// Set Doctor status to Available

			/* Doctor doctor = mongoOperation.findOne(searchUserQuery, 
					Doctor.class); */ 
				Update update = new Update();
				update.set("status", "Available");
				mongoOperation.updateFirst(searchUserQuery, update, "doctor");

				h.put("responseCode", "success");
				h.put("responseMessage", "");
				int counter = 0 ;

				
				counter = counter + 1;
				filteredUser f = new filteredUser();
				
				f.setStatus(doctor.getStatus());
				f.setDoctorName(doctor.getDoctorName());
				f.setDoctorId(doctor.getId());
				f.setDegree(doctor.getDegree());
				//f.setDateOfBirth(doctor.getDateofbirth());
				f.setHospitalName(doctor.getHospitalName());
				f.setPublisherId(doctor.getPublisherid());

				Query searchPatientsWaitingQuery = new Query(Criteria.where("doctorId")
						.is(doctor.getId())
						.and("chatStatus").in(listOfChatStatus)
						); 
				int noOfPatients = mongoOperation.find(searchPatientsWaitingQuery, 
						Enqueue.class).size();


				if(doctor.getStatus().equals("Not Available") || doctor.getStatus().equals("Away"))
				{
					f.setEWT(-1);
				}
				else
				{
					f.setEWT(noOfPatients); 
				}
				f.setDocImg(doctor.getProfilePic());
				
			
				h.put("doctor", f);

				//result="{\"notifier\":\"Available\"}";
		}
		else
		{
			h.put("responseCode", "error");
			h.put("responseMessage", "Incorrect userId/Password combination");

			
			h.put("doctor", null);
			//result="{\"notifier\":\"Invalid User\"}";
		}

					//System.out.println(h.toString());
		return h;

	}

	public HashMap<String,Object> listPatientsM(String doctorId) {
	 

		HashMap <String,Object> h = new HashMap <String,Object>();

		List<HashMap <String,Object>> plist = new ArrayList<HashMap <String,Object>>();
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	
		Query searchPatientsWaitingQuery = new Query(Criteria.where("doctorId").is(doctorId)
				.and("chatStatus").in(listOfChatStatus)

				); 

		
	//	searchPatientsWaitingQuery.with(new Sort(Sort.Direction.DESC, "chatStatus"));
	//	searchPatientsWaitingQuery.with(new Sort(Sort.Direction.DESC, "_id"));

		List<Enqueue> d = mongoOperation.find(searchPatientsWaitingQuery, Enqueue.class);

		Iterator<Enqueue> itr = d.iterator();

		while(itr.hasNext())
		{
			HashMap <String,Object> patientCover = new HashMap<String,Object>();
			
			PatientsinQueue p = new PatientsinQueue();
			Enqueue e = itr.next();
			
			
			Patient p2 = getPatientDetails(e.getPatientId());
			patientCover.put("patientId", p2.getId());
			patientCover.put("firstName",p2.getFirstname());
			patientCover.put("lastName",p2.getLastname());
			patientCover.put("dateofbirth",p2.getDateofbirth());
			patientCover.put("gender",p2.getGender());
			patientCover.put("userImage",p2.getUserImage());
			//p.setId(p2.getId());
			patientCover.put("appointmentId",e.getId()); // Added Appointment Id for a granular control over chat
			patientCover.put("chatStatus",new Integer(e.getChatStatus()).toString());
			patientCover.put("phonenumber",p2.getPhonenumber());
			
			ArrayList<Document> reports = p2.getReports();
			Collections.reverse(reports); // To show latest on top 
			patientCover.put("reports", reports);
			
			plist.add(patientCover);
			p = null;
		}
		h.put("appointments", plist);
		h.put("responseCode", "success");
		h.put("responseMessage", "");
		return h;

	}

	public String updateFireBaseToken(String doctorId, String firebaseToken) {
		// TODO Auto-generated method stub
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		Query searchDoctorQuery = new Query(Criteria.where("_id").is(new ObjectId(doctorId))
								); 

		
		Update update = new Update();
		update.addToSet("firebaseTokens",firebaseToken);
		
		mongoOperation.updateFirst(searchDoctorQuery, update, "doctor");
		return "{\"responseCode\" : \"success\",\"responseMessage\" : \"\" }";

	}

	public String changeDoctorStatusM(String doctorId, boolean status) {
		// TODO Auto-generated method stub
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is( new ObjectId(doctorId)));
		
		Doctor doctor = mongoOperation.findOne(query, Doctor.class);
		
		//doctor.setStatus(status);
		Update update = new Update();
		if(status == true)
		{
			update.set("status", "Available");
		}
		else
		{
			update.set("status", "Away");
		}
		
			mongoOperation.updateFirst(query, update, "doctor");
		boolean isAvailable = false;
		
		if(status == true)
			isAvailable = true;
		
		
		return "{\"responseCode\":\"success\",\"responseMessage\":\"\",\"available\":"+new Boolean(isAvailable).toString()+"}";
	}

	public String uploadDoctorImage(MultipartFile file) {
		File destination = new File(PropertyHotReloader.getProperty("doctorimageuploadpath")+"/"+file.getOriginalFilename());
		String imageUrl = PropertyHotReloader.getProperty("doctorimagewebpath")+"/"+ file.getOriginalFilename();
		try {
			file.transferTo(destination);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "{\"success\":\"true\",\"filepath\":"+ "\""+ imageUrl+"\"}";
		//imageUrl;
	}

	public String addDoctor(DoctorComplete doctorComplete) {
		String doctorName = doctorComplete.getDisplayname();
		String gender = doctorComplete.getGender();
		String email = doctorComplete.getEmail();
		String degree = doctorComplete.getDegree();
		String hospital = doctorComplete.getHospital();
		String profileImage = doctorComplete.getImageFilePath();
		String speciality = doctorComplete.getSpecialization();
		String yearsOfExperience = doctorComplete.getYears();
		String clinicalInterest = doctorComplete.getClinicalinterest();
		String sessionId = "";
		String token = "";
		
		//Split text from textArea on new line character
		 StringTokenizer st = new StringTokenizer(doctorComplete.getExperience(),"\n");
	     ArrayList<String> list = new ArrayList<String>();
	     while (st.hasMoreElements()) {
	    	 list.add(st.nextToken());
	     }
	     String[] experienceHolder = list.toArray(new String[list.size()]);
	     
	     ArrayList<String> experience = new ArrayList<String>();
	     experience.addAll(Arrays.asList(experienceHolder));
	     
	     
	     list = new ArrayList<String>();
	     st = null;
		 st = new StringTokenizer(doctorComplete.getEducation(),"\n");
	     //ArrayList<String> list = new ArrayList<String>();
	     while (st.hasMoreElements()) {
	    	 list.add(st.nextToken());
	     }
	     String[] education = list.toArray(new String[list.size()]);
		
	     ArrayList<String> medicalEducation  = new ArrayList<String>();
	     medicalEducation.addAll(Arrays.asList(education));
	     
	     list = new ArrayList<String>();
	     st = null;
		 st = new StringTokenizer(doctorComplete.getFellowships(),"\n");
	     //ArrayList<String> list = new ArrayList<String>();
	     while (st.hasMoreElements()) {
	    	 list.add(st.nextToken());
	     }
	     String[] fellowShipsHolder = list.toArray(new String[list.size()]); 
		 
	     ArrayList<String> fellowShips  = new ArrayList<String>();
	     fellowShips.addAll(Arrays.asList(fellowShipsHolder));
	     
	     list = new ArrayList<String>();
	     st = null;
		 st = new StringTokenizer(doctorComplete.getAwards(),"\n");
	     //ArrayList<String> list = new ArrayList<String>();
	     while (st.hasMoreElements()) {
	    	 list.add(st.nextToken());
	     }
	     String[] awardsHolder = list.toArray(new String[list.size()]);

	     ArrayList<String> awards  = new ArrayList<String>();
	     awards.addAll(Arrays.asList(awardsHolder));
	     
	     
	     list = new ArrayList<String>();
	     st = null;
		 st = new StringTokenizer(doctorComplete.getAssociations(),"\n");
	     //ArrayList<String> list = new ArrayList<String>();
	     while (st.hasMoreElements()) {
	    	 list.add(st.nextToken());
	     }
	     String[] otherAssociationsHolder = list.toArray(new String[list.size()]);
		 
	     ArrayList<String> otherAssociations  = new ArrayList<String>();
	     otherAssociations.addAll(Arrays.asList(otherAssociationsHolder));
	     
	     
	     HashMap<String,String> address = new HashMap<String,String>();
		 address.put("addressLine1", doctorComplete.getAddrLine1());
		 address.put("addressLine2", doctorComplete.getAddrLine2());
		 address.put("city", doctorComplete.getCity());
		 address.put("state", doctorComplete.getState());
		 address.put("zipcode", doctorComplete.getZipcode());
		 address.put("country", doctorComplete.getCountry());

		 Document socialMedia = new Document();
		 socialMedia.put("facebook", doctorComplete.getFacebookUrl());
		 socialMedia.put("linkedin", doctorComplete.getLinkedinUrl());
		 socialMedia.put("twitter", doctorComplete.getTwitterUrl());

		 ArrayList<Document> publications = new ArrayList<Document>();
		 Document publication1 = new Document();
		 publication1.append("title", doctorComplete.getTitlepublication1());
		 publication1.append("url", doctorComplete.getUrlpublication1());
		 
		 Document publication2 = new Document();
		 publication2.append("title", doctorComplete.getTitlepublication2());
		 publication2.append("url", doctorComplete.getUrlpublication2());
		 
		 Document publication3 = new Document();
		 publication1.append("title", doctorComplete.getTitlepublication3());
		 publication1.append("url", doctorComplete.getUrlpublication3());

		 publications.add(publication1);
		 publications.add(publication2);
		 publications.add(publication3);
		 
		 MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		 Document d = new Document();
		 d.append("doctorName", doctorName);
		 d.append("gender", gender);
		 d.append("email", email);
		 d.append("degree", degree);
		 d.append("hospitalName",hospital);
		 d.append("status", "Not Available");
		 d.append("profilePic", profileImage);
		 d.append("speciality", speciality);
		 d.append("years", yearsOfExperience);
		 d.append("clinicalInterest", clinicalInterest);
		 d.append("sessionid", sessionId);
		 d.append("token", token);
		 d.append("bio", "");
		 d.append("password", "123456");
		 d.append("experience", experience);
		 d.append("medicalEducation", medicalEducation);
		 d.append("fellowships", fellowShips);
		 d.append("publications", publications);
		 d.append("awardsRecognition", awards);
		 d.append("otherAssociations", otherAssociations);
		 d.append("socialMedia", socialMedia);
		 d.append("address", address);
		 mongoOperation.insert(d, "doctor");
		 
 		 
		 
		 return "{\"success\":1}";
	}

	public HashMap<String,Object> listPatientsA() {
		 

		HashMap <String,Object> h = new HashMap <String,Object>();

		List<HashMap<String,String>> plist = new ArrayList<HashMap<String,String>>();
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	
		Query searchPatientsWaitingQuery = new Query(Criteria.where("chatStatus").is(22)
				); 

		
	//	searchPatientsWaitingQuery.with(new Sort(Sort.Direction.DESC, "chatStatus"));
	//	searchPatientsWaitingQuery.with(new Sort(Sort.Direction.DESC, "_id"));

		List<Enqueue> d = mongoOperation.find(searchPatientsWaitingQuery, Enqueue.class);

		Iterator<Enqueue> itr = d.iterator();

		
		
		while(itr.hasNext())
		{
			 
			
			HashMap <String,String> patientCover = new HashMap<String,String>();
			Enqueue e = itr.next();

			Patient p2 = getPatientDetails(e.getPatientId());
		
			 
			patientCover.put("patientName", p2.getFirstname() + " "+ p2.getLastname());
			patientCover.put("appointmentId",e.getId()); // Added Appointment Id for a granular control over chat
			patientCover.put("doctorName", getDoctorDetails(e.getDoctorId()).getDoctorName());
			patientCover.put("appointmentRequestedAt", e.getRequestDateAndTime().toString());
			
			plist.add(patientCover);
			patientCover = null;
		}
		h.put("patients", plist); 
		return h;

	}

	
		
	// Patient Block end
	
	//New method to send Notification Type 2_1
	//When patient expressesinterest
	
	public String sendPatientExpressedInterestNotification(String patientId, String doctorId) {

		DoctorForFCM d = getDoctorDetailsFCM(doctorId);
		HashMap <String,String> doctorMap = new HashMap<String,String>();
		
		doctorMap.put("sessionid", d.getSessionid());
		doctorMap.put("token", d.getToken());
		
		doctorMap.put("doctorName", d.getDoctorName());
		doctorMap.put("degree", d.getDegree());
		doctorMap.put("profilePic", d.getProfilePic());
		doctorMap.put("id", d.getId());

		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		
		Query searchPatientsWaitingQuery = new Query(Criteria.where("doctorId").is(doctorId)
				.and("chatStatus").in(listOfChatStatus)

				); 

		
		int noOfPatients = mongoOperation.find(searchPatientsWaitingQuery, Enqueue.class).size();

		Patient p = getPatientDetails(patientId);

		//String deviceId = p.getDeviceId();

		ArrayList<String> deviceIds = d.getFirebaseTokens();
		
		//Starts
		
		Body b = new Body();
		b.setNotificationType("2_1");
		b.doctor = doctorMap;
		b.setPatients(noOfPatients);
		
		HashMap<String, Object> data = new HashMap<>();
		data.put("body", b);
		
		notificationService.sendNotification(deviceIds, data);
		
		//ends
		
		
//
//		for(String deviceId : deviceIds)
//		{
//
//		FCMMessage fcmmessage = new FCMMessage();
//		fcmmessage.setTo(deviceId);
//		Body b = new Body();
//		b.doctor = doctorMap;
//		
//		//b.setDoctor(doctorMap);
//		b.setNotificationType("2_1");
//		b.setPatients(noOfPatients);
//		
//
//		HashMap <String,Body> hMap = new HashMap <String,Body>();
//		List<Body> bodies = new ArrayList<Body>();
//		bodies.add(b);
//		hMap.put("body", b);
//		fcmmessage.setData(hMap);
//
//		ObjectMapper mapper = new ObjectMapper();
//		String jsonInString=null;
//		try {
//			jsonInString = mapper.writeValueAsString(fcmmessage);
//		} catch (JsonProcessingException e1) {
//			
//			e1.printStackTrace();
//		}
//
//		System.out.println(jsonInString);
//		URLConnection connection;
//		try {
//			connection = new URL(
//					PropertyHotReloader.getProperty("fcm.url")
//					).openConnection();
//			connection.setDoOutput(true); // Triggers POST.
//			connection.setRequestProperty("Authorization", GOOGLEAPIKEY);
//			connection.setRequestProperty("Content-Type", 
//					PropertyHotReloader.getProperty("content.type")
//					);
//			OutputStream out = 	connection.getOutputStream();
//			 
//			out.write(jsonInString.getBytes(
//					PropertyHotReloader.getProperty("encoding")
//					));
//			out.close();
//
//			BufferedReader in = new BufferedReader(
//					new InputStreamReader(
//							connection.getInputStream()));
//			String decodedString;
//			while ((decodedString = in.readLine()) != null) {
//				System.out.println(decodedString);
//			}
//			in.close();
//
//		}	  
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		}
		return "{\"success\":1}";

	}

	private DoctorForFCM getDoctorDetailsFCM(String doctorId) {
		// TODO Auto-generated method stub
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		Query searchDoctor = new Query(Criteria.where("_id")
				.is(new ObjectId(doctorId)));
		return mongoOperation.findOne(searchDoctor, DoctorForFCM.class, "doctor");
	
		
	}

	public String doctorDoneForTheDay(String doctorId) {
		// TODO Auto-generated method stub

		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		// 1. Set Status Not Available

		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is( new ObjectId(doctorId)));
		
		
		
		Update update2 = new Update();
		update2.set("status", "Not Available");

		mongoOperation.updateFirst(query, update2, "doctor");

		
		// Remove all patients from the queue	

		Query query2 = new Query();
		query2.addCriteria(Criteria.where("doctorId")
				.is(doctorId)
				.and("chatStatus")
				.in(listOfChatStatus));
		Update update = new Update();
		List<Enqueue> l = mongoOperation.find(query2,Enqueue.class);
		Iterator itr = l.iterator();
		while(itr.hasNext())
		{
			Enqueue e = (Enqueue) itr.next();
			sendLogoutNotification(e.getPatientId(),e.getDoctorId(),e.getId());
		}

		update.set("chatStatus", DOCTORLEFTCHAT); // Doctor Left chat abruptly
		mongoOperation.updateMulti(query2, update, Enqueue.class);



		

		return "{\"responseCode\":\"success\",\"responseMessage\":\"\"}";
	}

	public HashMap<String, Object> getAppointmentStatus(String appointmentId) {
		// TODO Auto-generated method stub
		Enqueue appointment = getAppointmentDetails(appointmentId);
		String doctorId = appointment.getDoctorId();
		Doctor doctor = getDoctorDetails(doctorId);
		filteredUser f = new filteredUser();
		
		//System.out.println(tempD);
		String tempStatus = doctor.getStatus(); 
		f.setStatus(tempStatus);
		f.setDoctorName(doctor.getDoctorName());
		f.setDoctorId(doctorId);
		f.setDegree(doctor.getDegree());
		 
		f.setHospitalName(doctor.getHospitalName());
		f.setPublisherId(doctor.getPublisherid());

		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		 Query searchPatientsWaitingQuery = new Query(Criteria.where("doctorId")
				.is(doctorId)
				.and("chatStatus").in(listOfChatStatus)
				); 
		 
		 
		int noOfPatients = mongoOperation.find(searchPatientsWaitingQuery, 
				Enqueue.class).size();
		 

		if(tempStatus.equals("Not Available") || tempStatus.equals("Away"))
		{
			f.setEWT(-1);
		}
		else
		{
			f.setEWT(noOfPatients); 
		}
		f.setDocImg(doctor.getProfilePic());
		
		HashMap <String,Object> result= new HashMap<String,Object>();
		
		result.put("responseCode", "success");
		result.put("responseMessage", "");
		result.put("appointmentStatus", new Integer(appointment.getChatStatus()).toString());
		result.put("doctor", f);
		return result;
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

	public HashMap<String, Object> getWaitingPatients() {
		// TODO Auto-generated method stub

		HashMap <String,Object> h = new HashMap <String,Object>();

		List<HashMap<String,String>> plist = new ArrayList<HashMap<String,String>>();
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	
		Query searchPatientsWaitingQuery = new Query(Criteria.where("chatStatus").is(22)
				); 

		
//		searchPatientsWaitingQuery.with(new Sort(Sort.Direction.DESC, "chatStatus"));
//		searchPatientsWaitingQuery.with(new Sort(Sort.Direction.DESC, "_id"));

		List<Enqueue> d = mongoOperation.find(searchPatientsWaitingQuery, Enqueue.class);

		Iterator<Enqueue> itr = d.iterator();

		
		
		while(itr.hasNext())
		{
			 
			
			HashMap <String,String> patientCover = new HashMap<String,String>();
			Enqueue e = itr.next();

			Patient p2 = getPatientDetails(e.getPatientId());
		
			 
			patientCover.put("patientName", p2.getFirstname() + " "+ p2.getLastname());
			patientCover.put("appointmentId",e.getId()); // Added Appointment Id for a granular control over chat
			patientCover.put("doctorName", getDoctorDetails(e.getDoctorId()).getDoctorName());
			patientCover.put("appointmentRequestedAt", e.getRequestDateAndTime().toString());
			
			plist.add(patientCover);
			patientCover = null;
		}
		h.put("patients", plist); 
		return h;

	}

	public HashMap<String, Object> getChattingPatients() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub

		HashMap <String,Object> h = new HashMap <String,Object>();

		List<HashMap<String,String>> plist = new ArrayList<HashMap<String,String>>();
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	
		Query searchPatientsWaitingQuery = new Query(Criteria.where("chatStatus").is(33)
				); 

		
	//	searchPatientsWaitingQuery.with(new Sort(Sort.Direction.DESC, "chatStatus"));
	//	searchPatientsWaitingQuery.with(new Sort(Sort.Direction.DESC, "_id"));

		List<Enqueue> d = mongoOperation.find(searchPatientsWaitingQuery, Enqueue.class);

		Iterator<Enqueue> itr = d.iterator();

		
		
		while(itr.hasNext())
		{
			 
			
			HashMap <String,String> patientCover = new HashMap<String,String>();
			Enqueue e = itr.next();

			Patient p2 = getPatientDetails(e.getPatientId());
		
			 
			patientCover.put("patientName", p2.getFirstname() + " "+ p2.getLastname());
			patientCover.put("appointmentId",e.getId()); // Added Appointment Id for a granular control over chat
			patientCover.put("doctorName", getDoctorDetails(e.getDoctorId()).getDoctorName());
			patientCover.put("chatStartedAt", e.getChatStartedAt().toString());
			
			plist.add(patientCover);
			patientCover = null;
		}
		h.put("patients", plist); 
		return h;
	}

	public HashMap<String, Object> getAvailableDoctors() {
		// TODO Auto-generated method stub
		HashMap <String,Object> h = new HashMap <String,Object>();

		List<HashMap<String,String>> dlist = new ArrayList<HashMap<String,String>>();
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	
		Query searchAvailableDoctorsQuery = new Query(Criteria.where("status").is("Available")
				); 

		List<Doctor> d = mongoOperation.find(searchAvailableDoctorsQuery, Doctor.class);

		Iterator<Doctor> itr = d.iterator();

		
		
		while(itr.hasNext())
		{
			 
			
			HashMap <String,String> doctorCover = new HashMap<String,String>();
			Doctor doctor = itr.next();

		
			doctorCover.put("doctorName", doctor.getDoctorName());
			
			Query searchPatientsWaitingQuery = new Query(Criteria.where("doctorId")
					.is(doctor.getId())
					.and("chatStatus").in(listOfChatStatus)
					); 
			 
			 
			int noOfPatients = mongoOperation.find(searchPatientsWaitingQuery, 
					Enqueue.class).size();
			doctorCover.put("ewt",new Integer(noOfPatients).toString());
			dlist.add(doctorCover);
			doctorCover = null;
		}
		h.put("doctors", dlist); 
		return h;
	}

	public HashMap<String,Object> getReports(String patientId) {
		// TODO Auto-generated method stub
		Patient patient = getPatientDetails(patientId);
		ArrayList<Document> reports = patient.getReports();
		Collections.reverse(reports);
		HashMap<String,Object> reportMap = new HashMap<String,Object>();
		reportMap.put("responseCode", "success");
		reportMap.put("responseMessage", "");
		reportMap.put("reports", reports);
		return reportMap;
	}

	public List<Enqueue> getPastAppointments(String doctorId) {
		// TODO Auto-generated method stub
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		
		Query searchCompleteAppointmentQuery = new Query(Criteria.where("doctorId")
				.is(doctorId)
				.and("chatStatus").is(NORMALCOMPLETE)
				);
	
		
		return mongoOperation.find(searchCompleteAppointmentQuery, Enqueue.class);
		 
	}


	
	

}
