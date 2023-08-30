package com.api.simt.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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
}
