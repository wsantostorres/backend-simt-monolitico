package com.api.simt.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "PROJECTS")
public class ProjectModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String type;
    @Column
    private String titleProject;
    @Column
    private int initialYear;
    @Column
    private int closingYear;

    @ManyToOne
    @JoinColumn(name="resume_id")
    private ResumeModel resume;
}
