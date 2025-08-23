package re1kur.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.dto.RentalDto;
import re1kur.app.core.exception.CarIsNotAvailableException;
import re1kur.app.core.exception.CarIsOccupiedException;
import re1kur.app.core.exception.RentalNotFoundException;
import re1kur.app.core.other.RentalAdminFilter;
import re1kur.app.core.other.RentalFilter;
import re1kur.app.core.payload.RentalPayload;
import re1kur.app.entity.Car;
import re1kur.app.entity.Rental;
import re1kur.app.mapper.RentalMapper;
import re1kur.app.repository.RentalRepository;
import re1kur.app.service.CarService;
import re1kur.app.service.RentalService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final CarService carService;

    @Override
    @Transactional
    public UUID create(RentalPayload payload, UUID userId) {
        log.info("CREATE RENTAL [{}] REQUEST BY USER [{}]", payload, userId);
        Car car = carService.getById(payload.carId());
        checkConflicts(car, payload);
        Rental rental = rentalMapper.write(payload, car, userId);
        Rental saved = rentalRepository.save(rental);

        UUID rentalId = saved.getId();
        log.info("RENTAL [{}] CREATED BY USER [{}]", rentalId, userId);
        return rentalId;
    }

    private void checkConflicts(Car car, RentalPayload payload) {
        Integer carId = car.getId();
        if (!car.isAvailable()) {
            throw new CarIsNotAvailableException("Car [%s] is not available for rent.".formatted(carId));
        }

        LocalDate startDate = payload.startDate();
        LocalDate endDate = payload.endDate();
        if (rentalRepository.existsByCarIdAndDate(carId, startDate, endDate))
            throw new CarIsOccupiedException("Car [%s] is occupied between %s and %s".formatted(carId, startDate, endDate));
    }

    @Override
    public RentalDto readById(UUID rentalId, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("READ RENTAL [{}] REQUEST BY USER [{}]", rentalId, logUser);

        return rentalRepository.findById(rentalId)
                .map(rentalMapper::read)
                .orElseThrow(() -> new RentalNotFoundException("Rental [%s] was not found.".formatted(rentalId)));
    }

    @Override
    public PageDto<RentalDto> readAllByUser(Pageable pageable, UUID userId, RentalFilter filter) {
        log.info("READ USER'S RENTALS REQUEST BY USER [{}]", userId);
        Integer carId = filter.carId();

        LocalDate date = filter.date();

        Page<Rental> page = date != null ? rentalRepository.findAllByUserIdAndDate(pageable, userId, carId, date) :
                rentalRepository.findAllByUserId(pageable, userId, carId);
        return rentalMapper.readPage(page);
    }

    @Override
    public List<Integer> readCarIdsByUser(UUID userId) {
        return rentalRepository.findAllCarIdsByUserId(userId);
    }

    @Override
    public PageDto<RentalDto> readAll(Pageable pageable, RentalAdminFilter filter, OidcUser user) {
        String logUser = user == null ? "Anonymous" : user.getSubject();
        log.info("READ RENTALS PAGE BY FILTER [{}] REQUEST BY USER [{}]", filter, logUser);

        UUID userId = filter.userId();
        Integer carId = filter.carId();

        LocalDate localDate = filter.date();
        Date date = localDate != null ? Date.valueOf(localDate) : null;

        Page<Rental> page = date != null ? rentalRepository.findAllByUserIdAndCarIdAndDate(pageable, userId, carId, date) :
                rentalRepository.findAllByUserIdAndCarId(pageable, userId, carId);
        return rentalMapper
                .readPage(page);
    }
}
