package ir.jamareh.tiktok_aa.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.jamareh.tiktok_aa.TiktokResponse;
import ir.jamareh.tiktok_aa.model.account.TiktokAccount;
import ir.jamareh.tiktok_aa.model.user.Role;
import ir.jamareh.tiktok_aa.model.user.User;
import ir.jamareh.tiktok_aa.model.user.UserDTO;
import ir.jamareh.tiktok_aa.repositories.AccountRepository;
import ir.jamareh.tiktok_aa.repositories.RoleRepository;
import ir.jamareh.tiktok_aa.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/user/accounts")
public class AccountsController {
    private final AccountRepository accountRepository;


    public AccountsController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @PostMapping("/add")
    public ResponseEntity<TiktokResponse<String>> save(@RequestBody TiktokAccount account) throws JsonProcessingException {
        log.info("save account api called");
        account.setEnabled(true);
        if (accountRepository.findByUsername(account.getUsername()).isEmpty()) {
            TiktokAccount insertedUser = accountRepository.save(account);
            Map<String, Long> responseMap = new HashMap<>();
            if (insertedUser.getId() != -1) {
                responseMap.put("id", insertedUser.getId());
                TiktokResponse<String> tiktokResponse = new TiktokResponse<>(true, "new account added", new ObjectMapper().writeValueAsString(responseMap));
                log.info("new account added successfully with id:{}", insertedUser.getId());
                return ResponseEntity.ok(tiktokResponse);
            } else {
                log.error("new account could not be added due to database problem");
                return ResponseEntity.ok(new TiktokResponse<>(false, "insert new account failed", null));
            }
        }
        return ResponseEntity.ok(new TiktokResponse<>(false, "account with same username exists", null));
    }
}
