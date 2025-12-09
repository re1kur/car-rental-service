package re1kur.app.mapper.rental;

import org.springframework.data.domain.Page;
import re1kur.app.model.dto.PageDto;
import re1kur.app.model.dto.RentalDto;
import re1kur.app.model.payload.RentalPayload;
import re1kur.app.model.entity.Car;
import re1kur.app.model.entity.Rental;

import java.util.UUID;

public interface RentalMapper {
    Rental write(RentalPayload payload, Car car, UUID userId);

    RentalDto read(Rental rental);

    PageDto<RentalDto> readPage(Page<Rental> page);
}
