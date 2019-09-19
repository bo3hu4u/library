package lib_group.library.services.interfaces;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface LibraryService<T, V> {
    ResponseEntity getById(V Id);

    List<T> getAll();

    ResponseEntity delete(V Id);

    void deleteAll();

    ResponseEntity save(T obj);

    List<T> saveAll(Set<T> objects);
}
