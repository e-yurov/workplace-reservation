package com.rc.mentorship.workplace_reservation.repository.impl.jpa;

import com.rc.mentorship.workplace_reservation.repository.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

class JpaRepositoryAdapter<T, ID> implements Repository<T, ID> {
    private final JpaRepository<T, ID> jpaRepository;

    public JpaRepositoryAdapter(JpaRepository<T, ID> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<T> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<T> findById(ID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public T save(T item) {
        return jpaRepository.save(item);
    }

    @Override
    public void removeById(ID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void removeAll() {
        jpaRepository.deleteAll();
    }
}
