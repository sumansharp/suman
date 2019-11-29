package service;

import java.util.HashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Service;

import dao.EfficenzMongoDao;

@Service

public class AppointmentService {
	
	ApplicationContext ctx; 
	
	

	public AppointmentService() {
		ctx = new GenericXmlApplicationContext("SpringConfig.xml"); 
	}



	public HashMap<String, Object> getAppointmentStatus(String appointmentId) {
		// TODO Auto-generated method stub
		return EfficenzMongoDao.getInstance().getAppointmentStatus(appointmentId);
	}

}
