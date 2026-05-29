package re1kur.app.dto.view;

import java.util.List;

public record PageView<T>(
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
