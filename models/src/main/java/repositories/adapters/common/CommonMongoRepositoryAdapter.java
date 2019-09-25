package repositories.adapters.common;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public class CommonMongoRepositoryAdapter<T, V> implements JpaRepository<T, V> {
    private static final String NOT_SUPPORTED_METHOD = "Not supported method";
    private MongoRepository<T, V> mongoRepository;

    public CommonMongoRepositoryAdapter(MongoRepository<T, V> mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public List<T> findAll() {
        return mongoRepository.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public List<T> findAllById(Iterable<V> iterable) {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public long count() {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public void deleteById(V v) {
        mongoRepository.deleteById(v);
    }

    @Override
    public void delete(T t) {
        mongoRepository.delete(t);
    }

    @Override
    public void deleteAll(Iterable<? extends T> iterable) {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public void deleteAll() {
        mongoRepository.deleteAll();
    }

    @Override
    public <S extends T> S save(S s) {
        return mongoRepository.save(s);
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> iterable) {
        return mongoRepository.saveAll(iterable);
    }

    @Override
    public Optional<T> findById(V v) {
        return mongoRepository.findById(v);
    }

    @Override
    public boolean existsById(V v) {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public void flush() {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public <S extends T> S saveAndFlush(S s) {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public void deleteInBatch(Iterable<T> iterable) {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public void deleteAllInBatch() {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public T getOne(V v) {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        throw new RuntimeException(NOT_SUPPORTED_METHOD);
    }
}
