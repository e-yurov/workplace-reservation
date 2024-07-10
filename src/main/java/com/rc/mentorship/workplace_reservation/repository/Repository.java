package com.rc.mentorship.workplace_reservation.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    List<T> findAll();

    Optional<T> findById(ID id);

    T save(T item);

    void removeById(ID id);

    void removeAll();
}
