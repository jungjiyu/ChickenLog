package com.fizz.fizz_server.review.service;

import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import com.fizz.fizz_server.global.base.response.exception.ExceptionType;
import com.fizz.fizz_server.menu.dto.request.MenuRequestDto;
import com.fizz.fizz_server.review.dto.request.ReviewRequestDto;
import com.fizz.fizz_server.review.dto.response.ReviewResponseDto;
import com.fizz.fizz_server.review.entity.Review;
import com.fizz.fizz_server.review.repository.ReviewRepository;
import com.fizz.fizz_server.store.entity.Store;
import com.fizz.fizz_server.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    public ReviewResponseDto createReview(Long storeId, ReviewRequestDto requestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ExceptionType.STORE_NOT_FOUND));

        Review review = requestDto.toEntity(store);
        Review saved = reviewRepository.save(review);
        return ReviewResponseDto.fromEntity(saved);
    }

    public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ExceptionType.REVIEW_NOT_FOUND));

        review.update(requestDto);
        return ReviewResponseDto.fromEntity(review);
    }

    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ExceptionType.REVIEW_NOT_FOUND));

        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByStoreId(Long storeId) {
        return reviewRepository.findByStoreId(storeId)
                .stream()
                .map(ReviewResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReviewResponseDto getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ExceptionType.REVIEW_NOT_FOUND));
        return ReviewResponseDto.fromEntity(review);
    }

    public void upsertReviews(Long storeid, List<ReviewRequestDto> reviewDtos) {
        Store store = storeRepository.findById(storeid).orElseThrow(()
                -> new BusinessException(ExceptionType.STORE_NOT_FOUND));

        reviewRepository.deleteAllByStoreId(storeid);

        reviewDtos.stream()
                .map(dto -> dto.toEntity(store))
                .forEach(reviewRepository::save);
    }
}