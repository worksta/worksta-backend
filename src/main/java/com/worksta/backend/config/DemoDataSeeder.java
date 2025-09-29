package com.worksta.backend.config;

import com.worksta.backend.data.user.WorkstaBusiness;
import com.worksta.backend.data.user.WorkstaBusinessRepository;
import com.worksta.backend.data.user.WorkstaWorker;
import com.worksta.backend.data.user.WorkstaWorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Inserts the two demo accounts into the real database tables on application start-up.
 * Idempotent – will do nothing if the rows are already present.
 */
@Configuration
@RequiredArgsConstructor
public class DemoDataSeeder {

    private final WorkstaWorkerRepository workerRepo;
    private final WorkstaBusinessRepository businessRepo;
    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner seedDemoRows() {
        return _ -> {
            if (!workerRepo.existsByEmail("worker@demo.com")) {
                workerRepo.save(WorkstaWorker.builder()
                        .firstName("Demo")
                        .lastName("Worker")
                        .country("Singapore")
                        .email("worker@demo.com")
                        .legalStatus("Citizen")
                        .phoneNumber("+65 6000 0000")
                        .age(25)
                        .build());
            }

            if (!businessRepo.existsByEmail("business@demo.com")) {
                businessRepo.save(WorkstaBusiness.builder()
                        .name("Demo Pte Ltd")
                        .email("business@demo.com")
                        .description("Seeded demo business from Singapore")
                        .build());
            }
        };
    }
}
