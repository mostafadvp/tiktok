package ir.jamareh.tiktok_aa.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.jamareh.tiktok_aa.TiktokResponse;
import ir.jamareh.tiktok_aa.model.user.Role;
import ir.jamareh.tiktok_aa.model.user.User;
import ir.jamareh.tiktok_aa.model.user.UserDTO;
import ir.jamareh.tiktok_aa.repositories.RoleRepository;
import ir.jamareh.tiktok_aa.repositories.UserRepository;
import ir.jamareh.tiktok_aa.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/users")
public class UsersController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;


    public UsersController(UserRepository userRepository, RoleRepository roleRepository, UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    /**
     * registering new user
     *
     * @param user new user
     * @return inserted user id
     */
    @PostMapping("/register")
    public ResponseEntity<TiktokResponse<String>> register(@RequestBody User user) throws JsonProcessingException {
        log.info("register api called");
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setEnabled(true);

        Role defaultRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(Collections.singleton(defaultRole));

        if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
            User insertedUser = userRepository.save(user);
            Map<String, Long> responseMap = new HashMap<>();
            if (insertedUser.getId() != -1) {
                responseMap.put("id", insertedUser.getId());
                TiktokResponse<String> tiktokResponse = new TiktokResponse<>(true, "new user added", new ObjectMapper().writeValueAsString(responseMap));
                log.info("new user registered successfully with id:{}", insertedUser.getId());
                return ResponseEntity.ok(tiktokResponse);
            } else {
                log.error("new user could not be added due to database problem");
                return ResponseEntity.ok(new TiktokResponse<>(false, "insert new user failed", null));
            }
        }
        return ResponseEntity.ok(new TiktokResponse<>(false, "user with same username exists", null));
    }

    /**
     * delete user
     *
     * @param id user id
     * @return deleted user id
     */
    @GetMapping("/delete")
    public ResponseEntity<TiktokResponse<String>> deleteUser(@RequestParam("id") Long id) {
        log.info("deleteUser api Called");
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User with id:{} deleted successfully", id);
            return ResponseEntity.ok(new TiktokResponse<>(true, "user deleted", null));
        }
        log.warn("User with id:{} not found", id);
        return new ResponseEntity<>(new TiktokResponse<>(false, "user not found", null), HttpStatus.NOT_FOUND);
    }

    /**
     * check if user is enabled
     *
     * @param id user id
     * @return user status
     */
    @GetMapping("/status")
    public ResponseEntity<TiktokResponse<String>> statusUser(@RequestParam("id") Long id) throws JsonProcessingException {
        log.info("statusUser api Called, userId:{}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("id", id);
            responseMap.put("status", user.get().isEnabled());
            return ResponseEntity.ok(new TiktokResponse<>(true, "user status", new ObjectMapper().writeValueAsString(responseMap)));
        }
        return new ResponseEntity<>(new TiktokResponse<>(false, "user not found", null), HttpStatus.NOT_FOUND);
    }

    /**
     * change user status
     *
     * @param id      user id
     * @param enabled new status of user
     * @return user status
     */
    @GetMapping("/enable")
    public ResponseEntity<TiktokResponse<String>> enableUser(@RequestParam("id") Long id, @RequestParam("enabled") boolean enabled) throws JsonProcessingException {
        log.info("enableUser api Called, userId:{}", id);
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEnabled(enabled);
            userRepository.save(user);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("id", id);
            responseMap.put("enabled", enabled);

            return ResponseEntity.ok(new TiktokResponse<>(true, "user status", new ObjectMapper().writeValueAsString(responseMap)));
        } else {
            return new ResponseEntity<>(new TiktokResponse<>(false, "user not found", null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<TiktokResponse<List<UserDTO>>> getUsersByRole(@PathVariable String role) {
        log.info("getUsersByRole:{} api Called", role);
        List<UserDTO> users = userService.getUsersByRoleName(role);
        return ResponseEntity.ok(new TiktokResponse<>(true, "users by role", users));
    }
}
