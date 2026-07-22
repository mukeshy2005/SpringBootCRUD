package com.example.CRUDSpringBootDemo.controller;

import com.example.CRUDSpringBootDemo.dto.CreateStudentRequestDto;
import com.example.CRUDSpringBootDemo.dto.CreateStudentResponseDto;
import com.example.CRUDSpringBootDemo.dto.UpdateStudentRequestDto;
import com.example.CRUDSpringBootDemo.dto.UpdateStudentResponseDto;
import com.example.CRUDSpringBootDemo.entity.Student;
import com.example.CRUDSpringBootDemo.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @PostMapping("/create")
    public ResponseEntity<CreateStudentResponseDto> createStudent(@RequestBody CreateStudentRequestDto studentRequestDto) {

//        Object createdStudent = studentService.createStudent(studentRequestDto);
//        if(createdStudent instanceof String) {
//            return ResponseEntity
//                    .status(409)   // CONFLICT
//                    .body(createdStudent); // "Student with this ID is already present!"
//        }
         CreateStudentResponseDto createdStudent = studentService.createStudent(studentRequestDto);

        return ResponseEntity.status(201).body(createdStudent);
    }
    @GetMapping("/get")
    public ResponseEntity<Student> getStudent(@RequestParam Long id){
        Student studentResp = studentService.getStudent(id);
        if(studentResp == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(200).body(studentResp);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<Student>> getAllStudents(){
        List<Student> studentList = studentService.getAllStudent();
        if(studentList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(200).body(studentList);
    }
    @PutMapping("/update")
    public ResponseEntity<UpdateStudentResponseDto> updateStudent(@RequestParam Long id, @RequestBody UpdateStudentRequestDto studentUpdateReqDto){
        UpdateStudentResponseDto studentUpdateRespDto = studentService.updateStudent(id, studentUpdateReqDto);
        if(studentUpdateRespDto == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(200).body(studentUpdateRespDto);
    }
    //creating updateAll
    @PutMapping("/updateAll")
    public ResponseEntity<List<Student>> updateAllStudent(@RequestBody List<Student> studentList){
        List<Student> studentResp = studentService.updateAllStudent(studentList);
        if(studentResp == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(200).body(studentResp);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteStudent(@RequestParam Long id){
        Boolean isDeleted = studentService.deleteStudent(id);
            if(!isDeleted){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok("Delete Success");
    }
    //Creating another method that is delete all
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllStudents(){
        Boolean isDeleted = studentService.deleteAllStudent();
        if(!isDeleted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Delete All Success");
    }
    @PatchMapping("/softDelete ")
    public ResponseEntity<String> softDeleteStudents(@RequestParam Long id){
        //quiery mus
        Boolean isDeleted = studentService.softDeleteStudents(id);
        if(!isDeleted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Soft Delete Success");
    }



}
