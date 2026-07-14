package com.example.CRUDSpringBootDemo.service;

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

    public Student createStudent(Student studentReq) {


        Student studentResp = studentRepository.save(studentReq);

        return studentResp;
    }
    public Student getStudent(Long id) {
        Optional<Student> studentResp = studentRepository.findById(id);
        if(studentResp.isPresent()){
            return studentResp.get();
        }
        return null;
    }
    public List<Student> getAllStudent() {
        List<Student> studentList = studentRepository.findAll();
        return  studentList;
    }
    public Student updateStudent(Long id, Student studentReq) {
        Optional<Student> existingStudent = studentRepository.findById(id);
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
        List<Student> allStudent = studentRepository.findAll(); // Fetches all 3 students

        for(Student newStudentData : studentList) {
            for(Student dbStudent : allStudent) {
                // If the IDs match, update the fields
                if(dbStudent.getId() == newStudentData.getId()) {
                    dbStudent.setName(newStudentData.getName());
                    dbStudent.setAge(newStudentData.getAge());
                    dbStudent.setRollNo(newStudentData.getRollNo());
                    dbStudent.setSubject(newStudentData.getSubject());
                    dbStudent.setEmail(newStudentData.getEmail());
                }
            }
        }

        // Save the updated list back to the database
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

}
