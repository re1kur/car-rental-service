package re1kur.app.core.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import re1kur.app.core.annotations.EmptyOrSize;

import java.time.LocalDate;

@Builder
public record MakeUpdatePayload(
        @NotBlank(message = "Name cannot be empty or contain backspaces.")
        @Size(message = "Name have to be between 64 and 3 characters long.", min = 3, max = 64)
        String name,

        @EmptyOrSize(message = "Country have to be between 32 and 3 characters long.", min = 3, max = 32)
        String country,

        String description,

        LocalDate foundedAt,

        @EmptyOrSize(message = "Founder have to be between 6 and 64 characters long.", min = 6, max = 64)
        String founder,

        @EmptyOrSize(message = "Owner have to be between 6 and 64 characters long.", min = 6, max = 64)
        String owner,

        String titleImageId
) {
}
