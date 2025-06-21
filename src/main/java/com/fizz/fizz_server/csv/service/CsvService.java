package com.fizz.fizz_server.csv.service;


import com.fizz.fizz_server.menu.dto.request.MenuRequestDto;
import com.fizz.fizz_server.menu.service.MenuService;
import com.fizz.fizz_server.review.dto.request.ReviewRequestDto;
import com.fizz.fizz_server.review.service.ReviewService;
import com.fizz.fizz_server.store.dto.request.StoreRequestDto;
import com.fizz.fizz_server.store.entity.Store;
import com.fizz.fizz_server.store.service.StoreService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CsvService {

    private final StoreService storeService;
    private final MenuService menuService;
    private final ReviewService reviewService;

    private static final String STORE_CSV = "./csv/store.csv";
    private static final String MENU_CSV = "./csv/menu.csv";
    private static final String REVIEW_CSV = "./csv/review.csv";


    /**
     * DB에 존재 + 현재도 노출	: UPDATE (리뷰 수, 주소, 메뉴 등 최신 정보 반영)
     * DB에 존재 + 현재 노출되지 않음 : IGNORE
     * DB에 존재하지 않음 + 현재 노출됨 : INSERT
     */
    public void updateDatabaseFromCsv(){
        try {
            List<StoreRequestDto> stores = parseStoreCsv(STORE_CSV);
            Map<String, List<MenuRequestDto>> menuMap = parseMenuCsv(MENU_CSV);
            Map<String, List<ReviewRequestDto>> reviewMap = parseReviewCsv(REVIEW_CSV);

            for (StoreRequestDto storeDto : stores) {
                Store store = storeService.upsertStore(storeDto);
                Long storeId = store.getId();
                Long externalStoreId = store.getExternalStoreId();


                // db 에는 storeid 를 fk 로 하고 있어서 첫  인자로는 storeid 를 사용하지만, csv 파일에는 externalstoreid 로 저장되어있기떄문에[ getOrDefulat 로 조회
                menuService.upsertMenus(storeId, menuMap.getOrDefault(externalStoreId, List.of()));
                reviewService.upsertReviews(storeId, reviewMap.getOrDefault(externalStoreId, List.of()));
            }

        } catch (Exception e) {
            log.error("CSV 동기화 중 오류 발생: {}", e.getMessage(), e);
        }
    }



    public List<StoreRequestDto> parseStoreCsv(String classpathLocation) throws IOException , CsvValidationException {
        List<StoreRequestDto> stores = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(classpathLocation);
        try (CSVReader reader = new CSVReader(new FileReader(resource.getFile()))) {
            String[] line;
            reader.readNext(); // skip header
            while ((line = reader.readNext()) != null) {
                StoreRequestDto dto = StoreRequestDto.builder()
                        .externalStoreId(Long.parseLong(line[0]))
                        .name(line[1])
                        .openHour(line[2])
                        .address(line[3])
                        .reviewCount(Long.parseLong(line[4]))
                        .build();
                stores.add(dto);
            }
        }
        return stores;
    }

    public Map<String, List<MenuRequestDto>> parseMenuCsv(String classpathLocation) throws IOException , CsvValidationException {
        Map<String, List<MenuRequestDto>> menuMap = new HashMap<>();
        ClassPathResource resource = new ClassPathResource(classpathLocation);
        try (CSVReader reader = new CSVReader(new FileReader(resource.getFile()))) {
            String[] line;
            reader.readNext(); // skip header
            while ((line = reader.readNext()) != null) {
                String externalId = line[0];
                MenuRequestDto dto = MenuRequestDto.builder()
                        .name(line[1])
                        .price(line[2])
                        .description(line[3])
                        .build();
                menuMap.computeIfAbsent(externalId, k -> new ArrayList<>()).add(dto);
            }
        }
        return menuMap;
    }

    public Map<String, List<ReviewRequestDto>> parseReviewCsv(String classpathLocation) throws IOException  , CsvValidationException  {
        Map<String, List<ReviewRequestDto>> reviewMap = new HashMap<>();
        ClassPathResource resource = new ClassPathResource(classpathLocation);
        try (CSVReader reader = new CSVReader(new FileReader(resource.getFile()))) {
            String[] line;
            reader.readNext(); // skip header
            while ((line = reader.readNext()) != null) {
                String externalId = line[0];
                ReviewRequestDto dto = ReviewRequestDto.builder()
                        .writer(line[1])
                        .rating(Integer.parseInt(line[2]))
                        .postedAt(line[3])
                        .comment(line[4])
                        .build();
                reviewMap.computeIfAbsent(externalId, k -> new ArrayList<>()).add(dto);
            }
        }
        return reviewMap;
    }


}
