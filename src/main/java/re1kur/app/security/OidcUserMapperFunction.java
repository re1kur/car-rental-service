//package re1kur.app.security;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
//import org.springframework.security.oauth2.core.oidc.OidcIdToken;
//import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
//import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import re1kur.app.service.UserService;
//
//import java.util.List;
//import java.util.function.BiFunction;
//
//
//@Slf4j
//public class OidcUserMapperFunction implements BiFunction<OidcUserRequest, OidcUserInfo, OidcUser> {
//
//    public UserService service;
//
//    public OidcUserMapperFunction(UserService service) {
//        this.service = service;
//    }
//
//    /**
//     * Applies this function to the given arguments.
//     *
//     * @param userRequest the first function argument
//     * @param userInfo        the second function argument
//     * @return the function result
//     */
//    @Override
//    public OidcUser apply(OidcUserRequest userRequest, OidcUserInfo userInfo) {
//        OidcIdToken idToken = userRequest.getIdToken();
//        service.writeIfNotExists(userInfo);
//        log.info("userRequest : {}", idToken.getClaims().toString());
//        List<GrantedAuthority> authorities = idToken.getClaimAsStringList("custom-realm-roles")
//                .stream()
//                .map(role -> role.replace("ROLE_", ""))
//                .map(SimpleGrantedAuthority::new)
//                .map(GrantedAuthority.class::cast)
//                .toList();
//        return new DefaultOidcUser(
//                authorities,
//                idToken, "email");
//    }
//}
