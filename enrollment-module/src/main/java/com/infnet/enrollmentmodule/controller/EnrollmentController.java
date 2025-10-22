package com.infnet.enrollmentmodule.controller;

import com.infnet.enrollmentmodule.dto.EnrollmentRequestDTO;
import com.infnet.enrollmentmodule.dto.EnrollmentResponseDTO;
import com.infnet.enrollmentmodule.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
@Slf4j
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<EnrollmentResponseDTO> createEnrollment(@Valid @RequestBody EnrollmentRequestDTO requestDTO) {
        log.info("POST /enrollments - Creating enrollment");
        EnrollmentResponseDTO response = enrollmentService.createEnrollment(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDTO> getEnrollmentById(@PathVariable Long id) {
        log.info("GET /enrollments/{} - Fetching enrollment", id);
        EnrollmentResponseDTO response = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDTO>> getAllEnrollments() {
        log.info("GET /enrollments - Fetching all enrollments");
        List<EnrollmentResponseDTO> response = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponseDTO>> getEnrollmentsByStudentId(@PathVariable Long studentId) {
        log.info("GET /enrollments/student/{} - Fetching enrollments for student", studentId);
        List<EnrollmentResponseDTO> response = enrollmentService.getEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDTO> updateEnrollment(
            @PathVariable Long id,
            @Valid @RequestBody EnrollmentRequestDTO requestDTO) {
        log.info("PUT /enrollments/{} - Updating enrollment", id);
        EnrollmentResponseDTO response = enrollmentService.updateEnrollment(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<EnrollmentResponseDTO> cancelEnrollment(@PathVariable Long id) {
        log.info("PATCH /enrollments/{}/cancel - Cancelling enrollment", id);
        EnrollmentResponseDTO response = enrollmentService.cancelEnrollment(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        log.info("DELETE /enrollments/{} - Deleting enrollment", id);
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

}