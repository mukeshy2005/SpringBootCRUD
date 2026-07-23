package com.example.CRUDSpringBootDemo.dto;

import jakarta.validation.constraints.*;

public class CreateStudentRequestDto {
    @NotBlank(message = "can not be blank")
    @Size(min=2 , max = 20, message ="student name must be 2 to 50 character long" )
    private String name;
    @NotBlank(message = "can not be blank")
    @Email(message = "enter valid email")
    private String email;
    @NotNull(message  = "age is required ")
    @Min(value = 18,message ="only 18 above can apply")
    private Integer age;
    @NotNull(message = "roll number is required ")
    private Integer rollNo;
    @NotBlank
    private String subject;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
