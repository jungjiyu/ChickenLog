package com.fizz.fizz_server.menu.controller;

import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.global.base.response.ResponseUtil;
import com.fizz.fizz_server.menu.dto.request.MenuRequestDto;
import com.fizz.fizz_server.menu.dto.response.MenuResponseDto;
import com.fizz.fizz_server.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/{storeId}")
    public ResponseEntity<ResponseBody<MenuResponseDto>> createMenu(
            @PathVariable Long storeId,
            @RequestBody MenuRequestDto requestDto) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                menuService.createMenu(storeId, requestDto)
        ));
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<ResponseBody<MenuResponseDto>> updateMenu(
            @PathVariable Long menuId,
            @RequestBody MenuRequestDto requestDto) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                menuService.updateMenu(menuId, requestDto)
        ));
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<ResponseBody<Void>> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ResponseBody<List<MenuResponseDto>>> getMenusByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                menuService.getMenusByStoreId(storeId)
        ));
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<ResponseBody<MenuResponseDto>> getMenuById(@PathVariable Long menuId) {
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(
                menuService.getMenuById(menuId)
        ));
    }
}