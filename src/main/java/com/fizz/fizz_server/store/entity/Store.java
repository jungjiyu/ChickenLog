package com.fizz.fizz_server.store.entity;

import com.fizz.fizz_server.memo.entity.Memo;
import com.fizz.fizz_server.menu.entity.Menu;
import com.fizz.fizz_server.review.entity.Review;
import com.fizz.fizz_server.store.dto.request.StoreRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Store {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long externalStoreId;

    private String name;
    private String openHour;
    private String address;
    private Long reviewCount;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Memo> memos = new ArrayList<>();

    public void update(StoreRequestDto dto) {
        if (dto.getExternalStoreId() != null) {
            this.externalStoreId = dto.getExternalStoreId();
        }
        if (dto.getName() != null) {
            this.name = dto.getName();
        }
        if (dto.getOpenHour() != null) {
            this.openHour = dto.getOpenHour();
        }
        if (dto.getAddress() != null) {
            this.address = dto.getAddress();
        }
        if (dto.getReviewCount() != null) {
            this.reviewCount = dto.getReviewCount();
        }
    }


}

