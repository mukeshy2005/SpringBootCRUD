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

    public Object createStudent(Student studentReq) {


        Optional<Student> existingStudent = studentRepository.findById(studentReq.getId());
        if(existingStudent.isPresent()) {
            return "Student with this ID is already present!";
        }
        studentReq.setDeleted(false);
        Student studentResp = studentRepository.save(studentReq);

        return studentResp;
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

}
