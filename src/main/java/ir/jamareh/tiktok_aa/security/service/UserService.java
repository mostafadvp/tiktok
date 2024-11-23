package ir.jamareh.tiktok_aa.security.service;

import ir.jamareh.tiktok_aa.model.user.User;
import ir.jamareh.tiktok_aa.model.user.UserDTO;
import ir.jamareh.tiktok_aa.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * manage user things like login and register
 */
@Service
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    public UserService(AuthenticationManager authenticationManager, JWTService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public String verify(User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(user.getUsername());
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<UserDTO> getUsersByRoleName(String role) {
        String roleName = switch (role) {
            case "user" -> "ROLE_USER";
            case "admin" -> "ROLE_ADMIN";
            default -> "";
        };

        List<User> users = userRepository.findByRole(roleName);
        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.isEnabled()))
                .collect(Collectors.toList());
    }
}
