package com.atharva.patientservice.mapper;

import com.atharva.patientservice.dto.PatientRequestDTO;
import com.atharva.patientservice.dto.PatientResponseDTO;
import com.atharva.patientservice.model.Patient;

public class PatientMapper {
    public static PatientResponseDTO toDTO(Patient patient) {
        PatientResponseDTO PatientDTO = new PatientResponseDTO();
        PatientDTO.setId(patient.getId().toString());
        PatientDTO.setName(patient.getName());
        PatientDTO.setEmail(patient.getEmail());
        PatientDTO.setAddress(patient.getAddress());
        PatientDTO.setDateOfBirth(patient.getDateOfBirth().toString());
        PatientDTO.setRegisteredDate(patient.getRegisteredDate().toString());
        return PatientDTO;
    }

    public static Patient toModel(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(java.time.LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegisteredDate(java.time.LocalDate.parse(patientRequestDTO.getRegisteredDate()));
        return patient;
    }
}
