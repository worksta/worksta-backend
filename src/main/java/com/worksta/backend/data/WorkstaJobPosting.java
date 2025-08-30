package com.worksta.backend.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * A Worksta job posting. Businesses post jobs to the Worksta
 * job pool, and individual jobs are modelled by this class.
 *
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

    private String business;
    private String title;
    private String description;
    private String location;

    @ElementCollection
    private List<String> jobRequirements;

    @ElementCollection
    private List<String> tags;

    @ElementCollection
    @CollectionTable
    private List<JobShift> jobShifts;

    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class JobShift {
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private double hourlyRate;
        private double fixedAmount;
    }

}
