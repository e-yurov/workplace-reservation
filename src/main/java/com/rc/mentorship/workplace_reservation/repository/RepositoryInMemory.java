package com.rc.mentorship.workplace_reservation.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class RepositoryInMemory<T, ID> {
    private final Map<ID, T> storage = new HashMap<>();

    public List<T> findAll() {
        return List.copyOf(storage.values());
    }

    public Optional<T> findById(ID id) {
        return Optional.of(storage.get(id));
    }

    public T add(T item, ID id) {
        return storage.put(id, item);
    }

    public void deleteById(ID id) {
        storage.remove(id);
    }

    public void deleteAll() {
        storage.clear();
    }
}
