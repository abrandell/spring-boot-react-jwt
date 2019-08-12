package dev.abrandell.api.account;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
@Transactional(rollbackFor = Exception.class)
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepo;

    public AccountService(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Supplier<String> exceptionMessage = () -> String.format("Account with username '%s' not found", username);

        return accountRepo.fetchByUsername(username)
            .orElseThrow(() ->
                new UsernameNotFoundException(exceptionMessage.get())
            );
    }
}
