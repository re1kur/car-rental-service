package re1kur.app.mapper.make;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import re1kur.app.util.annotations.Mapper;
import re1kur.app.dto.view.MakeFullView;
import re1kur.app.dto.view.MakeView;
import re1kur.app.dto.view.MakeShortView;
import re1kur.app.dto.view.PageView;
import re1kur.app.dto.payload.MakeUpdatePayload;
import re1kur.app.dto.payload.MakePayload;
import re1kur.app.entity.File;
import re1kur.app.entity.Make;
import re1kur.app.entity.MakeInformation;
import re1kur.app.mapper.file.FileMapper;

import java.util.List;
import java.util.Objects;

@Slf4j
@Mapper
@RequiredArgsConstructor
public class MakeMapperImpl implements MakeMapper {
    private final FileMapper imageMapper;
    private final MakeInformationMapper infoMapper;

    @Override
    public MakeFullView readFull(Make make) {
        MakeInformation makeInformation = make.getInformation();
        File titleImage = make.getTitleImage();
        List<File> images = make.getImages();

        return MakeFullView.builder()
                .id(make.getId())
                .name(make.getName())
                .information(infoMapper.read(makeInformation))
                .titleImage(imageMapper.read(titleImage))
                .images(images != null ? images.stream().map(imageMapper::read).toList() : List.of())
                .build();
    }

    @Override
    public Make create(MakePayload payload) {
        return Make.builder()
                .name(payload.name())
                .build();
    }

    @Override
    public Make update(Make make, MakeUpdatePayload payload) {
        String newImageId = payload.titleImageId();
        File titleImage = make.getTitleImage();

        make.setName(payload.name());
        make.setInformation(infoMapper.update(make, payload));

        if ((titleImage == null && newImageId != null)
                || (titleImage != null && !Objects.equals(titleImage.getId(), newImageId))
        ) {
            File image = make.getImages().stream()
                    .filter(img -> img.getId().equals(newImageId))
                    .findFirst().orElse(null);

            make.setTitleImage(image);
        }

        return make;
    }

    @Override
    public MakeView read(Make make) {
        File titleImage = make.getTitleImage();
        return MakeView.builder()
                .id(make.getId())
                .name(make.getName())
                .titleImage(imageMapper.read(titleImage))
                .build();
    }

    @Override
    public MakeShortView readShort(Make make) {
        return MakeShortView.builder()
                .id(make.getId())
                .name(make.getName())
                .build();
    }

    @Override
    public PageView<MakeView> readPage(Page<Make> page) {
        boolean hasNext = page.hasNext();
        boolean hasPrevious = page.hasPrevious();
        return new PageView<>(
                page.getContent().stream().map(this::read).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                hasNext ? page.nextPageable().getPageNumber() : 0,
                hasPrevious ? page.previousPageable().getPageNumber() : 0,
                hasNext ? page.nextOrLastPageable().getPageNumber() : 0,
                hasPrevious ? page.previousOrFirstPageable().getPageNumber() : 0
        );
    }
}