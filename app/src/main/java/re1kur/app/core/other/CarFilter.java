package re1kur.app.core.other;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarFilter {
    private Integer makeId;
    private String model;
    private Integer year;
}
