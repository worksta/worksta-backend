package com.worksta.backend.api.v1;

import com.worksta.backend.data.JobPostingRepository;
import com.worksta.backend.data.JobShiftRepository;
import com.worksta.backend.data.WorkstaJobPosting;
import com.worksta.backend.data.user.WorkstaBusiness;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobsAPIController {

    private final JobPostingRepository jobPostingRepository;
    private final JobShiftRepository jobShiftRepository;
    private final com.worksta.backend.data.user.WorkstaBusinessRepository businessRepository;
    private final com.worksta.backend.data.user.WorkstaWorkerRepository  workerRepository;

    public JobsAPIController(@Autowired JobPostingRepository jobPostingRepository,
                             @Autowired JobShiftRepository jobShiftRepository,
                             @Autowired com.worksta.backend.data.user.WorkstaBusinessRepository businessRepository,
                             @Autowired com.worksta.backend.data.user.WorkstaWorkerRepository workerRepository) {
        this.jobPostingRepository = jobPostingRepository;
        this.jobShiftRepository = jobShiftRepository;
        this.businessRepository = businessRepository;
        this.workerRepository  = workerRepository;
    }

    /**
     * Retrieves the corresponding {@link WorkstaJobPosting}s based on the supplied parameters.
     * Only one (set) of the following should be supplied:
     * <ul>
     *     <li>a {@link UUID} on {@code ?jid=}, which will immediately retrieve a <em>unique</em> job posting or return
     *     a {@code 404} error if it does not exist;</li>
     *     <li>a {@link UUID} on {@code ?bid=}, which will retrieve a page of the jobs created by a business
     *     with the corresponding UUID.</li>
     * </ul>
     * In the case both are given, {@code jid} takes precedence.
     * @param id a UUID corresponding to the requested {@code WorkstaJobPosting}.
     * @return a {@link java.util.Collection Collection} of (not necessarily multiple)
     * job postings matching the supplied parameters.
     */
    @GetMapping("/")
    public ResponseEntity<Collection<WorkstaJobPosting>> getJobPostings(
            @RequestParam(required = false, name = "jid") UUID id,
            @RequestParam(required = false, name = "bid") UUID bid,
            @RequestParam(required = false, defaultValue = "0") int page
            // the default page size is 10; later I can make this configurable
    ) {
        if(id != null) {
            return ResponseEntity.ok(Collections.singleton(
                    jobPostingRepository.getWorkstaJobPostingById(id)
            ));
        } else {
            if(bid != null) {

//                var x = jobPostingRepository.getWorkstaJobPostingByBusiness(
//                        null
//                );
//                return ResponseEntity.ok(
//                        x
//                );
            }
            var x = jobPostingRepository.findAll(PageRequest.of(page, 10))
                    .stream()
                    .toList();
            return ResponseEntity.ok(
                    x
            );
        }
//        return ResponseEntity.badRequest().build();
    }

    /**
     * Creates a new {@link WorkstaJobPosting} based on the supplied request body.
     * The request body takes a form similar to:
     * <pre>
     *     {
     *         "title": "My Job Title",
     *         "description": "My Job Description",
     *         "location": "My Job Location",
     *         "tags": ["tag1", "tag2"],
     *         "shifts": [
     *              {
     *                  "date": "YYYY-MM-DD",
     *                  "startTime": "HH:MM:00",
     *                  "endTime": "HH:MM:00",
     *                  "hourlyRate": 12.34,
     *              },
     *              {
     *                  "date": "YYYY-MM-DD",
     *                  "startTime": "HH:MM",
     *                  "endTime": "HH:MM",
     *                  "fixedAmount": 123.45,
     *              }
     *         ]
     *     }
     * </pre>
     * Note that it is illegal for a {@link com.worksta.backend.data.WorkstaJobPosting.JobShift JobShift}
     * to have both a fixed amount and hourly rate. In the event that both are supplied,
     * a code {@code 400} will be returned.
     * @param requestBody The request body containing the data for the new job posting.
     * @return a {@link ResponseEntity} containing the newly created {@code WorkstaJobPosting}.
     */
//    @org.springframework.security.access.prepost.PreAuthorize("hasRole('BUSINESS')")
    @PostMapping("/create")
    public ResponseEntity<WorkstaJobPosting> createJobPosting(
            @RequestBody APIRequestBodies.CreateJobPostingRequestBody requestBody
    ) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getName();
        WorkstaBusiness business = businessRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Business account not found for " + email));

        var posting = WorkstaJobPosting.builder()
                .title(requestBody.getTitle())
                .description(requestBody.getDescription())
                .location(requestBody.getLocation())
                .tags(requestBody.getTags())
                .jobShifts(requestBody.getShifts())
                .business(business)
                .build();
        jobPostingRepository.save(posting);
        return ResponseEntity.ok(posting);
    }

    /**
     * Creates a new {@link com.worksta.backend.data.WorkstaJobPosting.JobApplication WorkstaJobPosting.JobApplication},
     * based on a given <b>job shift</b> UUID.
     * This API route can only be called by workers.
     * @return whether the user applied successfully.
     */
    @PostMapping("/{id}/apply/")
    public ResponseEntity<?> createJobApplication(
            @PathVariable UUID id,
            @RequestBody APIRequestBodies.CreateJobApplicationRequestBody requestBody
    ) {
        WorkstaJobPosting.JobShift jobShift = jobShiftRepository.findDistinctFirstById(id);
        if (jobShift == null) {
            return ResponseEntity.notFound().build();
        }

        String email = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getName();
        UUID workerId = workerRepository.findByEmail(email)
                .map(com.worksta.backend.data.user.WorkstaWorker::getId)
                .orElseThrow(() -> new IllegalStateException("Worker account not found for " + email));

        jobShift.getJobApplications().add(WorkstaJobPosting.JobApplication.builder()
                .coverMessage(requestBody.coverMessage)
                .accepted(false)
                .workerID(workerId)
                .build());

        jobShiftRepository.save(jobShift);
        return ResponseEntity.ok().build();
    }

    /**
     * A container class for defining various API request bodies related to the Jobs API.
     * These classes encapsulate the data structures used in request payloads for the API endpoints.
     */
    private static final class APIRequestBodies {
        @Data
        public static final class CreateJobPostingRequestBody {
            private String title;
            private String description;
            private String location;
            private List<String> tags;
            private List<WorkstaJobPosting.JobShift> shifts;
        }
        @Data
        public static final class CreateJobApplicationRequestBody {
            private String coverMessage;
        }
    }

}
