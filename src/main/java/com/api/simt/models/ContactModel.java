package com.api.simt.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "CONTACTS")
public class ContactModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int phone;
    private String email;
    private String linkedin;
}
