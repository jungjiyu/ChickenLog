package com.fizz.fizz_server.menu.repository;

import com.fizz.fizz_server.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStoreId(Long storeId);

    void deleteAllByStoreId(Long storeId);
}