package controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dao.EfficenzMongoDao;
import service.DashboardService;

@RestController
@RequestMapping ("/dashboard")
public class DashboardController {
@Autowired
DashboardService ds;

@CrossOrigin(origins = "*")
@RequestMapping("/waiting")
public HashMap<String,Object> waitingPatients(/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	
	return ds.getWaitingPatients();
	
 }


@CrossOrigin(origins = "*")
@RequestMapping("/chatting")
public HashMap<String,Object> chattingPatients(/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	
	return ds.getChattingPatients();
	
 }

@CrossOrigin(origins = "*")
@RequestMapping("/availabledoctors")
public HashMap<String,Object> availableDoctors(/*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {
 	
	return ds.getAvailableDoctors();
	
 }


}
