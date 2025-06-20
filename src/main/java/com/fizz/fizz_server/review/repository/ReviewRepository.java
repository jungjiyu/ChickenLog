package com.fizz.fizz_server.review.repository;

import com.fizz.fizz_server.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStoreId(Long storeId);
}