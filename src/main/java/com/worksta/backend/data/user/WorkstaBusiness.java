package com.worksta.backend.data.user;

import com.worksta.backend.data.WorkstaJobPosting;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkstaBusiness {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String email;
    private String description; // edit later

    @OneToMany(cascade = CascadeType.ALL)
    private Set<WorkstaJobPosting> jobPostings;

}
