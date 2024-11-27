package ir.jamareh.tiktok_aa.job;

import ir.jamareh.tiktok_aa.model.job.TiktokJob;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
/*
 * performing job tasks
 */
public class JobRunnable implements Runnable {

    private final TiktokJob job;
    private final JobLifecycleListener lifecycleListener;
    private volatile boolean running = true;

    public JobRunnable(TiktokJob job, JobLifecycleListener lifecycleListener) {
        this.job = job;
        this.lifecycleListener = lifecycleListener;
    }

    @Override
    public void run() {
        try {
            // Delay until the start time
            long now = Instant.now().toEpochMilli();
            if (job.getStartTime() > now) {
                Thread.sleep(job.getStartTime() - now);
            }
            lifecycleListener.onJobStarted(job.getId());
            log.info("Job {}, started at:{}", job.getId(), Instant.now());

            // Run until the end time or until manually stopped
            while (running && (job.getEndTime() == 0 || Instant.now().toEpochMilli() < job.getEndTime())) {
                log.info("Job {}, is running....", job.getId());
                Thread.sleep(1000); // Simulate job execution
            }
            log.info("Job {}, stopped at {}", job.getId(), Instant.now());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Job {} was interrupted", job.getId(), e);
        } finally {
            lifecycleListener.onJobStopped(job.getId());
        }
    }

    public void stop() {
        running = false;
    }
}