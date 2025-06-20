package com.fizz.fizz_server.crawler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlerService {

    private final RestTemplate restTemplate;
    private final String URL = "http://localhost:8000/crawl";


    @Scheduled(cron = "0 0 * * * *") // 매 정시마다 실행
    public void run() {
        try {
            // 서버의 크롤링 서버에 크롤링 요청 및 csv 파일 업데이트
            String response = restTemplate.getForObject(URL, String.class);
            log.info("크롤링 응답: {} " ,response);
            // 이후 CSV 파일 읽어서 DB 반영하는 로직 작성
        } catch (Exception e) {
            log.error("FastAPI 호출 실패: {}" ,e.getMessage());
        }
    }

}
