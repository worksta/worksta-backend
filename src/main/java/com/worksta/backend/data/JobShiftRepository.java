package com.worksta.backend.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface JobShiftRepository
        extends PagingAndSortingRepository<WorkstaJobPosting.JobShift, UUID>,
                org.springframework.data.repository.CrudRepository<WorkstaJobPosting.JobShift, UUID> {

    WorkstaJobPosting.JobShift findDistinctFirstById(UUID id);
}
