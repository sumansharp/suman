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

public class AppointmentDao {
	@Autowired
	private ApplicationContext ctx ;
	
	@Autowired
	private NotificationService notificationService;
	
	private AppointmentDao(){
		ctx = new GenericXmlApplicationContext("SpringConfig.xml");
		notificationService = new NotificationService();
	}

	private static class SingletonHelper{
		private static final AppointmentDao INSTANCE = new AppointmentDao();
	}

	public static AppointmentDao getInstance(){
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
	 
 
	public HashMap<String, String> sendOpenTokParametersToPatient(String patientId, 
			String doctorId, String appointmentId) {


		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		DoctorForFCM d = DoctorDao.getInstance().getDoctorDetailsFCM(doctorId);
		
		HashMap <String,String> doctorMap = new HashMap<String,String>();
		
		doctorMap.put("sessionid", d.getSessionid());
		doctorMap.put("token", d.getToken());
		
		doctorMap.put("doctorName", d.getDoctorName());
		doctorMap.put("degree", d.getDegree());
		doctorMap.put("profilePic", d.getProfilePic());
		doctorMap.put("id", d.getId());

		
		Patient p = PatientDao.getInstance().getPatientDetails(patientId);
	
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

		//b.setDoctor(doctorMap);
		
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
		List<Body> bodies = new ArrayList<Body>();
		bodies.add(b);
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

 	public String sendLogoutNotification(String patientId, String doctorId, String appointmentId) {

		DoctorForFCM d = DoctorDao.getInstance().getDoctorDetailsFCM(doctorId);
		
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
		Patient p = PatientDao.getInstance().getPatientDetails(patientId);

		Body b = new Body();
		b.doctor = doctorMap;

	//	b.setDoctor(doctorMap);
		b.setNotificationType("3");
		b.setTitle("Appointment Update");
		b.setMessage("Dr had to leave on emergency. Please select next available doctor.");
		b.setAppointmentId(appointmentId);


		HashMap <String,Object> hMap = new HashMap <String,Object>();
		List<Body> bodies = new ArrayList<Body>();
		bodies.add(b);
		hMap.put("body", b);
		
		ArrayList<String> sendTo = new ArrayList<>();
		sendTo.add(p.getDeviceId());
		
		notificationService.sendNotification(sendTo, hMap);

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

			Patient p2 = PatientDao.getInstance().getPatientDetails(e.getPatientId());
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
			
			
			Patient p2 = PatientDao.getInstance().getPatientDetails(e.getPatientId());
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
			patientCover.put("reports", p2.getReports());
			plist.add(patientCover);
			p = null;
		}
		h.put("appointments", plist);
		h.put("responseCode", "success");
		h.put("responseMessage", "");
		return h;

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

			Patient p2 = PatientDao.getInstance().getPatientDetails(e.getPatientId());
		
			 
			patientCover.put("patientName", p2.getFirstname() + " "+ p2.getLastname());
			patientCover.put("appointmentId",e.getId()); // Added Appointment Id for a granular control over chat
			patientCover.put("doctorName", DoctorDao.getInstance().getDoctorDetails(e.getDoctorId()).getDoctorName());
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

		DoctorForFCM d = DoctorDao.getInstance().getDoctorDetailsFCM(doctorId);
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

		ArrayList<String> deviceIds = d.getFirebaseTokens();

		ArrayList<String> sendTo = new ArrayList<>();
		
		for(String deviceId : deviceIds)
		{
			sendTo.add(deviceId);
		}
		
		Body b = new Body();
		b.doctor = doctorMap;
		b.setNotificationType("2_1");
		b.setPatients(noOfPatients);

		HashMap <String,Object> hMap = new HashMap <String,Object>();
		hMap.put("body", b);
		
		notificationService.sendNotification(sendTo, hMap);
		
		return "{\"success\":1}";

	}

 	public HashMap<String, Object> getAppointmentStatus(String appointmentId) {
		// TODO Auto-generated method stub
		Enqueue appointment = getAppointmentDetails(appointmentId);
		String doctorId = appointment.getDoctorId();
		Doctor doctor = DoctorDao.getInstance().getDoctorDetails(doctorId);
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

 
	public HashMap<String, Object> getWaitingPatients() {
		// TODO Auto-generated method stub

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

			Patient p2 = PatientDao.getInstance().getPatientDetails(e.getPatientId());
		
			 
			patientCover.put("patientName", p2.getFirstname() + " "+ p2.getLastname());
			patientCover.put("appointmentId",e.getId()); // Added Appointment Id for a granular control over chat
			patientCover.put("doctorName", DoctorDao.getInstance().getDoctorDetails(e.getDoctorId()).getDoctorName());
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

			Patient p2 = PatientDao.getInstance().getPatientDetails(e.getPatientId());
		
			 
			patientCover.put("patientName", p2.getFirstname() + " "+ p2.getLastname());
			patientCover.put("appointmentId",e.getId()); // Added Appointment Id for a granular control over chat
			patientCover.put("doctorName", DoctorDao.getInstance().getDoctorDetails(e.getDoctorId()).getDoctorName());
			patientCover.put("chatStartedAt", e.getChatStartedAt().toString());
			
			plist.add(patientCover);
			patientCover = null;
		}
		h.put("patients", plist); 
		return h;
	}

 
}
