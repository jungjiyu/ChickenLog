package com.fizz.fizz_server.review.entity;

import com.fizz.fizz_server.review.dto.request.ReviewRequestDto;
import com.fizz.fizz_server.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String writer;
    private Integer rating;
    private String postedAt;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public void update(ReviewRequestDto dto) {
        if (dto.getWriter() != null) {
            this.writer = dto.getWriter();
        }
        if (dto.getRating() != null) {
            this.rating = dto.getRating();
        }
        if (dto.getPostedAt() != null) {
            this.postedAt = dto.getPostedAt();
        }
        if (dto.getComment() != null) {
            this.comment = dto.getComment();
        }
    }


}

