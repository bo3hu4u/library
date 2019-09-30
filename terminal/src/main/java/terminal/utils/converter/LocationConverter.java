package terminal.utils.converter;

import models.Location;
import org.springframework.stereotype.Component;
import terminal.utils.converter.common.BaseObjectConverter;

@Component
public class LocationConverter extends BaseObjectConverter<Location> {
    @Override
    public Class<Location> getThisClass() {
        return Location.class;
    }
}
