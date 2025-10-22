package com.infnet.enrollmentmodule.service;

import com.infnet.enrollmentmodule.dto.EnrollmentRequestDTO;
import com.infnet.enrollmentmodule.dto.EnrollmentResponseDTO;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponseDTO createEnrollment(EnrollmentRequestDTO requestDTO);

    EnrollmentResponseDTO getEnrollmentById(Long id);

    List<EnrollmentResponseDTO> getAllEnrollments();

    List<EnrollmentResponseDTO> getEnrollmentsByStudentId(Long studentId);

    EnrollmentResponseDTO updateEnrollment(Long id, EnrollmentRequestDTO requestDTO);

    void deleteEnrollment(Long id);

    EnrollmentResponseDTO cancelEnrollment(Long id);

}
