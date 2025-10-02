package com.worksta.backend.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * A Worksta job posting. Businesses post jobs to the Worksta
 * job pool, and individual jobs are modelled by this class.
 * For efficiency, individual shifts are contained within one {@link WorkstaJobPosting} instance,
 * instead of each shift having its own instance.
 *
 * @author Gerard Sayson
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkstaJobPosting {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;


    private UUID businessId;
    private String title;
    private String description;
    private String location;

    @ElementCollection
    private List<String> jobRequirements;

    @ElementCollection
    private List<String> tags;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobPosting", orphanRemoval = true)
    @JsonManagedReference
    private List<JobShift> jobShifts;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    public static final class JobShift {
        @Id
        @Column(columnDefinition = "uuid", updatable = false, nullable = false)
        @GeneratedValue
        private UUID id;

        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;

        private BigDecimal hourlyRate;
        private BigDecimal fixedAmount;

        private boolean available;

        @ManyToOne(cascade = CascadeType.ALL)
        @JsonBackReference
        private WorkstaJobPosting jobPosting;

        @ElementCollection
        @CollectionTable
        private List<JobApplication> jobApplications;
    }

    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class JobApplication {
        private UUID workerID; // unique; applicants make only one job posting
        private boolean accepted;
        private @Nullable String coverMessage;
    }

}
