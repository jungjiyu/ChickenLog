package com.fizz.fizz_server.store.dto.response;
import com.fizz.fizz_server.menu.dto.response.MenuResponseDto;
import com.fizz.fizz_server.menu.entity.Menu;
import com.fizz.fizz_server.review.dto.response.ReviewResponseDto;
import com.fizz.fizz_server.review.entity.Review;
import com.fizz.fizz_server.store.entity.Store;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class StoreResponseDto {
    private Long id;
    private Long externalStoreId;
    private String name;
    private String openHour;
    private String address;
    private Long reviewCount;

    private List<MenuResponseDto> menus = new ArrayList<>();
    private List<ReviewResponseDto> reviews = new ArrayList<>();

    public static StoreResponseDto fromEntity(Store store) {
        return StoreResponseDto.builder()
                .id(store.getId())
                .externalStoreId(store.getExternalStoreId())
                .name(store.getName())
                .openHour(store.getOpenHour())
                .address(store.getAddress())
                .reviewCount(store.getReviewCount())
                .build();
    }
}