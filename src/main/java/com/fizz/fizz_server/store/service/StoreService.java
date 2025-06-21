package com.fizz.fizz_server.store.service;

import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import com.fizz.fizz_server.global.base.response.exception.ExceptionType;
import com.fizz.fizz_server.menu.dto.response.MenuResponseDto;
import com.fizz.fizz_server.menu.entity.Menu;
import com.fizz.fizz_server.menu.repository.MenuRepository;
import com.fizz.fizz_server.review.dto.response.ReviewResponseDto;
import com.fizz.fizz_server.review.entity.Review;
import com.fizz.fizz_server.review.repository.ReviewRepository;
import com.fizz.fizz_server.store.dto.request.StoreRequestDto;
import com.fizz.fizz_server.store.dto.response.SimpleStoreResponseDto;
import com.fizz.fizz_server.store.dto.response.StoreResponseDto;
import com.fizz.fizz_server.store.entity.Store;
import com.fizz.fizz_server.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;

    public StoreResponseDto createStore(StoreRequestDto requestDto) {
        if (storeRepository.findByExternalStoreId(requestDto.getExternalStoreId()).isPresent()) {
            throw new BusinessException(ExceptionType.DUPLICATED_EXTERNAL_STORE_ID);
        }
        Store store = requestDto.toEntity();
        Store savedStore = storeRepository.save(store);

        log.info("Store created: id={}, name={}", savedStore.getId(), savedStore.getName());
        return StoreResponseDto.fromEntity(savedStore);
    }

    public StoreResponseDto updateStore(Long id, StoreRequestDto requestDto) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionType.STORE_NOT_FOUND));

        store.update(requestDto);

        log.info("Store updated: id={}, updatedFields={}", id, requestDto);
        return StoreResponseDto.fromEntity(store);
    }

    public void deleteStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionType.STORE_NOT_FOUND));

        storeRepository.delete(store);
        log.info("Store deleted: id={}", id);
    }

    @Transactional(readOnly = true)
    public List<SimpleStoreResponseDto> getAllStores() {
        return storeRepository.findAll()
                .stream()
                .map(SimpleStoreResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StoreResponseDto getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionType.STORE_NOT_FOUND));

        List<Menu> menus = menuRepository.findByStoreId(id);
        List<Review> reviews = reviewRepository.findByStoreId(id);

        StoreResponseDto response = StoreResponseDto.fromEntity(store);

        response.getMenus().addAll(
                menus.stream()
                        .map(MenuResponseDto::fromEntity)
                        .collect(Collectors.toList())
        );

        response.getReviews().addAll(
                reviews.stream()
                        .map(ReviewResponseDto::fromEntity)
                        .collect(Collectors.toList())
        );

        return response;
    }


    /**
     * 크롤링 데이터를 db 에 반영하는 메서드
     * @param dto
     * @return
     */
    public Store upsertStore(StoreRequestDto dto) {
        Optional<Store> existing = storeRepository.findByExternalStoreId(dto.getExternalStoreId());

        if (existing.isPresent()) {
            Store store = existing.get();
            store.update(dto);
            log.info("Updated store: {}", store.getName());
            return store;
        } else {
            Store newStore = storeRepository.save(dto.toEntity());
            log.info("Inserted new store: {}", newStore.getName());
            return newStore;
        }
    }








}
