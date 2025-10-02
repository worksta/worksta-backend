package com.worksta.backend.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * A {@link org.springframework.data.repository.Repository Repository} for {@linkplain WorkstaJobPosting job postings}.
 *
 * @author Gerard Sayson
 */
public interface JobPostingRepository extends JpaRepository<WorkstaJobPosting, UUID> {

    Slice<WorkstaJobPosting> getWorkstaJobPostingByBusinessId(UUID businessId);

    WorkstaJobPosting getWorkstaJobPostingById(UUID id);
    
    Slice<WorkstaJobPosting> getWorkstaJobPostingsByTitle(String title, Pageable pageable);

    Slice<WorkstaJobPosting> getWorkstaJobPostingsByTitleIsLike(String title, Pageable pageable);

    Slice<WorkstaJobPosting> findAllByBusinessId(UUID businessId, Pageable pageable);
}
