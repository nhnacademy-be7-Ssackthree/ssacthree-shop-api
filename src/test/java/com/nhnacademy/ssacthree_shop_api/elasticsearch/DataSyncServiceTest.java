package com.nhnacademy.ssacthree_shop_api.elasticsearch;


import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.impl.BookCustomRepositoryImpl;
import com.nhnacademy.ssacthree_shop_api.elastic.client.ElasticsearchFeignClient;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import com.nhnacademy.ssacthree_shop_api.elastic.service.DataSyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 - testSyncBooksFromMySQLToElasticsearch
      정상적으로 책 데이터를 MySQL에서 읽고
      Elasticsearch에 저장하는 과정을 검증합니다.

 - testSyncBooksFromMySQLToElasticsearch_EmptyPage
      MySQL에 책 데이터가 없을 때
      Elasticsearch에 저장이 이루어지지 않는지 검증합니다.

 */


class DataSyncServiceTest {

  @Mock
  private BookRepository bookRepository;

  @Mock
  private BookCustomRepositoryImpl bookCustomRepository;

  @Mock
  private ElasticsearchFeignClient elasticsearchFeignClient;

  @InjectMocks
  private DataSyncService dataSyncService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    ReflectionTestUtils.setField(dataSyncService, "readSize", 1);  // 최소 1 이상의 값을 설정
  }


  @Test
  void testSyncBooksFromMySQLToElasticsearch() {
    // given
    Book book = new Book();
    book.setBookId(1L);
    book.setBookName("Test Book");
    book.setBookIndex("001");
    book.setBookInfo("This is a test book.");
    book.setBookIsbn("978-3-16-148410-0");
    book.setPublicationDate(LocalDateTime.of(2023, 1, 1, 0, 0));
    book.setRegularPrice(10000);
    book.setSalePrice(8000);
    book.setIsPacked(true);
    book.setStock(50);
    book.setBookThumbnailImageUrl("http://example.com/thumbnail.jpg");
    book.setBookViewCount(100);
    book.setBookDiscount(20);

    List<Book> books = Collections.singletonList(book);
    Page<Book> bookPage = new PageImpl<>(books);
    Pageable pageable = PageRequest.of(0, 100);

    when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
    when(bookCustomRepository.findAuthorNamesByBookId(anyLong())).thenReturn(Collections.singletonList("Test Author"));
    when(bookCustomRepository.findPublisherNameByBookId(anyLong())).thenReturn("Test Publisher");
    when(bookCustomRepository.findTagNamesByBookId(anyLong())).thenReturn(Collections.singletonList("Test Tag"));
    when(bookCustomRepository.findCategoryNamesByBookId(anyLong())).thenReturn(Collections.singletonList("Test Category"));

    // when
    dataSyncService.syncBooksFromMySQLToElasticsearch();

    // then
    verify(elasticsearchFeignClient, times(1)).saveBook(any(BookDocument.class));
    verify(bookRepository, times(1)).findAll(any(Pageable.class));
  }

  @Test
  void testSyncBooksFromMySQLToElasticsearch_EmptyPage() {
    // given
    Page<Book> bookPage = new PageImpl<>(Collections.emptyList());
    when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

    // when
    dataSyncService.syncBooksFromMySQLToElasticsearch();

    // then
    verify(elasticsearchFeignClient, never()).saveBook(any(BookDocument.class));
    verify(bookRepository, times(1)).findAll(any(Pageable.class));
  }
}
