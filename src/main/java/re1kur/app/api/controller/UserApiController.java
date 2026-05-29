package re1kur.app.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import re1kur.app.api.ApiMapper;
import re1kur.app.api.dto.RentalResponse;
import re1kur.app.core.other.RentalFilter;
import re1kur.app.service.RentalService;

import java.util.List;
import java.util.UUID;

@Tag(name = "Users", description = "User rental history")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserApiController {

    private static final int MAX = 1000;

    private final RentalService rentalService;
    private final ApiMapper mapper;

    @Operation(summary = "Get a user's rental history — self or admin only")
    @GetMapping("/{id}/rentals")
    public List<RentalResponse> rentals(@PathVariable String id, @AuthenticationPrincipal OidcUser user) {
        boolean admin = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
        if (!admin && !user.getSubject().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can only view your own rentals.");
        }

        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user id.");
        }

        return rentalService.readAllByUser(PageRequest.of(0, MAX), userId, RentalFilter.builder().build())
                .content().stream()
                .map(mapper::rental)
                .toList();
    }
}
