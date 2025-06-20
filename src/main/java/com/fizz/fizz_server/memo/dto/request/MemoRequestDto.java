package com.fizz.fizz_server.memo.dto.request;

import com.fizz.fizz_server.memo.entity.Memo;
import com.fizz.fizz_server.store.entity.Store;
import com.fizz.fizz_server.user.entity.User;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoRequestDto {
    private String content;
    private Long storeId;
//    private Long userId;

    public Memo toEntity(User user, Store store) {
        return Memo.builder()
                .content(content)
                .user(user)
                .store(store)
                .build();
    }
}

