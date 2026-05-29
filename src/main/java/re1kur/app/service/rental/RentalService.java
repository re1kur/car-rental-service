package re1kur.app.service.rental;

import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import re1kur.app.dto.view.PageView;
import re1kur.app.dto.view.RentalView;
import re1kur.app.dto.filter.RentalAdminFilter;
import re1kur.app.dto.filter.RentalFilter;
import re1kur.app.dto.payload.RentalPayload;

import java.util.List;
import java.util.UUID;

public interface RentalService {
    UUID create(RentalPayload payload, OidcUser userId);

    void createAll(List<RentalPayload> payloads, OidcUser user);

    RentalView readById(UUID rentalId, OidcUser user);

    PageView<RentalView> readAllByUser(Pageable pageable, UUID userId, RentalFilter filter);

    List<Integer> readCarIdsByUser(UUID userId);

    PageView<RentalView> readAll(Pageable pageable, RentalAdminFilter filter, OidcUser user);

    void deleteById(UUID id, OidcUser user);
}
