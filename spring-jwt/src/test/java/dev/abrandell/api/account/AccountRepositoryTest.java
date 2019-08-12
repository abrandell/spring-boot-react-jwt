package dev.abrandell.api.account;

import dev.abrandell.api.security.Authority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setUsername("test");
        account.setPassword("password");
        account.setAuthorities(Set.of(new Authority("ROLE_USER")));
    }

    @Test
    void accountPersisted() {
        assertAll("account and authorities persisted",
            () -> {
                Account savedAccount = accountRepository.save(account);
                assertNotNull(savedAccount);

                assertAll("persisted account properties equal to initial",
                    () -> assertEquals(account, savedAccount,
                        "Account equals/hashcode should be based on UUID only"
                    ),
                    () -> assertNotNull(savedAccount.getId())
                );
            });
    }

    @Test
    void fetchByUsername() {
        accountRepository.save(account);

        assertAll("fetchByUsername",
            () -> {
                Optional<Account> fetchedAccount = accountRepository.fetchByUsername(account.getUsername());
                assertTrue(fetchedAccount.isPresent());

                assertEquals(account, fetchedAccount.get());
            });
    }
}
