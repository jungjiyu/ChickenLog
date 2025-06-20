package com.fizz.fizz_server.memo.entity;

import com.fizz.fizz_server.memo.dto.request.MemoRequestDto;
import com.fizz.fizz_server.store.entity.Store;
import com.fizz.fizz_server.user.dto.request.UserRequestDto;
import com.fizz.fizz_server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Memo {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void update(MemoRequestDto dto) {
        if (dto.getContent() != null) {
            this.content = dto.getContent();
        }
    }


}
