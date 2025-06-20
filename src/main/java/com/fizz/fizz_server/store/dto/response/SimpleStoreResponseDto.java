package com.fizz.fizz_server.store.dto.response;
import com.fizz.fizz_server.menu.dto.response.MenuResponseDto;
import com.fizz.fizz_server.review.dto.response.ReviewResponseDto;
import com.fizz.fizz_server.store.entity.Store;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


/**
 * 리뷰와 메뉴를 생략한 간략한
 */
@Getter
@Builder
public class SimpleStoreResponseDto {
    private Long id;
    private Long externalStoreId;
    private String name;
    private String openHour;
    private String address;

    public static SimpleStoreResponseDto fromEntity(Store store) {
        return SimpleStoreResponseDto.builder()
                .id(store.getId())
                .externalStoreId(store.getExternalStoreId())
                .name(store.getName())
                .openHour(store.getOpenHour())
                .address(store.getAddress())
                .build();
    }
}