package com.fizz.fizz_server.csv.service;


import com.fizz.fizz_server.menu.repository.MenuRepository;
import com.fizz.fizz_server.review.repository.ReviewRepository;
import com.fizz.fizz_server.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CsvService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;

    private static final String STORE_CSV = "./csv/store.csv";
    private static final String MENU_CSV = "./csv/menu.csv";
    private static final String REVIEW_CSV = "./csv/review.csv";


    /**
     * DB에 존재 + 현재도 노출	: UPDATE (리뷰 수, 주소, 메뉴 등 최신 정보 반영)
     * DB에 존재 + 현재 노출되지 않음 : IGNORE (운영시간에 의한 노출제한으로 간주, 유지)
     * DB에 없음 + 현재 노출됨	: 아래 조건으로 분기 처리
     *  ① DB 내 가게 수 < 100 → INSERT
     *  ② DB 내 가게 수 = 100 → 신규 가게의 리뷰 수 > 현재 DB 내 최하위 리뷰수 가게 인지 확인
     *      : 참 ) 신규 가게 insert 및 기존 최하위 가게 delete
     *      : 거짓 ) 신규 가게 저장 skip
     */
    public void updateDatabaseFromCsv(){

        // 반복문으로 storecsv 파일의 각 행 단위 처리
        // 1. externalStore id 기반 조회 :
            // 존재하는 경우 -> update
                // 1. store 정보
                // 2. review 정보
                // 3. menu 정보
        
            // 존재하지 않는 경우 ( 신규 가게 )-> 
                // a. db 에 가게가 100 개 이하 저장되있는지 확인
                    // 100 개 미만이면 그냥 insert
                    // 100 개 이상이면 
                      // 1. db에서 리뷰 수 최하위 가게 조회
                      // 2. 리뷰수 최하위 가게 보다 리뷰 더 많은지 확인
                        // 리뷰수가 최하위 가게 보다 많으면 
                            // 0. 최하위 가게의 review 정보를 reviewrepository에서 삭제
                            // 1. 최하위 가게의 menu 정보를 menurepository에서 삭제
                            // 2. 최하위 가게의 정보를 storepository에서 삭제
                            // 3. 신규 가게의 정보를 추가 ( store, review , menu )


                        // 리뷰수가 최하위 가게보다 적으면 걍 신규가게 정보 저장 안하고 지나감

    }

    public void updateDatㄴabaseFromCsv() {

        try {
            List<StoreDto> stores = parseStoreCsv(STORE_CSV);
            Map<String, List<MenuDto>> menuMap = parseMenuCsv(MENU_CSV);
            Map<String, List<ReviewDto>> reviewMap = parseReviewCsv(REVIEW_CSV);

            long storeCount = storeRepository.count();
            List<Store> currentStores = storeRepository.findAll();

            // 최하위 리뷰 수 가게 찾기 (storeId 기준)
            Map<Long, Long> storeReviewCounts = reviewRepository.countByStoreIds(
                    currentStores.stream().map(Store::getId).collect(Collectors.toList())
            );
            long minReviewCount = storeReviewCounts.values().stream().min(Long::compare).orElse(0L);

            for (StoreDto dto : stores) {
                Optional<Store> existing = storeRepository.findByExternalId(dto.getExternalId());

                if (existing.isPresent()) {
                    Store store = existing.get();
                    store.updateFromDto(dto); // 영업시간, 주소 등 최신화
                    storeRepository.save(store);
                    log.info("Updated store: {}", store.getName());
                } else {
                    if (storeCount < 100) {
                        insertNewStore(dto, menuMap, reviewMap);
                        storeCount++;
                    } else {
                        long reviewCount = reviewMap.getOrDefault(dto.getExternalId(), List.of()).size();
                        if (reviewCount > minReviewCount) {
                            Store toDelete = findStoreWithMinReview(currentStores, storeReviewCounts);
                            storeRepository.delete(toDelete);
                            insertNewStore(dto, menuMap, reviewMap);
                            log.info("Replaced store: {} with {}", toDelete.getName(), dto.getName());
                        } else {
                            log.info("Skipped store: {}", dto.getName());
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("CSV 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    private void insertNewStore(StoreDto dto, Map<String, List<MenuDto>> menuMap, Map<String, List<ReviewDto>> reviewMap) {
        Store newStore = storeRepository.save(Store.fromDto(dto));

        menuMap.getOrDefault(dto.getExternalId(), List.of()).forEach(menuDto -> {
            menuRepository.save(Menu.fromDto(menuDto, newStore));
        });

        reviewMap.getOrDefault(dto.getExternalId(), List.of()).forEach(reviewDto -> {
            reviewRepository.save(Review.fromDto(reviewDto, newStore));
        });
    }

    // 이 외에 parseStoreCsv(), parseMenuCsv(), parseReviewCsv(), findStoreWithMinReview() 등 구현 필요
}
