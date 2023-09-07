package com.api.simt.controllers;

import com.api.simt.dtos.ResumeDto;
import com.api.simt.models.ProjectModel;
import com.api.simt.repositories.ProjectRepository;
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

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/resumes")
public class ResumeController {
    @Autowired
    ResumeRepository resumeRepository;

    @Autowired
    ProjectRepository projectRepository;

    @PostMapping
    public ResponseEntity<String> createResume(@RequestBody ResumeDto resumeDto) {

        try{
            ResumeModel resumeModel = new ResumeModel();
            resumeModel.setObjectiveDescription(resumeDto.objectiveDescription());

            for(ProjectModel project : resumeDto.projects()){
                projectRepository.save(project);
                resumeModel.getProjects().add(project);
                project.setResume(resumeModel);
            }
            resumeRepository.save(resumeModel);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cadastrado com sucesso");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateResume(@PathVariable long id, @RequestBody ResumeModel resume) {
        if(resumeRepository.findById(id).isPresent()){
            resume.setId(id);
            return ResponseEntity.status(HttpStatus.OK).body(resumeRepository.save(resume));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
