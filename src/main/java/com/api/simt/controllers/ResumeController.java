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

//    Esse Logger não faz o menor sentido,
//    o próprio "println" já estava mostrando a msn de erro
    private static final Logger logger = LoggerFactory.getLogger(ResumeController.class);

    @GetMapping("{id}")
    public ResponseEntity<ResumeModel> getResume(@PathVariable long id){
        try{
            ResumeModel resumeFound = resumeService.getResume(id);

            if(resumeFound == null){
                throw new Exception("Currículo não encontrado");
            }

            return ResponseEntity.status(HttpStatus.OK).body(resumeFound);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<ResumeModel> createResume(@RequestBody ResumeDto resumeDto) {

        try {
            /*
            Isso é apenas um exemplo, porque vamos ter que limitar a quantidade
            de projetos, experiências que o aluno pode enviar. Isso pode ser feito no
            front-end, porém eu vou deixar aqui porque ainda não sei qual seria o ideal.
            */
            if(resumeDto.projects().size() > 2 || resumeDto.experiences().size() > 2 || resumeDto.academics().size() > 2
            || resumeDto.skills().size() > 2){
                throw new Exception("quantidade não permitida");
            }
            ResumeModel savedResume = resumeService.createResume(resumeDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedResume);
        } catch (Exception e) {
            logger.error("Erro ao criar o currículo: " + e.getMessage(), e);
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @PutMapping("{id}")
    public ResponseEntity<ResumeModel> updateResume(@PathVariable long id, @RequestBody ResumeDto resumeDto){
        try{

            if(resumeDto.projects().size() > 2 || resumeDto.experiences().size() > 2 || resumeDto.academics().size() > 2
                    || resumeDto.skills().size() > 2){
                throw new Exception("quantidade não permitida");
            }

            ResumeModel resumeFound = resumeService.updateResume(id, resumeDto);

            if(resumeFound == null){
                throw new Exception("Currículo não encontrado");
            }

            return ResponseEntity.status(HttpStatus.OK).body(resumeFound);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
