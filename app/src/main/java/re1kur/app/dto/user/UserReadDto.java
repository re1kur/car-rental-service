package re1kur.app.dto.user;

import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UserReadDto {
    String id;
    String email;
    String fullName;
    boolean isEmailVerified;

}
