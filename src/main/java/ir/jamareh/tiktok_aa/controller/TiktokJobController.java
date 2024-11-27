package ir.jamareh.tiktok_aa.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.jamareh.tiktok_aa.TiktokResponse;
import ir.jamareh.tiktok_aa.model.job.JobStatus;
import ir.jamareh.tiktok_aa.model.job.TiktokJob;
import ir.jamareh.tiktok_aa.model.user.User;
import ir.jamareh.tiktok_aa.service.UserService;
import ir.jamareh.tiktok_aa.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user/job")
public class TiktokJobController {
    private final JobService jobService;
    private final UserService userService;

    public TiktokJobController(JobService jobService, UserService userService) {
        this.jobService = jobService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<TiktokResponse<String>> addJob(@RequestBody TiktokJob job) throws JsonProcessingException {
        log.info("Add job: {} called", job.getTitle());
        try {
            User user = userService.getAuthenticatedUser();
            job.setOwner(user);
            job.setLastStartTime(0L);
            job.setLastEndTime(0L);
            job.setJobStatus(JobStatus.STOPPED);
            long jobId = jobService.addJob(job);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("id", jobId);
            log.info("job with id:{} added", jobId);
            return ResponseEntity.ok(new TiktokResponse<>(true, "job added successfully", new ObjectMapper().writeValueAsString(responseMap)));
        } catch (IllegalArgumentException e) {
            log.error("Could not add job: {}", e.getMessage());
            return new ResponseEntity<>(new TiktokResponse<>(false, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/delete")
    public ResponseEntity<TiktokResponse<String>> delete(@RequestParam Long id) {
        log.info("Delete job: {} called", id);
        try {
            if (jobService.isJobExists(id)) {
                User user = userService.getAuthenticatedUser();
                if (jobService.isJobBelongToUser(id, user.getId())) {
                    if (jobService.isJobStopped(id)) {
                        jobService.deleteJob(id);
                        log.info("job with id:{} deleted", id);
                        return ResponseEntity.ok(new TiktokResponse<>(true, "job deleted successfully", null));
                    } else {
                        return ResponseEntity.ok(new TiktokResponse<>(false, "this job is not stopped", null));
                    }
                } else {
                    log.warn("job with id:{} not belong to user", id);
                    return ResponseEntity.ok(new TiktokResponse<>(false, "this job is not belong to you", null));
                }
            } else {
                log.warn("Job with id:{} does not exist", id);
                return new ResponseEntity<>(new TiktokResponse<>(false, "Job not found", null), HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            log.error("Could not delete job: {}", e.getMessage());
            return new ResponseEntity<>(new TiktokResponse<>(false, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/start")
    public ResponseEntity<TiktokResponse<String>> start(@RequestParam Long id) {
        log.info("Start job: {} called", id);
        try {
            if (jobService.isJobExists(id)) {
                User user = userService.getAuthenticatedUser();
                if (jobService.isJobBelongToUser(id, user.getId())) {
                    jobService.startJob(id);
                    return ResponseEntity.ok(new TiktokResponse<>(true, "job started successfully", null));
                } else {
                    log.warn("job with id:{} not belong to user", id);
                    return ResponseEntity.ok(new TiktokResponse<>(false, "this job is not belong to you", null));
                }
            } else {
                log.warn("Job with id:{} does not exist", id);
                return new ResponseEntity<>(new TiktokResponse<>(false, "Job not found", null), HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            log.error("Could not start job: {}", e.getMessage());
            return new ResponseEntity<>(new TiktokResponse<>(false, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/stop")
    public ResponseEntity<TiktokResponse<String>> stop(@RequestParam Long id) {
        log.info("Stop job: {} called", id);
        try {
            if (jobService.isJobExists(id)) {
                User user = userService.getAuthenticatedUser();
                if (jobService.isJobBelongToUser(id, user.getId())) {
                    jobService.stopJob(id);
                    return ResponseEntity.ok(new TiktokResponse<>(true, "job stopped successfully", null));
                } else {
                    log.warn("job with id:{} not belong to user", id);
                    return ResponseEntity.ok(new TiktokResponse<>(false, "this job is not belong to you", null));
                }
            } else {
                log.warn("Job with id:{} does not exist", id);
                return new ResponseEntity<>(new TiktokResponse<>(false, "Job not found", null), HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            log.error("Could not stop job: {}", e.getMessage());
            return new ResponseEntity<>(new TiktokResponse<>(false, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}