package ai.bartender.persistence;

import java.util.Collection;
import java.util.Optional;

public interface DataStore<K, V> {

    V save(V entity);

    Optional<V> findById(K id);

    Collection<V> findAll();

    void delete(V entity);

    void deleteById(K id);

    void deleteAll();

    long count();


}
