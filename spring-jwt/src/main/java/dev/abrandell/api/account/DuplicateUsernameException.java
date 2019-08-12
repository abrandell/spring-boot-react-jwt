package dev.abrandell.api.account;

public class DuplicateUsernameException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    DuplicateUsernameException(String username) {
        super(String.format("Account with username: '%s' already exists", username));
    }
}
