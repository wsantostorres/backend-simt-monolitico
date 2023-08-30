package com.api.simt.controllers;

import com.api.simt.models.CourseModel;
import com.api.simt.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    CourseRepository courseRepository;

    @GetMapping
    public ResponseEntity<List<CourseModel>> getAllCourses(){
        return ResponseEntity.status(HttpStatus.OK).body(courseRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<CourseModel>> getCourse(@PathVariable long id){
        if(courseRepository.findById(id).isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(courseRepository.findById(id));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping
    public ResponseEntity<CourseModel> createCourse(@RequestBody CourseModel course){
        return ResponseEntity.status(HttpStatus.CREATED).body(courseRepository.save(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseModel> updateCourse(@PathVariable long id, @RequestBody CourseModel course){
        if(courseRepository.findById(id).isPresent()){
            course.setId(id);
            return ResponseEntity.status(HttpStatus.OK).body(courseRepository.save(course));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCourse(@PathVariable long id){
        if(courseRepository.findById(id).isPresent()){
            courseRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
