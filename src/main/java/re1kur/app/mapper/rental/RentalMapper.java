package re1kur.app.mapper.rental;

import org.springframework.data.domain.Page;
import re1kur.app.dto.view.PageView;
import re1kur.app.dto.view.RentalView;
import re1kur.app.dto.payload.RentalPayload;
import re1kur.app.entity.Car;
import re1kur.app.entity.Rental;

import java.util.UUID;

public interface RentalMapper {
    Rental write(RentalPayload payload, Car car, UUID userId);

    RentalView read(Rental rental);

    PageView<RentalView> readPage(Page<Rental> page);
}
