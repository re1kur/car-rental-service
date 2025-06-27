package re1kur.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.DefaultSecurityFilterChain;
//import re1kur.app.security.OidcUserMapperFunction;
import re1kur.app.service.UserService;


@Configuration
@Slf4j
public class SecurityConfiguration {

    @Bean
    public DefaultSecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
//                .oauth2Login(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(login -> login.loginPage("/users/login"))
                .authorizeHttpRequests(manager ->
                        manager
//                                .requestMatchers("/css/**",
//                                        "/auth/register",
//                                        "/auth/login").permitAll()
//                                .requestMatchers(
//                                        "/cars/create",
//                                        "/cars/{id}/edit",
//                                        "/makes/create",
//                                        "/makes/{id}/edit").hasAuthority("ADMIN")
//                                .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService(UserService service) {
//        OidcUserService oidcUserService = new OidcUserService();
//        oidcUserService.setOidcUserMapper(new OidcUserMapperFunction(service));
//        return oidcUserService;
//    }
}
