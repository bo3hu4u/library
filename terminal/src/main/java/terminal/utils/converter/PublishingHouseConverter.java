package terminal.utils.converter;

import models.PublishingHouse;
import org.springframework.stereotype.Component;
import terminal.utils.converter.common.BaseObjectConverter;

@Component
public class PublishingHouseConverter extends BaseObjectConverter<PublishingHouse> {
    @Override
    public Class<PublishingHouse> getThisClass() {
        return PublishingHouse.class;
    }
}