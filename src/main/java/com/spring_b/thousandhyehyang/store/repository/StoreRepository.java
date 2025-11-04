package com.spring_b.thousandhyehyang.store.repository;

import com.spring_b.thousandhyehyang.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
}

