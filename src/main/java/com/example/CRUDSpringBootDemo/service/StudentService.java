package com.example.CRUDSpringBootDemo.service;

import com.example.CRUDSpringBootDemo.dto.StudentRequestDto;
import com.example.CRUDSpringBootDemo.dto.StudentResponseDto;
import com.example.CRUDSpringBootDemo.entity.Student;
import com.example.CRUDSpringBootDemo.repository.StudentRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private StudentRepository  studentRepository;
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentResponseDto createStudent(StudentRequestDto studentReqDto) {


//        Optional<Student> existingStudent = studentRepository.findById(studentReqDto.getId());
//        if(existingStudent.isPresent()) {
//            return "Student with this ID is already present!";
//        }
//        studentReq.setDeleted(false);
//        Student studentResp = studentRepository.save(studentReq);
//
//        return studentResp;
        Student student = mapToEntity(studentReqDto);
        Student studentResp  =  studentRepository.save(student);
        return mapToDto(studentResp);

    }
    public Student getStudent(Long id) {
        //query must be select * from student where id =1 and delete = false
        Optional<Student> studentResp = studentRepository.findByIdAndDeletedIsFalse(id);
        if(studentResp.isPresent()){
            return studentResp.get();
        }
        return null;
    }
    public List<Student> getAllStudent() {
        // query must be select * from student where delete = false
        List<Student> studentList = studentRepository.findByDeletedIsFalse();
        return  studentList;
    }
    public Student updateStudent(Long id, Student studentReq) {
        //Query must be
        Optional<Student> existingStudent = studentRepository.findByIdAndDeletedIsFalse(id);
        if(existingStudent.isEmpty()){
            return null;
        }
        Student studentToSave = existingStudent.get();
        studentToSave.setName(studentReq.getName());
        studentToSave.setAge(studentReq.getAge());
        studentToSave.setRollNo(studentReq.getRollNo());
        studentToSave.setSubject(studentReq.getSubject());
        studentToSave.setEmail(studentReq.getEmail());
        return studentRepository.save(studentToSave);
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
    private Student mapToEntity(StudentRequestDto studentReqDto) {
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
    private StudentResponseDto mapToDto(Student student) {
        StudentResponseDto studentResponseDto = new StudentResponseDto();
        studentResponseDto.setId(student.getId());
        studentResponseDto.setName(student.getName());
        studentResponseDto.setAge(student.getAge());
        studentResponseDto.setRollNo(student.getRollNo());
        studentResponseDto.setSubject(student.getSubject());
        studentResponseDto.setEmail(student.getEmail());
        studentResponseDto.setMessage("student saved successfully");
        return studentResponseDto;
    }

}
