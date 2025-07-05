//package re1kur.app.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//
//@Configuration
//public class SecurityConfiguration {
//
//    @Bean
//    public DefaultSecurityFilterChain configure(HttpSecurity http) throws Exception {
//        return http
//                .csrf(Customizer.withDefaults())
//                .cors(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(manager ->
//                        manager
//                                .requestMatchers(
//                                        "/css/**",
//                                        "favicon.ico",
//                                        "/users/register",
//                                        "/users/login",
//                                        "/users/logout").permitAll()
//                                .requestMatchers(
//                                        "/cars/create",
//                                        "/cars/{id}/edit",
//                                        "/makes/create",
//                                        "/makes/{id}/edit").hasAuthority("ADMIN")
//                                .anyRequest().authenticated())
//                .formLogin(login ->
//                        login
//                                .loginPage("/users/login")
//                                .defaultSuccessUrl("/"))
//                .logout(logout ->
//                        logout
//                                .logoutUrl("/users/logout")
//                                .logoutSuccessUrl("/users/login"))
//                .build();
//    }
//

//}
