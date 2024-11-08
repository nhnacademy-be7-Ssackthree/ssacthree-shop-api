package com.nhnacademy.ssacthree_shop_api.elastic.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "books")  // Elasticsearch 인덱스 설정
public class BookDocument {
  @Id
  private long bookId;
  private String bookName;
  private String bookIndex;
  private String bookInfo;
  private String bookIsbn;
  private String publicationDate;
  private int regularPrice;
  private int salePrice;
  private boolean isPacked;
  private int stock;
  private String bookThumbnailImageUrl;
  private int bookViewCount;
  private int bookDiscount;
  private String publisherNames;
  private String authorNames;
  private String tagNames;
}
