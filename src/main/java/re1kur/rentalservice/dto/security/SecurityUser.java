package re1kur.rentalservice.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import re1kur.rentalservice.entity.Role;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUser implements UserDetails {
    private Integer id;
    private String username;
    private String password;
    private Collection<SecurityRole> authorities;

    @Override
    public Collection<SecurityRole> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
