package com.fizz.fizz_server.store.dto.request;

import com.fizz.fizz_server.menu.entity.Menu;
import com.fizz.fizz_server.review.entity.Review;
import com.fizz.fizz_server.store.entity.Store;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRequestDto {
    private Long externalStoreId;
    private String name;
    private String openHour;
    private String address;
    private Long reviewCount;


    public Store toEntity() {
        return Store.builder()
                .externalStoreId(externalStoreId)
                .name(name)
                .openHour(openHour)
                .address(address)
                .reviewCount(reviewCount)
                .build();
    }
}
