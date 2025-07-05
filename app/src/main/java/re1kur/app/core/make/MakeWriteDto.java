package re1kur.app.core.make;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
public class MakeWriteDto {
    @NotBlank(message = "The make name mustn't only name with backspace chars.")
    @NotNull(message = "The make name have to be.")
    @Size(message = "The make name have to be lesser and greater than 64 and 3 chars respectively.", min = 3, max = 64)
    private String name;

    @NotBlank(message = "The country haven't to be only name with backspace chars.")
    @Size(message = "The country haven't to be lesser and greater than 32 and 3 chars respectively. ", min = 3, max = 32)
    private String country;

    private String description;

    private String titleImageUrl;

    private MultipartFile image;
}
