package ai.bartender.persistence;

import ai.bartender.model.Recipe;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryStore implements DataStore<Recipe, String> {

    private final Map<String, Recipe> cache = new ConcurrentHashMap<>();

    @Override
    public boolean exists(String id) {
        return cache.containsKey(id);
    }

    @Override
    public <S extends Recipe> S save(S entity) {
        cache.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Recipe> findById(String id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public Collection<Recipe> findAll() {
        return cache.values();
    }

    @Override
    public long count() {
        return cache.size();
    }

    @Override
    public void deleteById(String id) {
        cache.remove(id);
    }

    @Override
    public void delete(Recipe entity) {
        cache.remove(entity.getId());
    }

    @Override
    public void deleteAll(Collection<Recipe> entities) {
        entities.forEach(e -> cache.remove(e.getId()));
    }

    @Override
    public void deleteAll() {
        cache.clear();
    }

}
