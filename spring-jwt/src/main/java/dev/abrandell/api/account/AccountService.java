package dev.abrandell.api.account;

import dev.abrandell.api.security.Authority;
import dev.abrandell.api.security.AuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.function.Supplier;

@Service
@Transactional(
    rollbackFor = Exception.class,
    timeout = 30
)
public class AccountService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepo;

    public AccountService(AccountRepository accountRepo, PasswordEncoder passwordEncoder,
                          AuthorityRepository authorityRepo) {
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepo = authorityRepo;
    }

    public Account createAccount(AuthRequest authRequest) {
        final String username = authRequest.getUsername();

        if (accountRepo.existsByUsername(username)) {
            throw new DuplicateUsernameException(username);
        }

        LOGGER.debug("Creating account...");

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(authRequest.getPassword()));

        Authority userAuthority = authorityRepo.fetchByName("ROLE_USER")
            .orElseGet(() ->
                authorityRepo.save(new Authority("ROLE_USER"))
            );

        account.setAuthorities(Set.of(userAuthority));
        LOGGER.debug("Created account: {}", account);
        return accountRepo.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Account loadUserByUsername(String username) throws UsernameNotFoundException {
        Supplier<String> exceptionMessage = () -> String.format("Account with username '%s' not found", username);

        return accountRepo.fetchByUsername(username)
            .orElseThrow(() ->
                new UsernameNotFoundException(exceptionMessage.get())
            );
    }
}
