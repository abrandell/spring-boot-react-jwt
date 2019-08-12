package dev.abrandell.api.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Value object used for login/sign-up requests.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthRequest {

    @NotBlank
    String username;

    @NotBlank
    String password;
}
