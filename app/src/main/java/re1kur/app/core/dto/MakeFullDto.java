package re1kur.app.core.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@Setter
public class MakeFullDto {
    public Integer id;
    public String name;
    public String country;
    public String description;
    public LocalDate foundedAt;
    public String founder;
    public String owner;
    public String titleImgUrl;
}
