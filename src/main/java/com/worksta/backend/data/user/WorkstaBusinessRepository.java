package com.worksta.backend.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for {@link WorkstaBusiness}.
 */
public interface WorkstaBusinessRepository extends JpaRepository<WorkstaBusiness, UUID> {

    boolean existsByEmail(String email);

    Optional<WorkstaBusiness> findByEmail(String email);
}
