package com.api.simt.controllers;

import com.api.simt.dtos.ResumeDto;
import com.api.simt.services.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.api.simt.models.ResumeModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RestController
@RequestMapping("/resumes")
public class ResumeController {
    @Autowired
    ResumeService resumeService;

    @GetMapping("{id}")
    public ResponseEntity<ResumeModel> getResume(@PathVariable long id){
        try{
            ResumeModel resumeFound = resumeService.getResume(id);

            if(resumeFound == null){
                throw new Exception("Curriculo nao encontrado");
            }

            return ResponseEntity.status(HttpStatus.OK).body(resumeFound);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{studentId}")
    public ResponseEntity<ResumeModel> createResume(@RequestBody ResumeDto resumeDto,
                                                    @PathVariable long studentId) {

        try {
            /*
            Isso é apenas um exemplo, porque vamos ter que limitar a quantidade
            de projetos, experiências que o aluno pode enviar. Isso pode ser feito no
            front-end, porém eu vou deixar aqui porque ainda não sei qual seria o ideal.
            */
            if(resumeDto.projects().size() > 5 || resumeDto.experiences().size() > 5 || resumeDto.academics().size() > 5
            || resumeDto.skills().size() > 5){
                throw new Exception("quantidade nao permitida");
            }
            ResumeModel savedResume = resumeService.createResume(studentId, resumeDto);

            if(savedResume == null){
                throw new Exception("Este aluno ja tem curriculo ou aluno inexistente!");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedResume);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @PutMapping("/{studentId}/{resumeId}")
    public ResponseEntity<ResumeModel> updateResume(@PathVariable long studentId, @PathVariable long resumeId , @RequestBody ResumeDto resumeDto){
        try{

            if(resumeDto.projects().size() > 5 || resumeDto.experiences().size() > 5 || resumeDto.academics().size() > 5
                    || resumeDto.skills().size() > 5){
                throw new Exception("quantidade nao permitida");
            }

            ResumeModel resumeFound = resumeService.updateResume(studentId, resumeId, resumeDto);

            if(resumeFound == null){
                throw new Exception("Curriculo nao encontrado");
            }

            return ResponseEntity.status(HttpStatus.OK).body(resumeFound);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
