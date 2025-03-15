package re1kur.rentalservice.dto.make;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MakeReadDto {
    private int id;
    private String name;
    private String country;
    private String description;
}
