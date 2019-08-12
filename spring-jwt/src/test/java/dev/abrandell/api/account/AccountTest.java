package dev.abrandell.api.account;

import dev.abrandell.api.security.Authority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class AccountTest {

    private Account account;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setUsername("Test");
        account.setPassword("password");
        account.setAuthorities(Set.of(new Authority("ROLE_USER")));
        account = accountRepository.save(account);
    }

    @Test
    void accountAndAuthoritiesPersisted() {
        assertAll("account",
            () -> {
                assertNotNull(account);

                // executed only if above passes
                assertAll("authorities persisted",
                    () -> {
                        Set<Authority> authorities = account.getAuthorities();
                        assertTrue(authorities.size() > 0,
                            "Account authorities were not persisted");
                    });
            });
    }
}
