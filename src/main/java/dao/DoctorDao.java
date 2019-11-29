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

public class DoctorDao {
	@Autowired
	private ApplicationContext ctx ;
	
	@Autowired
	private NotificationService notificationService;
	
	private DoctorDao(){
		ctx = new GenericXmlApplicationContext("SpringConfig.xml");
		notificationService = new NotificationService();
	}

	private static class SingletonHelper{
		private static final DoctorDao INSTANCE = new DoctorDao();
	}

	public static DoctorDao getInstance(){
		return SingletonHelper.INSTANCE;
	}

 	private final int DOCTORLEFTCHAT = 88;
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
		Patient p = PatientDao.getInstance().getPatientDetails(patientId);

		Body b = new Body();
		b.doctor = doctorMap;

	//	b.setDoctor(doctorMap);
		b.setNotificationType("3");
		b.setTitle("Appointment Update");
		b.setMessage("Dr had to leave on emergency. Please select next available doctor.");
		b.setAppointmentId(appointmentId);


		HashMap <String,Object> hMap = new HashMap <String,Object>();
		hMap.put("body", b);
	
		ArrayList<String> sendTo = new ArrayList<>();
		sendTo.add(p.getDeviceId());
		
		notificationService.sendNotification(sendTo, hMap);


		return "{\"success\":1}";

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
		
	HashMap <String,String> doctorMap = new HashMap<String,String>();
		
		doctorMap.put("sessionid", d.getSessionid());
		doctorMap.put("token", d.getToken());
		
		doctorMap.put("doctorName", d.getDoctorName());
		doctorMap.put("degree", d.getDegree());
		doctorMap.put("profilePic", d.getProfilePic());
		doctorMap.put("id", d.getId());

		Patient p = PatientDao.getInstance().getPatientDetails(patientId);
		
		
		//System.out.println(appointmentQuery.toString());
		Enqueue enqueue = AppointmentDao.getInstance().getOngoingAppointmentDetails(doctorId,patientId);
		//System.out.println(enqueue.toString());
		String appointmentId = enqueue.getId();
	 	
		Body b = new Body();
		b.doctor = doctorMap;

		//b.setDoctor(doctorMap);
		b.setNotificationType("2");
		b.setTitle("Done");
		b.setMessage("Thanks");
		b.setAppointmentId(appointmentId);
		
		
		HashMap <String,Object> hMap = new HashMap <String,Object>();
		hMap.put("body", b);
		
		ArrayList<String> sendTo = new ArrayList<>();
		sendTo.add(p.getDeviceId());
		
		notificationService.sendNotification(sendTo, hMap);

		
		
		return "{\"success\":1}";
	 
	}


 
	//generic method to get all details of a Doctor
	public Doctor getDoctorDetails(String doctorId)
	{
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		Query searchDoctor = new Query(Criteria.where("_id")
				.is(new ObjectId(doctorId)));
		return mongoOperation.findOne(searchDoctor, Doctor.class);

	}
	
	//Method to return metadata about doctor
	//When user clicks on read more button
	//on doctorlist
	public DoctorProfile getDoctor2(String doctorId) {
	
		
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

		DoctorProfile doctorProfile = mongoOperation.findById(new ObjectId(doctorId),DoctorProfile.class,"doctor");
		
		return doctorProfile;
		
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

 	
 
	public DoctorForFCM getDoctorDetailsFCM(String doctorId) {
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

 
	
	

}
