package com.api.simt.controllers;

import com.api.simt.models.EmployeeModel;
import com.api.simt.models.StudentModel;
import com.api.simt.models.VacancyModel;
import com.api.simt.repositories.EmployeeRepository;
import com.api.simt.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-data")
public class GeneralUserController {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping
    public ResponseEntity<Object> getDataByToken(@RequestHeader("Authorization") String token) {
        try{
            Long id = extractIdFromToken(token);

            Optional<StudentModel> studentOptional = studentRepository.findById(id);
            if (studentOptional.isPresent()) {
                StudentModel student = studentOptional.get();

                Map<String, Object> dataStudent = new HashMap<>();
                dataStudent.put("id", student.getId());
                dataStudent.put("fullName", student.getFullName());
                dataStudent.put("bondType", student.getBondType());
                dataStudent.put("course", student.getCourse());
                /* Pegando os ids de todas as vagas que o aluno participou*/
                List<Long> vacancyIds = student.getVacancies()
                        .stream()
                        .map(VacancyModel::getId)
                        .toList();
                dataStudent.put("vacancyIds", vacancyIds);

                return ResponseEntity.status(HttpStatus.OK).body(dataStudent);
            }

            Optional<EmployeeModel> employeeOptional = employeeRepository.findById(id);
            if(employeeOptional.isPresent()){
                EmployeeModel employee = employeeOptional.get();

                Map<String, Object> employeeData = new HashMap<>();
                employeeData.put("id", employee.getId());
                employeeData.put("fullName", employee.getFullName());
                employeeData.put("bondType", employee.getBondType());

                return ResponseEntity.status(HttpStatus.OK).body(employeeData);
            }

            Map<String, Object> UserId = new HashMap<>();
            UserId.put("id", id);

            return ResponseEntity.status(HttpStatus.OK).body(UserId);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido");
        }

    }

    private Long extractIdFromToken(String token) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String tokenPayload = new String(decoder.decode(chunks[1]));

        Matcher matcher = Pattern.compile("\"user_id\":\\s*(\\d+)").matcher(tokenPayload);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        } else {
            throw new IllegalArgumentException("Token inválido");
        }
    }
}
