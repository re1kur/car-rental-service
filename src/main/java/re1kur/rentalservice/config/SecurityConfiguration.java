package re1kur.rentalservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public DefaultSecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .formLogin(loginConfig ->
                        loginConfig.successForwardUrl("/cars/list"))
                .csrf(csrfConfig -> {
                    csrfConfig.ignoringRequestMatchers("/users/register", "/login");
                })
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(manager ->
                        manager
                                .requestMatchers("/users/register")
                                .not().authenticated()
                                .requestMatchers(
                                        "/cars/create",
                                        "/cars/edit",
                                        "/makes/create").hasAuthority("ADMIN")
                                .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
