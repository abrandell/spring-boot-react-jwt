package dev.abrandell.api.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void fetchByUsernameThrowsCorrectException() {
        final String nameNotUsed = "NAME_THAT_DOESNT_EXIST";
        given(accountRepository.fetchByUsername(nameNotUsed)).willReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
            () -> accountService.loadUserByUsername(nameNotUsed)
        );
    }
}
