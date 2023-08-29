package com.api.simt.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "EMPLOYEES")
public class EmployeeModel {
    @Id
    private long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String registration;

    @Column(nullable = false)
    private String bondType;
}
