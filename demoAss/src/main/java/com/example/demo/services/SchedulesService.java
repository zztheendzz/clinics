package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Doctor;
import com.example.demo.model.Patients;
import com.example.demo.model.Schedules;
import com.example.demo.modelDTO.SchedulesDTO;
import com.example.demo.repository.SchedulesRepository;

@Service
public class SchedulesService {
	@Autowired
	SchedulesRepository schedulesRepository;
	
	@Autowired
	DoctorService doctorService;
	
//	@Autowired
//	PatientsService patientsService;
	
	
	public List<Schedules> listByDoctorId(int doctorId) {
		Doctor doctor = doctorService.findById(doctorId);
		 List<Schedules>  list= doctor.getSchedules();
		return list;	
	}
//	xac nhan lich kham voi benh nhan
	public void confirm( int status,String description, int schedulesId) {
		schedulesRepository.StatusAndDescriptionAndId(status, description, schedulesId);
	}

	public void save(Schedules schedules) {
		schedulesRepository.save(schedules);
	}
	public void addSchedules(SchedulesDTO schedulesDTO) {
//		Schedules schedules = new Schedules(schedulesDTO);
//		Patients patients = patientsService.getPatientsById(schedulesDTO.getPatientsId());
//		schedules.setPatients(patients);
//		Doctor doctor = doctorService.findById(schedulesDTO.getDoctorId());
//		doctor.getSchedules().add(schedules);
	}
}
