package ir.jamareh.tiktok_aa.model.account;

import ir.jamareh.tiktok_aa.model.job.TiktokJob;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "accounts")
public class TiktokAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "accounts")
    private List<TiktokJob> jobs;
}
