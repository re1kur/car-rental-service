package re1kur.app.mapper.rental;

import org.springframework.data.domain.Page;
import re1kur.app.core.annotations.Mapper;
import re1kur.app.model.dto.PageDto;
import re1kur.app.model.dto.RentalDto;
import re1kur.app.model.payload.RentalPayload;
import re1kur.app.model.entity.Car;
import re1kur.app.model.entity.Rental;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Mapper
public class RentalMapperImpl implements RentalMapper {
    @Override
    public Rental write(RentalPayload payload, Car car, UUID userId) {
        LocalDate endDate = payload.endDate();
        LocalDate startDate = payload.startDate();
        int rentDays = (Period.between(startDate, endDate)).getDays() + 1;
        return Rental.builder()
                .car(car)
                .endDate(endDate)
                .startDate(startDate)
                .userId(userId)
                .totalCost(car.getCost() * rentDays)
                .build();
    }

    @Override
    public RentalDto read(Rental rental) {
        return RentalDto.builder()
                .id(rental.getId())
                .userId(rental.getUserId())
                .carId(rental.getCar().getId())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .totalCost(rental.getTotalCost())
                .build();
    }

    @Override
    public PageDto<RentalDto> readPage(Page<Rental> page) {
        boolean hasNext = page.hasNext();
        boolean hasPrevious = page.hasPrevious();
        return new PageDto<>(
                page.getContent().stream().map(this::read).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                hasNext ? page.nextPageable().getPageNumber() : 0,
                hasPrevious ? page.previousPageable().getPageNumber() : 0,
                hasNext ? page.nextOrLastPageable().getPageNumber() : 0,
                hasPrevious ? page.previousOrFirstPageable().getPageNumber() : 0
        );
    }
}
