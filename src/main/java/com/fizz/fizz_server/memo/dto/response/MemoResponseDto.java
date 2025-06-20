package com.fizz.fizz_server.memo.dto.response;

import com.fizz.fizz_server.memo.entity.Memo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemoResponseDto {
    private Long id;
    private String content;
    private Long storeId;
    private Long userId;

    public static MemoResponseDto fromEntity(Memo memo) {
        return MemoResponseDto.builder()
                .id(memo.getId())
                .content(memo.getContent())
                .storeId(memo.getStore().getId())
                .userId(memo.getUser().getId())
                .build();
    }
}
