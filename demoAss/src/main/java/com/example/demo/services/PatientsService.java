package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Doctor;
import com.example.demo.model.Patients;
import com.example.demo.model.Schedules;
import com.example.demo.model.User;
import com.example.demo.modelDTO.SchedulesDTO;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientsRepository;

@Service
public class PatientsService {
	@Autowired
	PatientsRepository patientsRepository;
	
	@Autowired
	DoctorRepository doctorRepository;
	
	@Autowired
	SchedulesService schedulesService;
	
	@Autowired
	DoctorService doctorService;

//	danh sach benh nhan theo doctorId
	public List<Patients> patientsbyDoctor(int doctorId) {
		List<Patients> list = new ArrayList<Patients>();
		List<Schedules> lSchedules = schedulesService.listByDoctorId(doctorId);
		if (lSchedules.size() > 0) {
			for (Schedules schedules : lSchedules) {
				list.add(schedules.getPatients());
			}
		}
		return list;
	}

	public Patients getPatientsById(int id) {
		Patients patients = patientsRepository.findById(id).get();
		return patients;
	}
	public Patients getPatientsByUser(User user) {
		Patients p = new Patients();
		List<Patients> list = patientsRepository.findAll();
		for (Patients patients2 : list) {
			if(patients2.getUser().getId()==user.getId()) {
				p = patients2;
				break;
			}
		}
		return p;
	}
//	add schedule
	public void addSchedules(SchedulesDTO schedulesDTO) {
		Schedules schedules = new Schedules(schedulesDTO);
		Patients patients = getPatientsById(schedulesDTO.getPatientsId());
		schedules.setPatients(patients);
		schedulesService.save(schedules);
		Doctor doctor = doctorService.findById(schedulesDTO.getDoctorId());
		doctor.getSchedules().add(schedules);
		doctorService.update(doctor);
		
	}
}
