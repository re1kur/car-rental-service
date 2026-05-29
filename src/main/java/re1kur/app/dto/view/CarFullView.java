package re1kur.app.dto.view;

import lombok.Builder;
import re1kur.app.entity.CarType;
import re1kur.app.entity.Engine;

import java.util.List;

@Builder
public record CarFullView(
        Integer id,
        String model,
        Boolean available,
        Integer year,
        String licensePlate,
        MakeShortView make,
        CarType carType,
        Engine engine,
        FileView titleImage,
        List<FileView> images,
        CarDetailsView information,
        Integer cost
) {
}
