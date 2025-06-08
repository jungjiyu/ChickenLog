from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
import csv

options = Options()
options.add_argument('--headless')
options.add_argument('--no-sandbox')
options.add_argument('--disable-dev-shm-usage')
options.add_argument('--window-size=1280,1024')
service = Service("./chromedriver")
driver = webdriver.Chrome(service=service, options=options)
wait = WebDriverWait(driver, 10)

# CSV 초기화
with open("store_info.csv", mode="w", newline="", encoding="utf-8-sig") as f:
    writer = csv.writer(f)
    writer.writerow(["store_id", "메뉴이름", "가격", "설명", "운영시간", "주소", "상호명", "리뷰작성자", "평점", "작성일자", "리뷰내용"])

try:
    print("스크립트 시작됨")
    driver.get("https://www.yogiyo.co.kr/mobile/#/")
    time.sleep(2)

    address_input = wait.until(EC.presence_of_element_located((By.NAME, "address_input")))
    address_input.click()
    time.sleep(1)

    suggestions = driver.find_elements(By.CSS_SELECTOR, "ul.dropdown-menu li a")
    for s in suggestions:
        if "현재 위치로 설정" in s.text:
            s.click()
            break
    time.sleep(2)

    chicken_category = wait.until(EC.element_to_be_clickable((By.XPATH, "//span[text()='치킨']")))
    chicken_category.click()
    time.sleep(2)

    sort_dropdown = Select(wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "select[ng-model='session_storage.restaurant_list_sort_order']"))))
    sort_dropdown.select_by_value("review_count")
    time.sleep(2)

    # 스크롤을 통한 음식점 리스트 수집
    SCROLL_PAUSE = 1.5
    max_restaurants = 100
    prev_count = -1

    while True:
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time.sleep(SCROLL_PAUSE)
        restaurants = driver.find_elements(By.CSS_SELECTOR, "div.restaurant-list > div > div.item.clearfix")
        curr_count = len(restaurants)
        if curr_count == prev_count or curr_count >= max_restaurants:
            print(f"더 이상 음식점이 로딩되지 않음. 현재 {curr_count}개 수집됨.")
            break
        prev_count = curr_count

    total_count = min(max_restaurants, len(restaurants))
    print(f"최종 수집 음식점 수: {total_count}")

    # 음식점 상세 정보 수집
    for idx in range(total_count):
        try:
            restaurants = driver.find_elements(By.CSS_SELECTOR, "div.restaurant-list > div > div.item.clearfix")
            if idx >= len(restaurants):
                print(f"[WARNING] {idx + 1}번째 음식점 접근 실패 (리스트 길이: {len(restaurants)})")
                continue

            item = restaurants[idx]
            driver.execute_script("arguments[0].scrollIntoView(true);", item)
            time.sleep(1)
            driver.execute_script("arguments[0].click();", item)

            wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "div.restaurant-detail")))
            print(f"상세페이지 진입 성공 ({idx + 1}번째 가게)")

            # store_id
            try:
                store_url = driver.execute_script("return window.location.href;")
                store_id = store_url.split("#/")[-1].split("/")[0]
                if not store_id.isdigit():
                    raise ValueError("store_id가 숫자 아님")
            except Exception as e:
                print(f"[ERROR] store_id 수집 실패: {e}")
                store_id = "(확인 실패)"

            # 메뉴
            menu_data = []
            menu_items = driver.find_elements(By.CSS_SELECTOR, "td.menu-text")
            for item in menu_items:
                try:
                    name = item.find_element(By.CSS_SELECTOR, "div.menu-name").text.strip()
                except:
                    continue
                if not name:
                    continue
                try:
                    desc = item.find_element(By.CSS_SELECTOR, "div.menu-desc").text.strip()
                except:
                    desc = "(설명 없음)"
                try:
                    price = item.find_element(By.CSS_SELECTOR, "div.menu-price > span").text.strip()
                except:
                    price = "(가격 없음)"
                menu_data.append((name, price, desc))

            # 가게 정보
            try:
                info_tab = driver.find_element(By.XPATH, "//a[@data-toggle='tab' and text()='정보']")
                info_tab.click()
                time.sleep(1)
            except:
                pass

            open_hour = "(확인 실패)"
            address = "(확인 실패)"
            info_paragraphs = driver.find_elements(By.CSS_SELECTOR, "div.info-item p")
            for p in info_paragraphs:
                try:
                    label = p.find_element(By.TAG_NAME, "i").text.strip()
                    value = p.find_element(By.CSS_SELECTOR, "span.tc").text.strip()
                    if "영업시간" in label:
                        open_hour = value
                    elif "주소" in label:
                        address = value
                except:
                    continue

            company_name = "(확인 실패)"
            try:
                business_section = driver.find_elements(By.CSS_SELECTOR, "div.info-item")
                for section in business_section:
                    title = section.find_element(By.CLASS_NAME, "info-item-title").text.strip()
                    if "사업자정보" in title:
                        company_name = section.find_element(By.XPATH, ".//p[1]/span").text.strip()
                        break
            except:
                pass

            # 리뷰
            try:
                review_tab = driver.find_element(By.XPATH, "//a[@data-toggle='tab' and contains(text(), '클린리뷰')]")
                review_tab.click()
                time.sleep(1)
            except:
                reviews_data = []
            else:
                reviews_data = []
                reviews = driver.find_elements(By.CSS_SELECTOR, "ul.review-list > li.list-group-item")
                for review in reviews[:10]:
                    try:
                        writer = review.find_element(By.CSS_SELECTOR, "span.review-id").text.strip()
                        time_posted = review.find_element(By.CSS_SELECTOR, "span.review-time").text.strip()
                        stars = len(review.find_elements(By.CSS_SELECTOR, "div.star-point > span.total > span.full"))
                        comment = review.find_element(By.CSS_SELECTOR, "p.ng-binding").text.strip()
                        reviews_data.append((writer, stars, time_posted, comment))
                    except:
                        continue

            # 저장
            with open("store_info.csv", mode="a", newline="", encoding="utf-8-sig") as f:
                writer = csv.writer(f)
                max_len = max(len(menu_data), len(reviews_data))
                for i in range(max_len):
                    menu = menu_data[i] if i < len(menu_data) else ("", "", "")
                    review = reviews_data[i] if i < len(reviews_data) else ("", "", "", "")
                    writer.writerow([
                        store_id,
                        menu[0], menu[1], menu[2],
                        open_hour, address, company_name,
                        review[0], review[1], review[2], review[3]
                    ])

            print(f"저장 완료 ({idx + 1}/{total_count})")
            driver.back()
            time.sleep(1)

        except Exception as e:
            print(f"[ERROR] {idx + 1}번째 가게 처리 중 예외 발생: {e}")
            driver.get("https://www.yogiyo.co.kr/mobile/#/")  # 복구를 위한 초기 진입
            time.sleep(3)
            continue

finally:
    driver.quit()
    print("드라이버 종료됨")
