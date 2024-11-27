package ir.jamareh.tiktok_aa.job;

/**
 * to implement job status change callback
 */
public interface JobLifecycleListener {
    void onJobStarted(Long jobId);

    void onJobStopped(Long jobId);
}
