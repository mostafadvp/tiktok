package ir.jamareh.tiktok_aa.controller;

import ir.jamareh.tiktok_aa.TiktokResponse;
import ir.jamareh.tiktok_aa.model.user.User;
import ir.jamareh.tiktok_aa.security.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    public final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<TiktokResponse<String>> login(@RequestBody User user) {
        String token = userService.verify(user);
        if (token != null) {
            TiktokResponse<String> response = new TiktokResponse<>(true, "User authenticated successfully", token);
            return ResponseEntity.ok(response);
        } else {
            TiktokResponse<String> response = new TiktokResponse<>(false, "User authenticated failed", null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
