package re1kur.app.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.dto.RentalDto;
import re1kur.app.core.other.RentalAdminFilter;
import re1kur.app.core.other.RentalFilter;
import re1kur.app.core.payload.RentalPayload;

import java.util.List;
import java.util.UUID;

public interface RentalService {
    UUID create(RentalPayload payload, UUID userId);

    RentalDto readById(UUID rentalId, OidcUser user);

    PageDto<RentalDto> readAllByUser(Pageable pageable, UUID userId, RentalFilter filter);

    List<Integer> readCarIdsByUser(UUID userId);

    PageDto<RentalDto> readAll(Pageable pageable, RentalAdminFilter filter, OidcUser user);
}
