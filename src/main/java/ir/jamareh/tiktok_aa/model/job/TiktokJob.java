package ir.jamareh.tiktok_aa.model.job;

import ir.jamareh.tiktok_aa.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

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
}
