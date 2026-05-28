package re1kur.app.core.other;

import lombok.Builder;
import lombok.Data;
import re1kur.app.entity.CarType;
import re1kur.app.entity.Engine;

@Data
@Builder
public class CarFilter {
    private Integer makeId;
    private String model;
    private Integer year;
    private CarType carType;
    private Engine engine;
}
