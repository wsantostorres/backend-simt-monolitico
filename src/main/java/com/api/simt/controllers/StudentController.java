package com.api.simt.controllers;

import com.api.simt.models.StudentModel;
import com.api.simt.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<StudentModel>> getStudent(@PathVariable long id){
        if(studentRepository.findById(id).isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(studentRepository.findById(id));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping
    public ResponseEntity<StudentModel> createStudent(@RequestBody StudentModel student){
        return ResponseEntity.status(HttpStatus.CREATED).body(studentRepository.save(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentModel> updateStudent(@PathVariable long id, @RequestBody StudentModel student){
        if(studentRepository.findById(id).isPresent()){
            student.setId(id);
            return ResponseEntity.status(HttpStatus.OK).body(studentRepository.save(student));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
