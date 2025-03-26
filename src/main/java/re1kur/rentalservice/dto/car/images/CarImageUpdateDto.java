package re1kur.rentalservice.dto.car.images;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarImageUpdateDto {
    private Integer id;

    private String url;

    private String bucket;

    private MultipartFile image;

    private LocalDateTime uploadedAt;
}
