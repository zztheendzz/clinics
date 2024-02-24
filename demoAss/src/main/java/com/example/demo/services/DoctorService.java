package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.example.demo.model.Doctor;
import com.example.demo.model.Generic;
import com.example.demo.model.Specializations;
import com.example.demo.model.User;
import com.example.demo.repository.DoctorRepository;

@Service
public class DoctorService {
	@Autowired
	private DoctorRepository doctorRepository;
	@Autowired
	private SpecializationsService specializationsService;
	
	public Doctor findById(int id) {
		Optional<Doctor> doctor = doctorRepository.findById(id);
		Doctor doctor2 = doctor.get();
		return doctor2;
	}
//	list doctor by status
	public List<Doctor>findByStatus(int status) {
		List<Doctor> list = doctorRepository.findByStatus(status);
		return list;
	}
//	delete doctor : set status =0
	public void setStatusDoctor(int status, int id ) {
		doctorRepository.updateDoctorById(status, id);
	}
//	add doctor
	public void addDoctor(Doctor doctor ) {
		doctorRepository.save(doctor);
	}
//	0: search theo chuyên khoa specializations
//	1: search theo Khu vực
//	2: search theo Danh mục (theo tình hình bệnh lý)
//	3: search theo Mức giá
//	4: search theo Cơ sở y tế
	public List<Doctor> findByspecializations(String keySearch) {
		List<Doctor> list = new ArrayList<Doctor>();
		Specializations speci = new Specializations();
		Search search = new Search();
		List<Specializations> specializations =  specializationsService.findAlList();
		
		Double result = 0.0;
		
		for (Specializations specializations2 : specializations) {
			Double tempDouble =search.DSearch(keySearch, specializations2.getName());
			if(tempDouble>result) {
				result = tempDouble;
				speci = specializations2;
			}
		}
		
		list = speci.getDoctors();
		return list;
	}
//	them bac si
	public void save(Doctor doctor) {
		doctorRepository.save(doctor);
		
	}
//	find doctor from user
	public Doctor doctorFromUser(int idUser) {
		List<Doctor> list = doctorRepository.findAll();
		Doctor doctor = new Doctor();
		for (Doctor d : list) {
			if(d.getUser().getId()==idUser) {
				doctor = d;
				break;
			}
		}
		return doctor;
	}
//	update doctor
	public void update(Doctor doctor) {
		doctorRepository.saveAndFlush(doctor);
	}

}
