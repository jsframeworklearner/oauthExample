package com.example.oauthproject.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oauthproject.entity.Patient;
public interface PatientRepository extends JpaRepository<Patient, Long> {
	public Patient findById(String id);
}