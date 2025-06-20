package com.fizz.fizz_server.review.controller;


import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.global.base.response.ResponseUtil;
import com.fizz.fizz_server.review.dto.request.ReviewRequestDto;
import com.fizz.fizz_server.review.dto.response.ReviewResponseDto;
import com.fizz.fizz_server.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{storeId}")
    public ResponseEntity<ResponseBody<ReviewResponseDto>> createReview(
            @PathVariable Long storeId,
            @RequestBody ReviewRequestDto requestDto) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                reviewService.createReview(storeId, requestDto)
        ));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ResponseBody<ReviewResponseDto>> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto requestDto) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                reviewService.updateReview(reviewId, requestDto)
        ));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ResponseBody<Void>> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ResponseBody<List<ReviewResponseDto>>> getReviewsByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                reviewService.getReviewsByStoreId(storeId)
        ));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ResponseBody<ReviewResponseDto>> getReviewById(@PathVariable Long reviewId) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                reviewService.getReviewById(reviewId)
        ));
    }
}