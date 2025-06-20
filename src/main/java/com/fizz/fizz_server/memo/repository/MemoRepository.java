package com.fizz.fizz_server.memo.repository;

import com.fizz.fizz_server.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findAllByUserId(Long userId);

    List<Memo> findAllByUserIdAndStoreId(Long userId, Long storeId);

    Optional<Memo> findByIdAndUserId(Long id, Long userId);
}