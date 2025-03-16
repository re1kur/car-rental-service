package re1kur.rentalservice.dto.make;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MakeWriteDto {
    @NotBlank
    @NotNull
    @Size(min = 3, max = 64)
    private String name;

    @NotBlank
    @Size(min = 3, max = 32)
    private String country;

    private String description;
}
