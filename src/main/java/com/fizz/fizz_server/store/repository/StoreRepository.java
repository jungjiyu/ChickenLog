package com.fizz.fizz_server.store.repository;

import com.fizz.fizz_server.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByExternalStoreId(Long externalStoreId);
    Optional<Store> findByName(String name);

}
