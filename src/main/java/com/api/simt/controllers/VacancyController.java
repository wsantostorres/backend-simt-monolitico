package com.api.simt.controllers;

import com.api.simt.models.VacancyModel;
import com.api.simt.repositories.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vacancies/")
public class VacancyController {
    @Autowired
    VacancyRepository vacancyRepository;

    @GetMapping
    public ResponseEntity<List<VacancyModel>> getAllVacancies(){
        return ResponseEntity.status(HttpStatus.OK).body(vacancyRepository.findAll());
    }

    @GetMapping("{id}/")
    public ResponseEntity<Optional<VacancyModel>> getVacancy(@PathVariable long id){
        if(vacancyRepository.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(vacancyRepository.findById(id));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping
    public ResponseEntity<VacancyModel> saveVacancy(@RequestBody VacancyModel vaga){
        return ResponseEntity.status(HttpStatus.CREATED).body(vacancyRepository.save(vaga));
    }

    @PutMapping("{id}/")
    public ResponseEntity<VacancyModel> updateVacancy(@PathVariable long id, @RequestBody VacancyModel vaga){
        if(vacancyRepository.findById(id).isPresent()){
            vaga.setId(id);
            return ResponseEntity.status(HttpStatus.OK).body(vacancyRepository.save(vaga));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("{id}/")
    public ResponseEntity<Object> deleteVacancy(@PathVariable long id){
        if(vacancyRepository.findById(id).isPresent()) {
            vacancyRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}