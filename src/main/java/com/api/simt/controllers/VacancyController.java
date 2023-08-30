package com.api.simt.controllers;

import com.api.simt.dtos.VacancyDto;
import com.api.simt.models.VacancyModel;
import com.api.simt.repositories.VacancyRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vacancies")
public class VacancyController {
    @Autowired
    VacancyRepository vacancyRepository;

    @GetMapping
    public ResponseEntity<List<VacancyModel>> getAllVacancies(){
        return ResponseEntity.status(HttpStatus.OK).body(vacancyRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<VacancyModel>> getVacancy(@PathVariable long id){
        if(vacancyRepository.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(vacancyRepository.findById(id));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping
    public ResponseEntity<VacancyModel> createVacancy(@Valid @RequestBody VacancyDto vacancyDto) {
        LocalDate parsedClosingDate = LocalDate.parse(vacancyDto.closingDate(), DateTimeFormatter.ISO_DATE);
        LocalDateTime combinedDateTime = LocalDateTime.of(parsedClosingDate, LocalTime.of(23, 59));

        VacancyModel requestData = new VacancyModel();
        requestData.setTitle(vacancyDto.title());
        requestData.setDescription(vacancyDto.description());
        requestData.setType(vacancyDto.type());
        requestData.setMorning(vacancyDto.morning());
        requestData.setAfternoon(vacancyDto.afternoon());
        requestData.setNight(vacancyDto.night());
        requestData.setClosingDate(combinedDateTime);
        requestData.setCreatedAt(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(vacancyRepository.save(requestData));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacancyModel> updateVacancy(@PathVariable long id, @Valid @RequestBody VacancyDto vacancyDto) {
        try {
            Optional<VacancyModel> existingVacancyOptional = vacancyRepository.findById(id);

            if (existingVacancyOptional.isPresent()) {
                VacancyModel existingVacancy = existingVacancyOptional.get();

                LocalDate parsedClosingDate = LocalDate.parse(vacancyDto.closingDate(), DateTimeFormatter.ISO_DATE);
                LocalDateTime combinedDateTime = LocalDateTime.of(parsedClosingDate, LocalTime.of(23, 59));

                existingVacancy.setTitle(vacancyDto.title());
                existingVacancy.setDescription(vacancyDto.description());
                existingVacancy.setType(vacancyDto.type());
                existingVacancy.setMorning(vacancyDto.morning());
                existingVacancy.setAfternoon(vacancyDto.afternoon());
                existingVacancy.setNight(vacancyDto.night());
                existingVacancy.setClosingDate(combinedDateTime);
                existingVacancy.setUpdatedAt(LocalDateTime.now());

                VacancyModel updatedVacancy = vacancyRepository.save(existingVacancy);

                return ResponseEntity.status(HttpStatus.OK).body(updatedVacancy);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVacancy(@PathVariable long id){
        if(vacancyRepository.findById(id).isPresent()) {
            vacancyRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}