package com.fizz.fizz_server.menu.entity;

import com.fizz.fizz_server.menu.dto.request.MenuRequestDto;
import com.fizz.fizz_server.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String price;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;


    public void update(MenuRequestDto dto) {
        if (dto.getName() != null) {
            this.name = dto.getName();
        }
        if (dto.getPrice() != null) {
            this.price = dto.getPrice();
        }
        if (dto.getDescription() != null) {
            this.description = dto.getDescription();
        }
    }



}

