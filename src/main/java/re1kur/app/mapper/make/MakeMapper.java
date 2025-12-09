package re1kur.app.mapper.make;

import org.springframework.data.domain.Page;
import re1kur.app.model.dto.MakeFullDto;
import re1kur.app.model.dto.MakeDto;
import re1kur.app.model.dto.MakeShortDto;
import re1kur.app.model.dto.PageDto;
import re1kur.app.model.payload.MakeUpdatePayload;
import re1kur.app.model.payload.MakePayload;
import re1kur.app.model.entity.Make;

public interface MakeMapper {

    Make create(MakePayload make);

    Make update(Make make, MakeUpdatePayload payload);

    MakeFullDto readFull(Make make);

    MakeDto read(Make make);

    MakeShortDto readShort(Make make);

    PageDto<MakeDto> readPage(Page<Make> page);
}
