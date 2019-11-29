package service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import dao.EfficenzMongoDao;

@Service
public class DashboardService {

	public HashMap<String, Object> getWaitingPatients() {
		// TODO Auto-generated method stub
		return EfficenzMongoDao.getInstance().getWaitingPatients();
	}

	public HashMap<String, Object> getChattingPatients() {
		// TODO Auto-generated method stub
		return EfficenzMongoDao.getInstance().getChattingPatients();
	}

	public HashMap<String, Object> getAvailableDoctors() {
		// TODO Auto-generated method stub
		return EfficenzMongoDao.getInstance().getAvailableDoctors();
	}

	
}
