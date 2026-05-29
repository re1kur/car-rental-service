package re1kur.app.dto.view;

import lombok.Builder;
import re1kur.app.entity.CarType;
import re1kur.app.entity.Engine;

@Builder
public record CarView(
        Integer id,
        String model,
        Integer year,
        String licensePlate,
        Integer cost,
        Boolean available,
        MakeShortView make,
        CarType carType,
        Engine engine,
        FileView titleImage) {
}
