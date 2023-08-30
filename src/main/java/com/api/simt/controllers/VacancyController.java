package com.api.simt.controllers;

import com.api.simt.models.VacancyModel;
import com.api.simt.repositories.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/vacancies/")
public class VacancyController {
    @Autowired
    VacancyRepository vacancyRepository;

    public VacancyController(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

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
    public ResponseEntity<?> saveVacancy(@RequestBody Map<String, String> requestData) {
        try {
            String title = requestData.get("title");
            String closingDateStr = requestData.get("closingDate");
            String description = requestData.get("description");
            int type = Integer.parseInt(requestData.get("type"));
            int morning = Integer.parseInt(requestData.get("morning"));
            int afternoon = Integer.parseInt(requestData.get("afternoon"));
            int night = Integer.parseInt(requestData.get("night"));

            LocalDate parsedClosingDate = LocalDate.parse(closingDateStr, DateTimeFormatter.ISO_DATE);
            LocalDateTime combinedDateTime = LocalDateTime.of(parsedClosingDate, LocalTime.now());

            VacancyModel vacancyModel = new VacancyModel();
            vacancyModel.setTitle(title);
            vacancyModel.setClosingDate(combinedDateTime);
            vacancyModel.setDescription(description);
            vacancyModel.setType(type);
            vacancyModel.setMorning(morning);
            vacancyModel.setAfternoon(afternoon);
            vacancyModel.setNight(night);
            vacancyModel.setCreatedAt(LocalDateTime.now());
            VacancyModel savedVacancy = vacancyRepository.save(vacancyModel);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedVacancy);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("{id}/")
    public ResponseEntity<?> updateVacancy(@PathVariable long id, @RequestBody Map<String, String> requestData) {
        try {
            Optional<VacancyModel> existingVacancyOptional = vacancyRepository.findById(id);

            if (existingVacancyOptional.isPresent()) {
                VacancyModel existingVacancy = existingVacancyOptional.get();

                String title = requestData.get("title");
                String closingDateStr = requestData.get("closingDate");
                String description = requestData.get("description");
                int type = Integer.parseInt(requestData.get("type"));
                int morning = Integer.parseInt(requestData.get("morning"));
                int afternoon = Integer.parseInt(requestData.get("afternoon"));
                int night = Integer.parseInt(requestData.get("night"));

                LocalDate parsedClosingDate = LocalDate.parse(closingDateStr, DateTimeFormatter.ISO_DATE);
                LocalDateTime combinedDateTime = LocalDateTime.of(parsedClosingDate, LocalTime.of(0, 0));

                existingVacancy.setTitle(title);
                existingVacancy.setClosingDate(combinedDateTime);
                existingVacancy.setDescription(description);
                existingVacancy.setType(type);
                existingVacancy.setMorning(morning);
                existingVacancy.setAfternoon(afternoon);
                existingVacancy.setNight(night);
                existingVacancy.setUpdatedAt(LocalDateTime.now());

                VacancyModel updatedVacancy = vacancyRepository.save(existingVacancy);

                return ResponseEntity.status(HttpStatus.OK).body(updatedVacancy);
            }
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
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