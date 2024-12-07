package ir.jamareh.tiktok_aa.model.job;

import ir.jamareh.tiktok_aa.model.account.TiktokAccount;
import ir.jamareh.tiktok_aa.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "jobs")
public class TiktokJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 25)
    private String title;

    @Column(nullable = false, length = 300)
    private String description;

    @Column
    private String query;

    @Column(nullable = false)
    private Long startTime;

    @Column(nullable = false)
    private Long lastStartTime;

    @Column(nullable = false)
    private Long endTime;

    @Column(nullable = false)
    private Long LastEndTime;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "jobs_accounts", // Name of the join table
            joinColumns = @JoinColumn(name = "job_id"), // Foreign key for Job
            inverseJoinColumns = @JoinColumn(name = "account_id") // Foreign key for Account
    )
    private List<TiktokAccount> accounts;
}
