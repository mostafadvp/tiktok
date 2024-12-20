package ir.jamareh.tiktok_aa.repositories;

import ir.jamareh.tiktok_aa.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = :role")
    List<User> findByRole(@Param("role") String role);

    @Query("SELECT COUNT(j) FROM TiktokJob j WHERE j.owner.id = :userId")
    int countJobsByUserId(@Param("userId") Long userId);
}
