package com.fizz.fizz_server.store.controller;


import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.global.base.response.ResponseUtil;
import com.fizz.fizz_server.store.dto.request.StoreRequestDto;
import com.fizz.fizz_server.store.dto.response.SimpleStoreResponseDto;
import com.fizz.fizz_server.store.dto.response.StoreResponseDto;
import com.fizz.fizz_server.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<ResponseBody<StoreResponseDto>> createStore( @RequestBody StoreRequestDto requestDto) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                storeService.createStore(requestDto)
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBody<StoreResponseDto>> updateStore(
            @PathVariable Long id,
            @RequestBody StoreRequestDto requestDto) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                storeService.updateStore(id, requestDto)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody<Void>> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse());
    }

    @GetMapping
    public ResponseEntity<ResponseBody<List<SimpleStoreResponseDto>>> getAllStores() {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                storeService.getAllStores()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBody<StoreResponseDto>> getStoreById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                storeService.getStoreById(id)
        ));
    }


}
