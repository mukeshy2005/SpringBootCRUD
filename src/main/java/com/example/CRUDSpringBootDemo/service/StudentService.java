package com.example.CRUDSpringBootDemo.service;

import com.example.CRUDSpringBootDemo.dto.CreateStudentRequestDto;
import com.example.CRUDSpringBootDemo.dto.CreateStudentResponseDto;
import com.example.CRUDSpringBootDemo.dto.UpdateStudentRequestDto;
import com.example.CRUDSpringBootDemo.dto.UpdateStudentResponseDto;
import com.example.CRUDSpringBootDemo.entity.Student;
import com.example.CRUDSpringBootDemo.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private StudentRepository  studentRepository;
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public CreateStudentResponseDto createStudent(CreateStudentRequestDto studentReqDto) {


//        Optional<Student> existingStudent = studentRepository.findById(studentReqDto.getId());
//        if(existingStudent.isPresent()) {
//            return "Student with this ID is already present!";
//        }
//        studentReq.setDeleted(false);
//        Student studentResp = studentRepository.save(studentReq);
//
//        return studentResp;
        Student student = mapToEntity(studentReqDto);
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        Student studentResp  =  studentRepository.save(student);
        return mapToDto(studentResp);

    }
    public CreateStudentResponseDto getStudent(Long id) {
        //query must be select * from student where id =1 and delete = false
        Optional<Student> studentResp = studentRepository.findByIdAndDeletedIsFalse(id);
        if(studentResp.isPresent()){
            return mapToDto(studentResp.get());
        }
        return null;
    }
    public List<CreateStudentResponseDto> getAllStudent() {
        // query must be select * from student where delete = false
        List<Student> studentList = studentRepository.findByDeletedIsFalse();
        return studentList.stream()
                .map(this::mapToDto)
                .toList();
    }
    public UpdateStudentResponseDto updateStudent(Long id, UpdateStudentRequestDto studentUpdateReqDto) {
        //Query must be
        Optional<Student> existingStudent = studentRepository.findByIdAndDeletedIsFalse(id);
        if(existingStudent.isEmpty()){
            return null;
        }
        Student studentToSave = existingStudent.get();
        studentToSave.setName(studentUpdateReqDto.getName());
        studentToSave.setAge(studentUpdateReqDto.getAge());
        studentToSave.setRollNo(studentUpdateReqDto.getRollNo());
        studentToSave.setSubject(studentUpdateReqDto.getSubject());
//        studentToSave.setEmail(studentUpdateReqDto.getEmail());
        studentToSave.setUpdatedAt(LocalDateTime.now());
        Student savedStudent = studentRepository.save(studentToSave);
        return  mapToUpdateDto(savedStudent);

    }
    public List<Student> updateAllStudent(List<Student> studentList) {
        List<Student> allStudent =
                studentRepository.findByDeletedIsFalse();

        for(Student newStudentData : studentList) {
            for(Student dbStudent : allStudent) {
                // ✅ .equals() use kiya Long comparison ke liye
                if(dbStudent.getId()==(newStudentData.getId())) {
                    dbStudent.setName(newStudentData.getName());
                    dbStudent.setAge(newStudentData.getAge());
                    dbStudent.setRollNo(newStudentData.getRollNo());
                    dbStudent.setSubject(newStudentData.getSubject());
                    dbStudent.setEmail(newStudentData.getEmail());
                }
            }
        }
        return studentRepository.saveAll(allStudent);
    }
    public Boolean deleteStudent(Long id) {
        Boolean isStudent = studentRepository.existsById(id);
        if(!isStudent){
            return false;
        }
        studentRepository.deleteById(id);
        return true;
    }
    public Boolean deleteAllStudent() {
         studentRepository.deleteAll();
         return true;
    }
    public Boolean softDeleteStudents(Long id) {
        Optional<Student> existingStudent =
                studentRepository.findByIdAndDeletedIsFalse(id); // ✅ fixed!
        if(existingStudent.isEmpty()){
            return false;
        }
        Student studentToSave = existingStudent.get();
        studentToSave.setDeleted(true);
        studentRepository.save(studentToSave);
        return true;
    }

    //method response dto and request dto
    private Student mapToEntity(CreateStudentRequestDto studentReqDto) {
        Student student = new Student();
        student.setName(studentReqDto.getName());
        student.setAge(studentReqDto.getAge());
        student.setRollNo(studentReqDto.getRollNo());
        student.setSubject(studentReqDto.getSubject());
        student.setEmail(studentReqDto.getEmail());
        student.setDeleted(false);
        return student;

        //instead of above method use builder design method

    }
    private CreateStudentResponseDto mapToDto(Student student) {
        CreateStudentResponseDto studentResponseDto = new CreateStudentResponseDto();
        studentResponseDto.setId(student.getId());
        studentResponseDto.setName(student.getName());
        studentResponseDto.setAge(student.getAge());
        studentResponseDto.setRollNo(student.getRollNo());
        studentResponseDto.setSubject(student.getSubject());
        studentResponseDto.setEmail(student.getEmail());
        studentResponseDto.setMessage("student saved successfully");
        studentResponseDto.setCreatedAt(student.getCreatedAt());
        studentResponseDto.setUpdatedAt(student.getUpdatedAt());
        return studentResponseDto;
    }
    private UpdateStudentResponseDto mapToUpdateDto(Student student) {
        UpdateStudentResponseDto studentUpdateRespDto = new UpdateStudentResponseDto();
        studentUpdateRespDto.setId(student.getId());
        studentUpdateRespDto.setName(student.getName());
        studentUpdateRespDto.setAge(student.getAge());
        studentUpdateRespDto.setRollNo(student.getRollNo());
        studentUpdateRespDto.setSubject(student.getSubject());
        studentUpdateRespDto.setEmail(student.getEmail());
        studentUpdateRespDto.setMessage("student updated successfully");
        studentUpdateRespDto.setUpdatedAt(student.getUpdatedAt());
        return studentUpdateRespDto;
    }

}
