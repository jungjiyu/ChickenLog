package com.fizz.fizz_server.crawler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlerService {

    private final RestTemplate restTemplate;
    @Value("${crawler.server.url}")
    private final String URL;
    private final CsvService csvService;


    /**
     *  크롤링 서버에 크롤링 요청 -> csv 파일 업데이트 -> csv 파일 기반 db 업데이트
     *  과정을 매 정시마다 동기적 수행
     */
    @Scheduled(cron = "0 0 * * * *")
    public void run() {
        try {

            String response = restTemplate.getForObject(URL, String.class);
            log.info("crawler server response: {} " ,response);
            csvService.updateDatabaseFromCsv();


        } catch (Exception e) {
            log.error("crawler server error : {}" ,e.getMessage());
        }
    }

}
