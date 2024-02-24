package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Clinics;
import com.example.demo.model.Posts;

@Repository
	public interface ClinicsRepository  extends JpaRepository<Clinics, Integer> {

	@Query("SELECT u FROM Clinics u WHERE u.id = :id")
	Optional<Clinics> findById(@Param("id") int id);
	
}
