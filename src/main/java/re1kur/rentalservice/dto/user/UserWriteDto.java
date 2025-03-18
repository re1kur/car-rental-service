package re1kur.rentalservice.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWriteDto {
    @Email
    @NotNull
    @Size(max = 256)
    private String email;

    @NotBlank
    @Size(min = 6, max = 64)
    private String username;

    @NotBlank
    @NotNull
    @Size(min = 6, max = 128)
    private String password;
}
