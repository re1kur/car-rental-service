package re1kur.rentalservice.dto.user;

import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UserReadDto {
    int id;
    String email;
    String username;

}
