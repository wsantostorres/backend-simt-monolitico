package com.api.simt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "STUDENTS")
public class StudentModel {
    @Id
    @NotNull
    private long id;

    @NotBlank
    @Column(nullable = false)
    private String fullName;

    @NotBlank
    @Column(nullable = false)
    private String registration;

    @NotBlank
    @Column(nullable = false)
    private String bondType;

    @NotBlank
    @Column(nullable = false)
    private String course;

    @NotBlank
    @Column(nullable = false)
    private String schoolShift;

    @NotBlank
    @Column(nullable = false)
    private String schoolClass;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "VACANCIES_STUDENTS",
            joinColumns = @JoinColumn(name = "student_fk",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "vacancy_fk",
                    referencedColumnName = "id"))
    @JsonIgnore
    private List<VacancyModel> vacancies = new ArrayList<>();
}
