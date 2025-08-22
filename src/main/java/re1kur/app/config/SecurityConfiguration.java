package re1kur.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(
                                        "/cars/create",
                                        "/makes/create",
                                        "/car-types/create",
                                        "/engines/create",
                                        "/cars/*/update",
                                        "/makes/*/update",
                                        "/car-types/*/update",
                                        "/engines/*/update",
                                        "/cars/*/delete",
                                        "/car-types/*/delete",
                                        "/engines/*/delete",
                                        "/makes/*/delete",
                                        "/moderator/**",
                                        "/moderator").hasRole("ADMIN")
                                .requestMatchers(
                                        "/",
                                        "/cars",
                                        "/makes",
                                        "/engines",
                                        "/car-types",
                                        "/css/**",
                                        "/api/v1/auth/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui.html",
                                        "/cars/*",
                                        "/makes/*",
                                        "/engines/*",
                                        "/car-types/*",
                                        "/error",
                                        "/error/*").permitAll()
                                .anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID"));
        //todo: logout, http-status-exception-handlers
        return http.build();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
        OidcUserService oidcUserService = new OidcUserService();

        return userRequest -> {
            OidcUser oidcUser = oidcUserService.loadUser(userRequest);
            List<String> roles = oidcUser.getClaimAsStringList("roles");
            Collection<? extends GrantedAuthority> authorities = oidcUser.getAuthorities();

            Collection<? extends GrantedAuthority> mappedAuthorities = Stream.concat(
                    authorities.stream(),
                    roles.stream()
                            .filter(role -> role.startsWith("ROLE_"))
                            .map(SimpleGrantedAuthority::new)
                            .map(GrantedAuthority.class::cast)
            ).toList();

            return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
    }
}
