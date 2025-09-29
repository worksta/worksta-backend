package com.worksta.backend.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for {@link WorkstaWorker}.
 */
public interface WorkstaWorkerRepository extends JpaRepository<WorkstaWorker, UUID> {

    boolean existsByEmail(String email);

    Optional<WorkstaWorker> findByEmail(String email);
}
