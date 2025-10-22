package com.infnet.enrollmentmodule.service;

import com.infnet.enrollmentmodule.client.StudentClient;
import com.infnet.enrollmentmodule.dto.EnrollmentRequestDTO;
import com.infnet.enrollmentmodule.dto.EnrollmentResponseDTO;
import com.infnet.enrollmentmodule.dto.StudentDTO;
import com.infnet.enrollmentmodule.global.exception.EnrollmentNotFoundException;
import com.infnet.enrollmentmodule.entity.Enrollment;
import com.infnet.enrollmentmodule.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentClient studentClient;

    @Override
    @Transactional
    public EnrollmentResponseDTO createEnrollment(EnrollmentRequestDTO requestDTO) {
        log.info("Creating enrollment for student ID: {}", requestDTO.getStudentId());

        StudentDTO student = studentClient.getStudentById(requestDTO.getStudentId());

        if (!student.getActive()) {
            throw new IllegalArgumentException("Cannot enroll inactive student with ID: " + student.getId());
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(requestDTO.getStudentId());
        enrollment.setCourseName(requestDTO.getCourseName());
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus("ACTIVE");

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        log.info("Enrollment created successfully with ID: {}", savedEnrollment.getId());

        return convertToResponseDTO(savedEnrollment, student);
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponseDTO getEnrollmentById(Long id) {
        log.info("Fetching enrollment with ID: {}", id);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with ID: " + id));

        StudentDTO student = studentClient.getStudentById(enrollment.getStudentId());

        return convertToResponseDTO(enrollment, student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getAllEnrollments() {
        log.info("Fetching all enrollments");

        List<Enrollment> enrollments = enrollmentRepository.findAll();

        return enrollments.stream()
                .map(enrollment -> {
                    StudentDTO student = studentClient.getStudentById(enrollment.getStudentId());
                    return convertToResponseDTO(enrollment, student);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getEnrollmentsByStudentId(Long studentId) {
        log.info("Fetching enrollments for student ID: {}", studentId);

        StudentDTO student = studentClient.getStudentById(studentId);

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        return enrollments.stream()
                .map(enrollment -> convertToResponseDTO(enrollment, student))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EnrollmentResponseDTO updateEnrollment(Long id, EnrollmentRequestDTO requestDTO) {
        log.info("Updating enrollment with ID: {}", id);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with ID: " + id));

        StudentDTO student = studentClient.getStudentById(requestDTO.getStudentId());

        if (!student.getActive()) {
            throw new IllegalArgumentException("Cannot update enrollment with inactive student");
        }

        enrollment.setStudentId(requestDTO.getStudentId());
        enrollment.setCourseName(requestDTO.getCourseName());

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        log.info("Enrollment updated successfully with ID: {}", updatedEnrollment.getId());

        return convertToResponseDTO(updatedEnrollment, student);
    }

    @Override
    @Transactional
    public void deleteEnrollment(Long id) {
        log.info("Deleting enrollment with ID: {}", id);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with ID: " + id));

        enrollmentRepository.delete(enrollment);

        log.info("Enrollment deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional
    public EnrollmentResponseDTO cancelEnrollment(Long id) {
        log.info("Cancelling enrollment with ID: {}", id);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with ID: " + id));

        enrollment.setStatus("CANCELLED");

        Enrollment cancelledEnrollment = enrollmentRepository.save(enrollment);

        log.info("Enrollment cancelled successfully with ID: {}", cancelledEnrollment.getId());

        StudentDTO student = studentClient.getStudentById(enrollment.getStudentId());

        return convertToResponseDTO(cancelledEnrollment, student);
    }

    private EnrollmentResponseDTO convertToResponseDTO(Enrollment enrollment, StudentDTO student) {
        return new EnrollmentResponseDTO(
                enrollment.getId(),
                enrollment.getStudentId(),
                student.getName(),
                enrollment.getCourseName(),
                enrollment.getEnrollmentDate(),
                enrollment.getStatus()
        );
    }

}
