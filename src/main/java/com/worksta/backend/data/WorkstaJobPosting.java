package com.worksta.backend.data;

import com.worksta.backend.data.user.WorkstaBusiness;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private UUID id;


    @ManyToOne
    private WorkstaBusiness business;
    private String title;
    private String description;
    private String location;

    @ElementCollection
    private List<String> jobRequirements;

    @ElementCollection
    private List<String> tags;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobPosting")
    private List<JobShift> jobShifts;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    public static final class JobShift {
        @Id
        @GeneratedValue
        private UUID id;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private double hourlyRate;
        private double fixedAmount;
        private boolean available;

        @ManyToOne
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
