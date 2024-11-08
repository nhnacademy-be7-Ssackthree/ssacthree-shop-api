package com.nhnacademy.ssacthree_shop_api.elastic.client;

import com.nhnacademy.ssacthree_shop_api.config.FeignClientConfig;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

// 서버 엘라스틱서치 주소
@FeignClient(name = "elasticsearch", url = "${elasticsearch.url}", configuration = FeignClientConfig.class)
public interface ElasticsearchFeignClient {
  
  // 데이터 저장
  @PostMapping("/ssacthree_books/_doc")
  void saveBook(BookDocument bookDocument);
}