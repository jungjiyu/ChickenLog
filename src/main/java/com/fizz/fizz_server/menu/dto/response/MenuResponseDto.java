package com.fizz.fizz_server.menu.dto.response;


import com.fizz.fizz_server.menu.entity.Menu;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuResponseDto {
    private Long id;
    private String name;
    private String price;
    private String description;
    private Long storeId;

    public static MenuResponseDto fromEntity(Menu menu) {
        return MenuResponseDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .storeId(menu.getStore().getId())
                .build();
    }
}