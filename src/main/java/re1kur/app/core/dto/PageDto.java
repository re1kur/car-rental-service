package re1kur.app.core.dto;

import java.util.List;

public record PageDto<T>(
        List<T> content,
        Integer number,
        Integer size,
        Integer totalCount,
        Integer nextNumber,
        Integer prevNumber,
        Integer lastNumber,
        Integer firstNumber
) {
}
