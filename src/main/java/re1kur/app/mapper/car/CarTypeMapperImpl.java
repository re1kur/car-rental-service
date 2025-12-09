package re1kur.app.mapper.car;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import re1kur.app.model.dto.CarTypeDto;
import re1kur.app.model.dto.PageDto;
import re1kur.app.model.payload.CarTypePayload;
import re1kur.app.model.payload.CarTypeUpdatePayload;
import re1kur.app.model.entity.CarType;

@Component
public class CarTypeMapperImpl implements CarTypeMapper {
    @Override
    public CarType create(CarTypePayload payload) {
        return CarType.builder()
                .name(payload.name())
                .build();
    }

    @Override
    public CarTypeDto read(CarType carType) {
        return CarTypeDto.builder()
                .id(carType.getId())
                .name(carType.getName())
                .build();
    }

    @Override
    public CarType update(CarType carType, CarTypeUpdatePayload payload) {
        carType.setName(payload.name());
        return carType;
    }

    @Override
    public PageDto<CarTypeDto> readPage(Page<CarType> page) {
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
