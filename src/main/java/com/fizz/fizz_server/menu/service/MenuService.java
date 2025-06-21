package com.fizz.fizz_server.menu.service;

import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import com.fizz.fizz_server.global.base.response.exception.ExceptionType;
import com.fizz.fizz_server.menu.dto.request.MenuRequestDto;
import com.fizz.fizz_server.menu.dto.response.MenuResponseDto;
import com.fizz.fizz_server.menu.entity.Menu;
import com.fizz.fizz_server.menu.repository.MenuRepository;
import com.fizz.fizz_server.store.entity.Store;
import com.fizz.fizz_server.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    public MenuResponseDto createMenu(Long storeId, MenuRequestDto requestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(ExceptionType.STORE_NOT_FOUND));

        Menu menu = requestDto.toEntity(store);
        Menu saved = menuRepository.save(menu);
        return MenuResponseDto.fromEntity(saved);
    }

    public MenuResponseDto updateMenu(Long menuId, MenuRequestDto requestDto) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ExceptionType.MENU_NOT_FOUND));

        menu.update(requestDto);
        return MenuResponseDto.fromEntity(menu);
    }

    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ExceptionType.MENU_NOT_FOUND));
        menuRepository.delete(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> getMenusByStoreId(Long storeId) {
        List<Menu> menus = menuRepository.findByStoreId(storeId);
        return menus.stream()
                .map(MenuResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MenuResponseDto getMenuById(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ExceptionType.MENU_NOT_FOUND));
        return MenuResponseDto.fromEntity(menu);
    }


    public void upsertMenus(Long storeid, List<MenuRequestDto> menuDtos) {
        log.info("upsertMenus called for storeId: {}", storeid);

        // Store 존재 여부 확인
        Store store = storeRepository.findById(storeid).orElseThrow(() ->
                new BusinessException(ExceptionType.STORE_NOT_FOUND));
        log.info("Store found: {}", store.getName());

        // 기존 메뉴 삭제
        log.info("Deleting existing menus for storeId: {}", storeid);
        menuRepository.deleteAllByStoreId(storeid);

        // 새로운 메뉴 저장

        log.info("menudtos : {}",menuDtos);
        if (menuDtos.isEmpty()) {
            log.warn("No menu items found for storeId: {}", storeid);
        }

        menuDtos.stream()
                .map(dto -> {
                    log.info("Converting MenuRequestDto to Menu entity for menu: {}", dto.getName());
                    return dto.toEntity(store);
                })
                .forEach(menu -> {
                    log.info("Saving menu: {}", menu.getName());
                    menuRepository.save(menu);
                });

        log.info("Upserted {} menu items for storeId: {}", menuDtos.size(), storeid);
    }

}