package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import utils.FCMMessage;
import utils.PropertyHotReloader;

@Service

public class NotificationService {
	
		
	private final String GOOGLEAPIKEY = "key=AIzaSyBYFAasqfwMYS9cLdbTVruKQckNcwXAm9Q";
	private final String FCM_URL = "http://fcm.googleapis.com/fcm/send";
	private final String CONTENT_TYPE = "application/json";
	private final String ENCODING = "UTF-8";
	
	
	public void sendNotification(ArrayList<String> sendTo, HashMap<String, Object> data){
	    try{
			sendFCMNotification(sendTo,data);
		}catch(Exception ex){
			
		}
	}
	
	private void sendFCMNotification(ArrayList<String> sendTo, HashMap<String, Object> data){
		
		FCMMessage fcmmessage = new FCMMessage();
		fcmmessage.setRegistration_ids(sendTo);
		fcmmessage.setData(data);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString="";
		try {
			jsonInString = mapper.writeValueAsString(fcmmessage);
		} catch (JsonProcessingException e1) {
		    e1.printStackTrace();
		}
		
		URLConnection connection;
		try {
			connection = new URL(FCM_URL).openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", GOOGLEAPIKEY);
			connection.setRequestProperty("Content-Type", CONTENT_TYPE);
			
			OutputStream out = 	connection.getOutputStream();
			out.write(jsonInString.getBytes(ENCODING));
			out.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String decodedString;
			while ((decodedString = in.readLine()) != null) {
				System.out.println(decodedString);
			}
			in.close();

		}	  
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
