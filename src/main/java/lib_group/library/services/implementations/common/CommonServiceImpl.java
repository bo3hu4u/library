package lib_group.library.services.implementations.common;

import lib_group.library.exceptions.NotFoundEntityException;
import lib_group.library.services.interfaces.common.ICommonService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public abstract class CommonServiceImpl<T, V> implements ICommonService<T, V> {
    public abstract JpaRepository<T, V> getRepository();

    public abstract Class<T> getCurrentClass();

    public T getById(V Id) {
        T obj = getRepository().findById(Id).orElse(null);
        if (obj == null) {
            throw new NotFoundEntityException("Can't find " + getCurrentClass().getSimpleName() + " with id " + Id);
        }
        return obj;
    }

    public List<T> getAll() {
        return getRepository().findAll();
    }

    public void deleteAll() {
        getRepository().deleteAll();
    }

    public T save(T obj) {
        return getRepository().save(obj);
    }

    public List<T> saveAll(Set<T> objects) {
        return getRepository().saveAll(objects);
    }
}
