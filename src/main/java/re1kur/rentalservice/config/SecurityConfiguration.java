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
                .csrf(
                        csrfConfig -> {
                            csrfConfig
                                    .ignoringRequestMatchers(
                                            "/users/register",
                                            "/users/login",
                                            "/users/logout");
                        })
                .formLogin(loginConfig ->
                        loginConfig
                                .loginPage("/users/login")
                                .defaultSuccessUrl("/cars/list", true))
                .logout(logoutConfig ->
                        logoutConfig
                                .logoutUrl("/users/logout")
                                .logoutSuccessUrl("/users/login?logout"))
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(manager ->
                        manager
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers(
                                        "/users/register",
                                        "/users/login").not().authenticated()
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
