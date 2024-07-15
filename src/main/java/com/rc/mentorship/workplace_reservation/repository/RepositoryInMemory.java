package com.rc.mentorship.workplace_reservation.repository;

import com.rc.mentorship.workplace_reservation.entity.EntityInMemory;
import com.rc.mentorship.workplace_reservation.entity.Office;

import java.util.*;

class RepositoryInMemory<T extends EntityInMemory> {
    private final Map<UUID, T> storage = new HashMap<>();

    public List<T> findAll() {
        return List.copyOf(storage.values());
    }

    public Optional<T> findById(UUID id) {
        return Optional.of(storage.get(id));
    }

    public T save(T item) {
        if (item.getId() == null) {
            item.setId(UUID.randomUUID());
        }
        return storage.put(item.getId(), item);
    }

    public T update(T item) {
        return storage.put(item.getId(), item);
    }

    public void deleteById(UUID id) {
        storage.remove(id);
    }

    public void deleteAll() {
        storage.clear();
    }
}
