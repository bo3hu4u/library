package terminal.utils.converter.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.IOException;

public abstract class BaseObjectConverter<T> implements Converter<String, T> {

    public abstract Class<T> getThisClass();

    @Override
    public T convert(String json) {
        ObjectMapper mapper = new ObjectMapper();
        T author = null;
        try {
            author = mapper.readValue(json, getThisClass());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return author;
    }
}
