package re1kur.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MakeResponse(
        Integer id,
        String name,
        String imageUrl,
        // detail-only (null in list view)
        String country,
        String description,
        LocalDate foundedAt,
        String founder,
        String owner,
        List<String> images
) {
}
