package ir.jamareh.tiktok_aa.repositories;

import ir.jamareh.tiktok_aa.model.job.TiktokJob;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<TiktokJob, Long> {
    @Query("SELECT COUNT(j) > 0 FROM TiktokJob j WHERE j.id = :jobId AND j.owner.id = :userId")
    boolean existsByJobIdAndUserId(@Param("jobId") Long jobId, @Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(j) > 0 THEN true ELSE false END " +
            "FROM TiktokJob j WHERE j.id = :jobId AND j.jobStatus = 'STOPPED'")
    boolean isJobStopped(@Param("jobId") Long jobId);

    @Modifying
    @Transactional
    @Query("UPDATE TiktokJob j SET j.jobStatus = 'STOPPING' WHERE j.id = :jobId")
    void updateJobStatusToStopping(@Param("jobId") Long jobId);
}
