
package controller;

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

import model.Doctor;
import service.DoctorService;
import service.NotificationService;
import utils.ConsultationResult;
import utils.DoctorComplete;
import utils.DoctorFacade;
import utils.DoctorProfile;
import utils.Keys;
import utils.PatientDoctorCombination;
import utils.PatientsinQueue;
import utils.filteredUser;


 
@RestController
@RequestMapping ("/doctors")

public class DoctorController {
@Autowired
DoctorService ds;


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
public String login(@RequestBody Doctor doctor /*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	
	
	return ds.login(doctor.getEmail(), doctor.getPassword());
 
}


@CrossOrigin(origins = "*")
@RequestMapping(value="/login2",method=RequestMethod.POST)
public String login2(@RequestBody Doctor doctor /*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	
	
	return ds.login2(doctor.getEmail(), doctor.getPassword());
 
}

 


/* @CrossOrigin(origins = "*")
@RequestMapping(value="/startchat",method=RequestMethod.POST)
*/
//public void startConsultation(@RequestBody PatientDoctorCombination pcb/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	 //ds.sendOpenTokParametersToPatient(pcb.getPatientId(),pcb.getDoctorId());
//}



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
@RequestMapping(value="/updateDoctor",method=RequestMethod.POST)
public String changeDoctorStatus(@RequestBody Doctor d/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	return ds.changeDoctorStatus(d.getId(),d.getStatus());
	
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
@RequestMapping(value="/addDoctor",method = RequestMethod.POST)
public String addDoctor (@RequestBody DoctorComplete doctorComplete  )
{
	
	return ds.addDoctor(doctorComplete);
}

}
