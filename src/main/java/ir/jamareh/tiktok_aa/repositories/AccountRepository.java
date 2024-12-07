package ir.jamareh.tiktok_aa.repositories;

import ir.jamareh.tiktok_aa.model.account.TiktokAccount;
import ir.jamareh.tiktok_aa.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<TiktokAccount, Long> {
    Optional<TiktokAccount> findByUsername(String username);
}
