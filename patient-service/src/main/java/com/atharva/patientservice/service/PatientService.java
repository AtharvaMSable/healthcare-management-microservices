package com.atharva.patientservice.service;

import com.atharva.patientservice.dto.PatientRequestDTO;
import com.atharva.patientservice.dto.PatientResponseDTO;
import com.atharva.patientservice.exception.EmailAlreadyExistsException;
import com.atharva.patientservice.exception.PatientNoFoundException;
import com.atharva.patientservice.grpc.BillingServiceGrpcClient;
import com.atharva.patientservice.kafka.KafkaProducer;
import com.atharva.patientservice.mapper.PatientMapper;
import com.atharva.patientservice.model.Patient;
import com.atharva.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
public class PatientService {

    // Dependency Injection
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;


    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        List<PatientResponseDTO> patientResponseDTOs = patients.stream().map(patient -> PatientMapper.toDTO(patient)).toList();

        return patientResponseDTOs;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Patient with email " + patientRequestDTO.getEmail() + " already exists.");
        }

        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());

        kafkaProducer.sendEvent(newPatient);

        return PatientMapper.toDTO(newPatient);
    }

    //updating patient details can be added here
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {

        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNoFoundException("Patient not found eith ID: " + id));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) { // Check if email is taken by another patient
            throw new EmailAlreadyExistsException("Patient with email " + patientRequestDTO.getEmail() + " already exists.");
        }

        patient.setName(patientRequestDTO.getName()); // Update name
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

    Patient updatedPatient = patientRepository.save(patient); // Save updated patient
        return PatientMapper.toDTO(updatedPatient); // Convert to DTO and return
    }

    // Delete patient by ID
    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}