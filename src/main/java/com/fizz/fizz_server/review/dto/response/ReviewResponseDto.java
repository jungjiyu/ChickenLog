package com.fizz.fizz_server.review.dto.response;

import com.fizz.fizz_server.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponseDto {
    private Long id;
    private String writer;
    private int rating;
    private String postedAt;
    private String comment;
    private Long storeId;

    public static ReviewResponseDto fromEntity(Review review) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .writer(review.getWriter())
                .rating(review.getRating())
                .postedAt(review.getPostedAt())
                .comment(review.getComment())
                .storeId(review.getStore().getId())
                .build();
    }
}
