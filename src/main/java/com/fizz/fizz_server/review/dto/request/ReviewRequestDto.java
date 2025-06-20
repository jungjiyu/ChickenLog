package com.fizz.fizz_server.review.dto.request;

import com.fizz.fizz_server.review.entity.Review;
import com.fizz.fizz_server.store.entity.Store;
import com.fizz.fizz_server.user.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDto {
    private String writer;
    private Integer rating;
    private String postedAt;
    private String comment;
    private Long storeId;

    public Review toEntity(Store store) {
        return Review.builder()
                .writer(writer)
                .rating(rating)
                .postedAt(postedAt)
                .comment(comment)
                .store(store)
                .build();
    }
}