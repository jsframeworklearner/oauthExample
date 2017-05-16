package com.example.oauthproject.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oauthproject.entity.PatientAddress;
public interface PatientAddressRepository extends JpaRepository<PatientAddress, Long> {
	public PatientAddress findById (String id);
}