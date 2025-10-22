package com.infnet.studentmodule.service;

import com.infnet.studentmodule.dto.StudentRequestDTO;
import com.infnet.studentmodule.dto.StudentResponseDTO;
import com.infnet.studentmodule.entity.Student;
import com.infnet.studentmodule.global.exception.StudentNotFoundException;
import com.infnet.studentmodule.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) {
        log.info("Creating student with email: {}", requestDTO.getEmail());

        if (studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + requestDTO.getEmail());
        }

        if (studentRepository.existsByCpf(requestDTO.getCpf())) {
            throw new IllegalArgumentException("CPF already exists: " + requestDTO.getCpf());
        }

        Student student = new Student();
        student.setName(requestDTO.getName());
        student.setEmail(requestDTO.getEmail());
        student.setCpf(requestDTO.getCpf());
        student.setBirthDate(requestDTO.getBirthDate());
        student.setActive(true);

        Student savedStudent = studentRepository.save(student);

        log.info("Student created successfully with ID: {}", savedStudent.getId());

        return convertToResponseDTO(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long id) {
        log.info("Fetching student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));

        return convertToResponseDTO(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {
        log.info("Fetching all students");

        List<Student> students = studentRepository.findAll();

        return students.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO) {
        log.info("Updating student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));

        if (!student.getEmail().equals(requestDTO.getEmail())) {
            if (studentRepository.existsByEmail(requestDTO.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + requestDTO.getEmail());
            }
        }

        if (!student.getCpf().equals(requestDTO.getCpf())) {
            if (studentRepository.existsByCpf(requestDTO.getCpf())) {
                throw new IllegalArgumentException("CPF already exists: " + requestDTO.getCpf());
            }
        }

        student.setName(requestDTO.getName());
        student.setEmail(requestDTO.getEmail());
        student.setCpf(requestDTO.getCpf());
        student.setBirthDate(requestDTO.getBirthDate());

        Student updatedStudent = studentRepository.save(student);

        log.info("Student updated successfully with ID: {}", updatedStudent.getId());

        return convertToResponseDTO(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        log.info("Deleting student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));

        studentRepository.delete(student);

        log.info("Student deleted successfully with ID: {}", id);
    }

    private StudentResponseDTO convertToResponseDTO(Student student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getCpf(),
                student.getBirthDate(),
                student.getActive()
        );
    }

}
