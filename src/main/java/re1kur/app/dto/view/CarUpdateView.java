package re1kur.app.dto.view;

import lombok.Builder;
import re1kur.app.entity.CarType;
import re1kur.app.entity.Engine;

import java.util.List;

@Builder
public record CarUpdateView(
        Integer id,
        MakeShortView make,
        CarType carType,
        Engine engine,
        String model,
        Integer year,
        String licensePlate,
        CarDetailsView information,
        FileView titleImage,
        List<FileView> images,
        Boolean available,
        Integer cost
) {
}
