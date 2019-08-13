package dev.abrandell.api.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("username", username)
            .append("password", password)
            .toString();
    }
}
