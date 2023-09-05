package com.api.simt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.simt.models.ResumeModel;
import com.api.simt.repositories.ResumeRepository;


@RestController
@RequestMapping("/resumes")
public class ResumeController {
    @Autowired
    ResumeRepository resumeRepository;

    @PostMapping
    public ResponseEntity<ResumeModel> createCurriculum(@RequestBody ResumeModel resume) {
       return ResponseEntity.status(HttpStatus.CREATED).body(resumeRepository.save(resume));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCurriculum(@PathVariable long id, @RequestBody ResumeModel resume) {
        if(resumeRepository.findById(id).isPresent()){
            resume.setId(id);
            return ResponseEntity.status(HttpStatus.OK).body(resumeRepository.save(resume));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
