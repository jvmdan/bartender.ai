package uk.scarfe.cocktails.persistence;

import java.util.Collection;
import java.util.Optional;

/**
 * A generic interface used to represent a repository for storing data. Implementing classes may connect to
 * an underlying database, a separate microservice or simply store data within a hash map. Regardless,
 * this interface provides a common contract for retrieving data from a repository.
 *
 * @param <T>  the type of data we are storing.
 * @param <ID> the ID or key for storing the value against.
 */
public interface DataStore<T, ID> {

    <S extends T> S save(S entity);

    Optional<T> findById(ID id);

    Collection<T> findAll();

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAll(Collection<T> entities);

    void deleteAll();

}
