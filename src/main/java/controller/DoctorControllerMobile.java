
package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import model.Doctor;
import service.DoctorServiceMobile;
import utils.ConsultationResult;
import utils.DoctorFacade;
import utils.DoctorProfile;
import utils.Keys;
import utils.PatientDoctorCombination;
import utils.PatientsinQueue;
import utils.PropertyHotReloader;
import utils.filteredUser;


 
@RestController
@RequestMapping ("/doctorsm")

public class DoctorControllerMobile {
@Autowired
DoctorServiceMobile ds;

/*@CrossOrigin(origins = "*")
@RequestMapping("{name}")
public chatDoctor getDoctor(@PathVariable("name") String name @RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey ) {
	 
	chatDoctor d = null;
	
	
	d = ds.getDoctor(name);
	
	
	return d;
}*/
 
/*@CrossOrigin(origins = "*")
@RequestMapping(value="/createdoctor",method=RequestMethod.POST)
public String createdoctor (@RequestParam final String doctorName, 
		@RequestParam final String gender, 
		@RequestParam final String email,
		@RequestParam final String dateofbirth,
		@RequestParam final String degree,@RequestHeader( value="user")  String user,@RequestHeader( value="passkey" )  String passkey ) {
	return ds.addDoctor(doctorName, gender, email,dateofbirth,degree);
	}
*/
@CrossOrigin(origins = "*")
@RequestMapping("/dlist")
public HashMap<String,List<filteredUser>> getDList() { 
	return ds.getDocList();
	
	
}



@CrossOrigin(origins = "*")
@RequestMapping(value="/login",method=RequestMethod.POST)
public HashMap<String, Object> login(@RequestParam final String email,final String password /*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	
	
	return ds.loginM(email, password);
 
}


@CrossOrigin(origins = "*")
@RequestMapping(value="/login2",method=RequestMethod.POST)
public String login2(@RequestBody Doctor doctor /*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	
	
	return ds.login2(doctor.getEmail(), doctor.getPassword());
 
}

 


@CrossOrigin(origins = "*")
@RequestMapping(value="/startVideoChat",method=RequestMethod.POST)
public HashMap<String,String> startConsultation(@RequestParam final String patientId, @RequestParam final String doctorId,@RequestParam final String appointmentId /*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	 return ds.sendOpenTokParametersToPatient(patientId,doctorId,appointmentId);
}


@CrossOrigin(origins = "*")
@RequestMapping("/keys/{docId}")
public List<Keys> getKeys(@PathVariable("docId") String docId  /*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	
	return ds.getKeys2(docId);
}



@CrossOrigin(origins = "*")
@RequestMapping(value="/expressinterest",method=RequestMethod.POST)
public String expressInterest(@RequestParam final String patientId,final String doctorId,@RequestParam (value="previousAppointmentId", defaultValue="NA") final String previousAppointmentId /*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	return ds.addToQueue(patientId,doctorId,previousAppointmentId);
	
 }




@CrossOrigin(origins = "*")
@RequestMapping("/patientlist/{doctorId}")
public List<PatientsinQueue> patientList(@PathVariable("doctorId") String doctorId/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	return ds.listPatients(doctorId);
	
 }

@CrossOrigin(origins = "*")
@RequestMapping("/patientlistm/{doctorId}")
public HashMap<String,Object> patientListM(@PathVariable("doctorId") String doctorId/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	return ds.listPatientsM(doctorId);
	
 }



@CrossOrigin(origins = "*")
@RequestMapping(value="/changedoctor",method=RequestMethod.POST)
public String changeDoctor(@RequestParam final String appointmentId,final String doctorId/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	return ds.changeDoctor(appointmentId,doctorId);
	
 }


@CrossOrigin(origins = "*")
@RequestMapping(value="/logout/{doctorId}")
public String logout(@PathVariable  ("doctorId") String doctorId /*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {

	return ds.logout(doctorId);
 
}


@CrossOrigin(origins = "*")
@RequestMapping(value="/leavequeue",method=RequestMethod.POST)
public String leaveQueue(@RequestParam final String appointmentId/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	return ds.leaveQueue(appointmentId);
	
 }

//Removed chatend as this is being covered in updateAppointmentStatus
/*@CrossOrigin(origins = "*")
@RequestMapping(value="/chatend",method=RequestMethod.POST)
public String chatEnd(@RequestParam final String appointmentId,@RequestParam final int endReason@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey ) {
 	return ds.chatEnd(appointmentId,endReason);
	
 }
*/

@CrossOrigin(origins = "*")
@RequestMapping(value="/chatstart",method=RequestMethod.POST)
public String chatStart(@RequestParam final String appointmentId/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	return ds.chatStart(appointmentId);
	
 }

@CrossOrigin(origins = "*")
@RequestMapping(value="/updateAppointment",method=RequestMethod.POST)
public String changeAppointmentStatus(@RequestParam final String appointmentId,
		@RequestParam final int statusCode/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	return ds.changeAppointmentStatus(appointmentId,statusCode);
	
 }


@CrossOrigin(origins = "*")
@RequestMapping(value="/updateDoctorStatus",method=RequestMethod.POST)
public String changeDoctorStatusM(@RequestParam final String doctorId, @RequestParam final boolean available/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	return ds.changeDoctorStatusM(doctorId,available);
	
 }


@CrossOrigin(origins = "*")
@RequestMapping(value="/detailsd/{doctorId}")
public DoctorFacade getDoctor(@PathVariable  ("doctorId") String doctorId /*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
	return ds.getDoctor(doctorId);
 
}

@CrossOrigin(origins = "*")
@RequestMapping(value="/sendkillsignal",method=RequestMethod.POST)
public String sendKillSignal(@RequestBody PatientDoctorCombination pcb/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	return ds.KillOpenTok(pcb.getPatientId(),pcb.getDoctorId());
}

@CrossOrigin(origins = "*")
@RequestMapping(value="/endconsultation",method=RequestMethod.POST)
public String endConsultation(@RequestBody ConsultationResult cresult/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	return ds.endConsultation(cresult.getAppointmentId(),cresult.getChatText(),cresult.getChatStatus());
}


@CrossOrigin(origins = "*")
@RequestMapping(value="/details2/{doctorId}")
public DoctorProfile getDoctor2(@PathVariable  ("doctorId") String doctorId /*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {

	
	
	return ds.getDoctor2(doctorId);
 
}

@CrossOrigin(origins = "*")
@RequestMapping(value="/updateFirebaseToken",method=RequestMethod.POST)
public String updateFirebaseToken(@RequestParam final String doctorId, @RequestParam final String firebaseToken/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	return ds.updateFirebaseToken(doctorId, firebaseToken);
	
 }

@CrossOrigin(origins="*")
@RequestMapping(value="/uploadDoctorImage",method=RequestMethod.POST)
public String uploadDoctorImage(@RequestParam(name="file", required=false) MultipartFile file) {
	return ds.uploadDoctorImage(file);
	
	//return null;
}

@CrossOrigin(origins="*")
@RequestMapping(value="/waitingpatients",method=RequestMethod.GET)
public HashMap<String,Object> waitingPatients() {
	return ds.listPatientsA();
	
	//return null;
}

@CrossOrigin(origins = "*")
@RequestMapping(value="/doctorDoneForTheDay",method=RequestMethod.POST)
public String doctorDoneForTheDay(@RequestParam final String doctorId /*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {

	return ds.doctorDoneForTheDay(doctorId);
 
}

@CrossOrigin(origins = "*")
@RequestMapping("/pastAppointments/{doctorId}")
public HashMap<String,Object> getPastAppointments(@PathVariable  ("doctorId") String doctorId ) { 
	return ds.getPastAppointments(doctorId);
	
	
}





}

