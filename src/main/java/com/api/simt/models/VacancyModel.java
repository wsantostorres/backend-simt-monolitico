package com.api.simt.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "VACANCIES")
public class VacancyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private LocalDateTime closingDate;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private int type;

    @Column
    private int morning;

    @Column
    private int afternoon;

    @Column
    private int night;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
