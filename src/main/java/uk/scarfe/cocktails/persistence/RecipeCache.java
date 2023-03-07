package uk.scarfe.cocktails.persistence;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import uk.scarfe.cocktails.model.Recipe;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile("in-memory")
public class RecipeCache implements DataStore<Recipe, Long> {

    private final Map<Long, Recipe> cache = new ConcurrentHashMap<>();

    @Override
    public <S extends Recipe> S save(S entity) {
        if (entity.getId() == null) {
            final long nextId = cache.keySet().stream().mapToLong(k -> k).max().orElse(-1) + 1;
            entity.setId(nextId);
        }
        cache.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Recipe> findById(Long id) {
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
    public void deleteById(Long id) {
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
