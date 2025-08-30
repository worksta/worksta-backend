package com.worksta.backend.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

/**
 * A {@link org.springframework.data.repository.Repository Repository} for {@linkplain WorkstaJobPosting job postings}.
 *
 * @author Gerard Sayson
 */
public interface JobPostingRepository extends PagingAndSortingRepository<WorkstaJobPosting, UUID>,
        CrudRepository<WorkstaJobPosting, UUID> {

    Slice<WorkstaJobPosting> getWorkstaJobPostingByBusiness(String business, Pageable pageable);

    WorkstaJobPosting getWorkstaJobPostingById(UUID id);
    
    Slice<WorkstaJobPosting> getWorkstaJobPostingsByTitle(String title, Pageable pageable);

    Slice<WorkstaJobPosting> getWorkstaJobPostingsByTitleIsLike(String title, Pageable pageable);
}
