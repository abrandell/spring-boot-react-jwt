package dev.abrandell.api;

import dev.abrandell.api.account.AccountService;
import dev.abrandell.api.account.AuthRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJwtApplication.class, args);
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner init(AccountService accountService) {
        return (args) ->
            accountService.createAccount(
                new AuthRequest("user", "password")
            );
    }

    // Placed in main class to avoid circular dependencies.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
