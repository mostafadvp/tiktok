package ir.jamareh.tiktok_aa.service;

import ir.jamareh.tiktok_aa.model.account.TiktokAccount;
import ir.jamareh.tiktok_aa.model.user.User;
import ir.jamareh.tiktok_aa.model.user.UserDTO;
import ir.jamareh.tiktok_aa.repositories.AccountRepository;
import ir.jamareh.tiktok_aa.repositories.UserRepository;
import ir.jamareh.tiktok_aa.security.service.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * manage user things like login and register
 */
@Slf4j
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public TiktokAccount getAccount(long id) {
        return accountRepository.findById(id).orElse(null);
    }
}
