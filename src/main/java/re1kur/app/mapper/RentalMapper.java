package re1kur.app.mapper;

import org.springframework.data.domain.Page;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.dto.RentalDto;
import re1kur.app.core.payload.RentalPayload;
import re1kur.app.entity.Car;
import re1kur.app.entity.Rental;

import java.util.UUID;

public interface RentalMapper {
    Rental write(RentalPayload payload, Car car, UUID userId);

    RentalDto read(Rental rental);

    PageDto<RentalDto> readPage(Page<Rental> page);
}
