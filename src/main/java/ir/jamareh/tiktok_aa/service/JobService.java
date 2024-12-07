package ir.jamareh.tiktok_aa.service;

import ir.jamareh.tiktok_aa.job.JobLifecycleListener;
import ir.jamareh.tiktok_aa.job.JobRunnable;
import ir.jamareh.tiktok_aa.model.job.JobStatus;
import ir.jamareh.tiktok_aa.model.job.TiktokJob;
import ir.jamareh.tiktok_aa.repositories.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class JobService {
    private final JobRepository jobRepository;
    private final Map<Long, JobRunnable> activeJobs = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final JobLifecycleListener lifecycleListener;

    public JobService(JobLifecycleListener lifecycleListener, JobRepository jobRepository) {
        this.lifecycleListener = lifecycleListener;
        this.jobRepository = jobRepository;
    }

    /**
     * Start a job with specified parameters.
     */
    public void startJob(Long jobId) {
        if (activeJobs.containsKey(jobId)) {
            throw new IllegalArgumentException("Job is already running.");
        }
        TiktokJob tiktokJob = jobRepository.findById(jobId).orElse(null);
        if (tiktokJob != null) {
            tiktokJob.setJobStatus(JobStatus.STARTING);
            jobRepository.save(tiktokJob);
            log.info("Job {} status changed to starting.", jobId);
            JobRunnable jobRunnable = new JobRunnable(tiktokJob, lifecycleListener);
            activeJobs.put(jobId, jobRunnable);
            executorService.submit(jobRunnable);
            log.info("Job {} has been scheduled.", jobId);
        } else throw new IllegalStateException("Job is not found.");
    }

    /**
     * Stop a running job by ID.
     */
    public void stopJob(Long jobId) {
        JobRunnable jobRunnable = activeJobs.remove(jobId);
        if (jobRunnable != null) {
            jobRepository.updateJobStatusToStopping(jobId);
            log.info("Job {} status changed to stopping.", jobId);
            jobRunnable.stop();
        } else {
            throw new IllegalArgumentException("Job not found or already stopped.");
        }
    }

    public boolean isJobBelongToUser(Long jobId, Long userId) {
        return jobRepository.existsByJobIdAndUserId(jobId, userId);
    }

    public boolean isJobStopped(Long jobId) {
        return jobRepository.isJobStopped(jobId);
    }

    public Long addJob(TiktokJob job) {
        return jobRepository.save(job).getId();
    }

    public boolean isJobExists(Long id) {
        return jobRepository.existsById(id);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    public TiktokJob getJob(Long jobId) {
        TiktokJob job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            log.info("Job {} not found.", jobId);
            throw new IllegalArgumentException("Job not found");
        }
        return job;
    }
}
