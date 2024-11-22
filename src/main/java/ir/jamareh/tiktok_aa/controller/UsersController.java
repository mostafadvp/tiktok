package ir.jamareh.tiktok_aa.controller;

import ir.jamareh.tiktok_aa.model.Role;
import ir.jamareh.tiktok_aa.model.user.User;
import ir.jamareh.tiktok_aa.model.user.UserDTO;
import ir.jamareh.tiktok_aa.repositories.RoleRepository;
import ir.jamareh.tiktok_aa.repositories.UserRepository;
import ir.jamareh.tiktok_aa.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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
     * @param user
     * @return inserted user id
     */
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setEnabled(true);

        Role defaultRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(Collections.singleton(defaultRole));

        User insertedUser = userRepository.save(user);
        if (insertedUser.getId() != -1) {
            return "{id:" + insertedUser.getId() + "}";
        } else return "{error:insert new user failed}";
    }

    /**
     * delete user
     *
     * @param id user id
     * @return deleted user id
     */
    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userRepository.deleteById(id);
        return "{id:" + id + "}";
    }

    /**
     * check if user is enabled
     *
     * @param id user id
     * @return user status
     */
    @GetMapping("/status")
    public String statusUser(@RequestParam("id") Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) return "{id:" + id + ", enabled:" + user.isEnabled() + "}";
        else return "{error:user not found}";
    }

    /**
     * change user status
     *
     * @param id      user id
     * @param enabled new status of user
     * @return user status
     */
    @GetMapping("/enable")
    public String enableUser(@RequestParam("id") Long id, @RequestParam("enabled") boolean enabled) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "{error:user not found}";
        user.setEnabled(enabled);
        userRepository.save(user);
        return "{id:" + id + ", enable:" + enabled + "}";
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String role) {
        List<UserDTO> users = userService.getUsersByRoleName(role);
        return ResponseEntity.ok(users);
    }
}
