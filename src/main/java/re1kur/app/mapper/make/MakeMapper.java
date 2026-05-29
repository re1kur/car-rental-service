package re1kur.app.mapper.make;

import org.springframework.data.domain.Page;
import re1kur.app.dto.view.MakeFullView;
import re1kur.app.dto.view.MakeView;
import re1kur.app.dto.view.MakeShortView;
import re1kur.app.dto.view.PageView;
import re1kur.app.dto.payload.MakeUpdatePayload;
import re1kur.app.dto.payload.MakePayload;
import re1kur.app.entity.Make;

public interface MakeMapper {

    Make create(MakePayload make);

    Make update(Make make, MakeUpdatePayload payload);

    MakeFullView readFull(Make make);

    MakeView read(Make make);

    MakeShortView readShort(Make make);

    PageView<MakeView> readPage(Page<Make> page);
}
