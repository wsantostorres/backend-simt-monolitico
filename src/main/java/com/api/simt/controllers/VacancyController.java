package com.api.simt.controllers;

import com.api.simt.dtos.VacancyDto;
import com.api.simt.models.CourseModel;
import com.api.simt.models.StudentModel;
import com.api.simt.models.VacancyModel;
import com.api.simt.repositories.CourseRepository;
import com.api.simt.repositories.StudentRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/vacancies")
public class VacancyController {
    @Autowired
    VacancyRepository vacancyRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StudentRepository studentRepository;

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

        /* Aqui está ocorrendo o relacionamento entre
        vagas e cursos na hora do cadastro */
        List<CourseModel> courses = courseRepository.findAll();
        List<CourseModel> vacancyCoursesDto = vacancyDto.courses();

        if (courses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<CourseModel> selectedCourses = new ArrayList<>();

        for (CourseModel course : courses) {
            for (CourseModel vacancyCourseDto : vacancyCoursesDto) {
                if (vacancyCourseDto.getId() == course.getId()) {
                    course.getVacancies().add(requestData);
                    selectedCourses.add(course);
                }
            }
        }

        requestData.setCourses(selectedCourses);

        return ResponseEntity.status(HttpStatus.CREATED).body(vacancyRepository.save(requestData));
    }

    @PostMapping("/send-resume/{studentId}/{vacancyId}")
    public ResponseEntity<Object> sendResumeForVacancy(@PathVariable Long studentId, @PathVariable Long vacancyId) {
        try{
            Optional<StudentModel> studentOptional = studentRepository.findById(studentId);
            Optional<VacancyModel> vacancyOptional = vacancyRepository.findById(vacancyId);

            if(studentOptional.isPresent() && vacancyOptional.isPresent()) {
                StudentModel student = studentOptional.get();
                VacancyModel vacancy = vacancyOptional.get();
                String studentCourseString = student.getCourse();
                CourseModel studentCourse = courseRepository.findByName(studentCourseString);

                /* Este primeiro "if" vamos analisar se vamos remover ou não, porque o front-end
               naturalmente já está bloqueando pessoas que não têm vínculo entrar no sistema.
               (ainda precisa ser testado) */
                if (student.getBondType() == null || student.getBondType().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.GONE).body(null);
                }

                if (!vacancy.getCourses().contains(studentCourse)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }

                if (vacancy.getStudents().contains(student)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
                }

                student.getVacancies().add(vacancy);
                vacancy.getStudents().add(student);

                studentRepository.save(student);
                vacancyRepository.save(vacancy);

                return ResponseEntity.status(HttpStatus.OK).body(null);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacancyModel> updateVacancy(@PathVariable long id, @Valid @RequestBody VacancyDto vacancyDto) {
        try {
            Optional<VacancyModel> existingVacancyOptional = vacancyRepository.findById(id);

            if (existingVacancyOptional.isPresent()) {

                VacancyModel existingVacancy = existingVacancyOptional.get();

                /* Aqui, na hora de atualizar a vaga eu
                estou removendo todos os cursos da vaga existente
                e depois colocando os novos cursos que vieram, isso pode ser melhorado
                porque às vezes os cursos enviados podem ser iguais aos que já tem e aqui
                acaba removendo e adicionando de novo de qualquer forma. */
                List<CourseModel> currentCourses = existingVacancy.getCourses();
                List<CourseModel> coursesToRemove = new ArrayList<>();

                for (CourseModel currentCourse : currentCourses) {
                    currentCourse.getVacancies().remove(existingVacancy);
                    coursesToRemove.add(currentCourse);
                }

                existingVacancy.getCourses().removeAll(coursesToRemove);

                List<CourseModel> courses = courseRepository.findAll();
                List<CourseModel> vacancyCoursesDto = vacancyDto.courses();

                List<CourseModel> selectedCourses = new ArrayList<>();

                for (CourseModel course : courses) {
                    for (CourseModel vacancyCourseDto : vacancyCoursesDto) {
                        if (vacancyCourseDto.getId() == course.getId()) {
                            course.getVacancies().add(existingVacancy);
                            selectedCourses.add(course);
                        }
                    }
                }

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
                existingVacancy.setCourses(selectedCourses);

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
        try{
            Optional<VacancyModel> vacancyOptional = vacancyRepository.findById(id);

            if(vacancyOptional.isPresent()){
                VacancyModel vacancy = vacancyOptional.get();
                List<CourseModel> relatedCourses = vacancy.getCourses();

                for (CourseModel relatedCourse : relatedCourses) {
                    relatedCourse.getVacancies().remove(vacancy);
                }

                List<StudentModel> relatedStudents = vacancy.getStudents();
                for (StudentModel relatedStudent : relatedStudents) {
                    relatedStudent.getVacancies().remove(vacancy);
                }

                vacancyRepository.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}