package ir.jamareh.tiktok_aa.job;

import ir.jamareh.tiktok_aa.model.job.JobStatus;
import ir.jamareh.tiktok_aa.repositories.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
/**
 * handle start and stop job callbacks
 */
public class JobLifecycleCallback implements JobLifecycleListener {
    private final JobRepository jobRepository;

    public JobLifecycleCallback(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public void onJobStarted(Long jobId) {
        // Update the job's status in the database to "Running"
        jobRepository.findById(jobId).ifPresent(job -> {
            job.setJobStatus(JobStatus.STARTED);
            jobRepository.save(job);
            log.info("Job {} status updated to Running.", jobId);
        });
    }

    @Override
    public void onJobStopped(Long jobId) {
        // Update the job's status in the database to "Stopped"
        jobRepository.findById(jobId).ifPresent(job -> {
            job.setJobStatus(JobStatus.STOPPED);
            jobRepository.save(job);
            log.info("Job {} status updated to Stopped.", jobId);
        });
    }
}
