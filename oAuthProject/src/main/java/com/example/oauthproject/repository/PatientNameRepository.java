package com.example.oauthproject.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oauthproject.entity.PatientName;
public interface PatientNameRepository extends JpaRepository<PatientName, Long> {
	public PatientName findById(String id);
}