package ai.bartender.persistence;

import ai.bartender.model.Recipe;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryStore implements DataStore<String, Recipe> {

    private final Map<String, Recipe> map = new ConcurrentHashMap<>();

    @Override
    public Recipe save(Recipe entity) {
        return map.put(entity.getId(), entity);
    }

    @Override
    public Optional<Recipe> findById(String id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public Collection<Recipe> findAll() {
        return map.values();
    }

    @Override
    public void delete(Recipe entity) {
        map.remove(entity.getId());
    }

    @Override
    public void deleteById(String id) {
        map.remove(id);
    }

    @Override
    public void deleteAll() {
        map.clear();
    }

    @Override
    public long count() {
        return map.size();
    }

}