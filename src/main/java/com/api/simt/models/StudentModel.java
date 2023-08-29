package com.api.simt.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "STUDENTS")
public class StudentModel {
    @Id
    private long id;
    
    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String registration;

    @Column(nullable = false)
    private String bondType;

    @Column(nullable = false)
    private String course;

    @Column(nullable = false)
    private String schoolShift;

    @Column(nullable = false)
    private String schoolClass;
}
