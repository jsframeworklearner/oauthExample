package com.example.oauthproject.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oauthproject.entity.PatientIdentifier;
public interface PatientIdentifierRepository extends JpaRepository<PatientIdentifier, Long> {
	public PatientIdentifier findByValueAndType(String mrn, String type);
	public PatientIdentifier findById(String id);
	public List<PatientIdentifier> findByTypeAndPatientIdId(String Type , String patientId);
    public List<PatientIdentifier> findByTypeAndPatientIdIdAndStatus(String type , String patientId,String status);
	public PatientIdentifier findByTypeAndPatientIdIdAndValue(String type , String patientId,String value);
	public List<PatientIdentifier> findByTypeAndPatientIdIdAndAssignerId(String Type , String patientId, String organizationId);
	public PatientIdentifier findByTypeAndPatientIdIdAndAssignerIdAndStatus(String Type , String patientId, String organizationId, String status);
	public PatientIdentifier findByPatientIdIdAndType(String patientId, String Type);
}