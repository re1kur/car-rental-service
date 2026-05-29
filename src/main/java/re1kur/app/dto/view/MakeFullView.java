package re1kur.app.dto.view;

import lombok.*;

import java.util.List;

@Builder
public record MakeFullView(
         Integer id,
         String name,
         MakeInformationView information,
         FileView titleImage,
         List<FileView> images
) {
}