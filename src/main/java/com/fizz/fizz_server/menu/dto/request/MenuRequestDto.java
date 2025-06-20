package com.fizz.fizz_server.menu.dto.request;

import com.fizz.fizz_server.menu.entity.Menu;
import com.fizz.fizz_server.store.entity.Store;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRequestDto {
    private String name;
    private String price;
    private String description;
    private Long storeId;

    public Menu toEntity(Store store) {
        return Menu.builder()
                .name(name)
                .price(price)
                .description(description)
                .store(store)
                .build();
    }
}

