package com.rc.mentorship.workplace_reservation.repository.impl.in_memory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositoryInMemory<T, ID> {
    private final Map<ID, T> storage = new HashMap<>();

    public List<T> findAll() {
        return List.copyOf(storage.values());
    }

    public Optional<T> findById(ID id) {
        return Optional.of(storage.get(id));
    }

    // проверить добавление неск. элементов с одним айди
    public T add(T item, ID id) {
        return storage.put(id, item);
    }

    public void removeById(ID id) {
        storage.remove(id);
    }

    public void removeAll() {
        storage.clear();
    }
}
