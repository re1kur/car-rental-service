package re1kur.app.api;

import org.springframework.stereotype.Component;
import re1kur.app.api.dto.*;
import re1kur.app.core.dto.CarDto;
import re1kur.app.core.dto.CarFullDto;
import re1kur.app.core.dto.CarInformationDto;
import re1kur.app.core.dto.MakeDto;
import re1kur.app.core.dto.MakeFullDto;
import re1kur.app.core.dto.MakeInformationDto;
import re1kur.app.core.dto.RentalDto;
import re1kur.app.entity.CarType;
import re1kur.app.entity.Engine;

import java.util.List;

@Component
public class ApiMapper {

    public CarTypeResponse carType(CarType type) {
        return new CarTypeResponse(type.name(), type.getLabel());
    }

    public EngineResponse engine(Engine engine) {
        return new EngineResponse(engine.name(), engine.getLabel());
    }

    /** List view — no detailed characteristics. */
    public CarResponse carSummary(CarDto car) {
        return new CarResponse(
                car.id(),
                car.make() != null ? car.make().id() : null,
                car.make() != null ? car.make().name() : null,
                car.model(),
                car.year(),
                car.licensePlate(),
                car.cost(),
                true,
                carType(car.carType()),
                engine(car.engine()),
                car.titleImage() != null ? car.titleImage().url() : null,
                null, null, null, null, null, null, null
        );
    }

    /** Detail view — includes characteristics and gallery. */
    public CarResponse carDetail(CarFullDto car) {
        CarInformationDto info = car.information();
        List<String> images = car.images() == null ? List.of()
                : car.images().stream().map(f -> f.url()).toList();
        return new CarResponse(
                car.id(),
                car.make() != null ? car.make().id() : null,
                car.make() != null ? car.make().name() : null,
                car.model(),
                car.year(),
                car.licensePlate(),
                car.cost(),
                car.available(),
                carType(car.carType()),
                engine(car.engine()),
                car.titleImage() != null ? car.titleImage().url() : null,
                info != null ? info.description() : null,
                info != null ? info.color() : null,
                info != null ? info.seats() : null,
                info != null ? info.mileage() : null,
                info != null ? info.fuelType() : null,
                info != null ? info.transmission() : null,
                images
        );
    }

    public MakeResponse makeSummary(MakeDto make) {
        return new MakeResponse(
                make.id(),
                make.name(),
                make.titleImage() != null ? make.titleImage().url() : null,
                null, null, null, null, null, null
        );
    }

    public MakeResponse makeDetail(MakeFullDto make) {
        MakeInformationDto info = make.information();
        List<String> images = make.images() == null ? List.of()
                : make.images().stream().map(f -> f.url()).toList();
        return new MakeResponse(
                make.id(),
                make.name(),
                make.titleImage() != null ? make.titleImage().url() : null,
                info != null ? info.country() : null,
                info != null ? info.description() : null,
                info != null ? info.foundedAt() : null,
                info != null ? info.founder() : null,
                info != null ? info.owner() : null,
                images
        );
    }

    public RentalResponse rental(RentalDto rental) {
        return new RentalResponse(
                rental.id(),
                rental.carId(),
                rental.carMake(),
                rental.carModel(),
                rental.carImageUrl(),
                rental.startDate(),
                rental.endDate(),
                rental.totalCost()
        );
    }
}
