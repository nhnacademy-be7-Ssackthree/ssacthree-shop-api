package com.nhnacademy.ssacthree_shop_api.bookset.book.domain;


import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.converter.BookStatusConverter;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import jakarta.persistence.*; // jakarta -> javax?
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.Setter;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId; // 도서 아이디

    private String bookName; // 도서 이름

    private String bookIndex; // 도서 목차

    private String bookInfo; // 도서 설명

    private String bookIsbn; // 도서 고유 번호

    private LocalDateTime publicationDate; //출판 일시

//    @Pattern(regexp = "\\d+", message = "가격은 숫자로 입력해주세요")
    private int regularPrice; // 정가

//    @Pattern(regexp = "\\d+", message = "가격은 숫자로 입력해주세요")
    private int salePrice; // 판매가

    private boolean isPacked; //포장 가능 여부

    private int stock; // 재고

    private String bookThumbnailImageUrl; // 도서 썸네일 이미지

    private int bookViewCount; // 도서 조회수

    private int bookDiscount; // 할인율

    @Convert(converter = BookStatusConverter.class)
    private BookStatus bookStatus; // 도서 상태

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private  Publisher publisher;

    public boolean getIsPacked() {
        return isPacked;
    }

    public void setIsPacked(boolean isPacked) {
        this.isPacked = isPacked;
    }

}
