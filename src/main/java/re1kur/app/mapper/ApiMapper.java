package re1kur.app.mapper;

import org.springframework.stereotype.Component;
import re1kur.app.dto.response.*;
import re1kur.app.dto.view.CarView;
import re1kur.app.dto.view.CarFullView;
import re1kur.app.dto.view.CarDetailsView;
import re1kur.app.dto.view.MakeView;
import re1kur.app.dto.view.MakeFullView;
import re1kur.app.dto.view.MakeInformationView;
import re1kur.app.dto.view.RentalView;
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

    public CarResponse carSummary(CarView car) {
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
                null, null, null, null, null, null, null
        );
    }

    public CarResponse carDetail(CarFullView car) {
        CarDetailsView info = car.information();
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

    public MakeResponse makeSummary(MakeView make) {
        return new MakeResponse(
                make.id(),
                make.name(),
                make.titleImage() != null ? make.titleImage().url() : null,
                null, null, null, null, null, null
        );
    }

    public MakeResponse makeDetail(MakeFullView make) {
        MakeInformationView info = make.information();
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

    public RentalResponse rental(RentalView rental) {
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
