package com.worksta.backend.data.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkstaWorker {

    @Id
    @GeneratedValue
    private UUID id;
    private String firstName;
    private String lastName;
    private String country;
    private String email;
    private String legalStatus; // for now keep it as a string. later we'll make an enum
    private String phoneNumber;
    private int age; // keep private. do not expose

}