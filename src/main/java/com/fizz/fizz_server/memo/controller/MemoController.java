package com.fizz.fizz_server.memo.controller;



import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.global.base.response.ResponseUtil;
import com.fizz.fizz_server.memo.dto.request.MemoRequestDto;
import com.fizz.fizz_server.memo.dto.response.MemoResponseDto;
import com.fizz.fizz_server.memo.service.MemoService;
import com.fizz.fizz_server.security.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @PostMapping
    public ResponseEntity<ResponseBody<MemoResponseDto>> createMemo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MemoRequestDto requestDto) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                memoService.createMemo(userDetails.getId(), requestDto)
        ));
    }

    @GetMapping
    public ResponseEntity<ResponseBody<List<MemoResponseDto>>> getAllMemos(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                memoService.getAllMemos(userDetails.getId(), userDetails.getId())
        ));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ResponseBody<List<MemoResponseDto>>> getMemosByStoreId(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long storeId) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                memoService.getMemosByStoreId(userDetails.getId(), storeId)
        ));
    }

    @GetMapping("/{memoId}")
    public ResponseEntity<ResponseBody<MemoResponseDto>> getMemoById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long memoId) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                memoService.getMemoById(userDetails.getId(), memoId)
        ));
    }

    @PutMapping("/{memoId}")
    public ResponseEntity<ResponseBody<MemoResponseDto>> updateMemo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long memoId,
            @RequestBody MemoRequestDto requestDto) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                memoService.updateMemo(userDetails.getId(), memoId, requestDto)
        ));
    }

    @DeleteMapping("/{memoId}")
    public ResponseEntity<ResponseBody<Void>> deleteMemo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long memoId) {
        memoService.deleteMemo(userDetails.getId(), memoId);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse());
    }
}

