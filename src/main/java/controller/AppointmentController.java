package controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import service.AppointmentService;


 
@RestController
@RequestMapping ("/appointments")
public class AppointmentController {
	@Autowired
	AppointmentService as;
	
	@CrossOrigin(origins = "*")
	@RequestMapping("/appointmentStatus/{appointmentId}")
	public HashMap<String,Object> appointmentStatus(@PathVariable("appointmentId") String appointmentId/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
	 	
		return as.getAppointmentStatus(appointmentId);
	 }

}
