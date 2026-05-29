package re1kur.app.mapper.rental;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import re1kur.app.util.annotations.Mapper;
import re1kur.app.dto.view.FileView;
import re1kur.app.dto.view.PageView;
import re1kur.app.dto.view.RentalView;
import re1kur.app.dto.payload.RentalPayload;
import re1kur.app.entity.Car;
import re1kur.app.entity.Rental;
import re1kur.app.mapper.file.FileMapper;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Mapper
@RequiredArgsConstructor
public class RentalMapperImpl implements RentalMapper {
    private final FileMapper fileMapper;

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
    public RentalView read(Rental rental) {
        Car car = rental.getCar();
        FileView image = fileMapper.read(car.getTitleImage());
        return RentalView.builder()
                .id(rental.getId())
                .userId(rental.getUserId())
                .carId(car.getId())
                .carMake(car.getMake().getName())
                .carModel(car.getModel())
                .carImageUrl(image != null ? image.url() : null)
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .totalCost(rental.getTotalCost())
                .build();
    }

    @Override
    public PageView<RentalView> readPage(Page<Rental> page) {
        boolean hasNext = page.hasNext();
        boolean hasPrevious = page.hasPrevious();
        return new PageView<>(
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
