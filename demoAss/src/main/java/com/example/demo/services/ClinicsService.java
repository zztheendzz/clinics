package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Clinics;
import com.example.demo.repository.ClinicsRepository;

@Service
public class ClinicsService {
	@Autowired 
	private ClinicsRepository clinicsRepository;
	public List<Clinics> clinics() {
		List<Clinics> clinics = clinicsRepository.findAll();
		return clinics;
	}
	
	public Clinics findByIdClinics(int id) {
		Clinics clinics = clinicsRepository.findById(id).get();
		return  clinics;
	}
}
