package services.interfaces.common;

import java.util.List;
import java.util.Set;

public interface ICommonService<T, V> {

    T getById(V Id);

    List<T> getAll();

    void delete(V Id);

    void deleteAll();

    T save(T obj);

    List<T> saveAll(Set<T> objects);
}
