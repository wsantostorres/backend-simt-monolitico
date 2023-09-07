package com.api.simt.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "RESUMES")
public class ResumeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String objectiveDescription;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<ProjectModel> projects = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<ExperienceModel> experiences = new ArrayList<>();

}
