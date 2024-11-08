package com.nhnacademy.ssacthree_shop_api.bookset.book.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.QAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.QBook;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.converter.BookStatusConverter;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookCustomRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.QBookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.QBookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.QBookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.QCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.QPublisher;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.QTag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class BookCustomRepositoryImpl implements BookCustomRepository {
    private final JPAQueryFactory queryFactory;

    private static final QBook book = QBook.book;
    private static final QPublisher publisher = QPublisher.publisher;
    private static final QCategory category = QCategory.category;
    private static final QBookCategory bookCategory = QBookCategory.bookCategory;
    private static final QTag tag = QTag.tag;
    private static final QBookTag bookTag = QBookTag.bookTag;
    private static final QAuthor author = QAuthor.author;
    private static final QBookAuthor bookAuthor = QBookAuthor.bookAuthor;

    BookStatusConverter converter = new BookStatusConverter();

    /**
     * 판매 중, 재고 없음 상태의 책을 최신 출판 순으로 불러옴.
     * @param pageable 페이징 설정
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookInfoResponse> findRecentBooks(Pageable pageable) {
        Map<Long, BookInfoResponse> bookMap = queryFactory
                .from(book)
                .join(book.publisher, publisher)
                .where(book.bookStatus.eq(BookStatus.ON_SALE).or(book.bookStatus.eq(BookStatus.NO_STOCK)))
                .transform(groupBy(book.bookId).as(Projections.constructor(BookInfoResponse.class,
                        book.bookId,
                        book.bookName,
                        book.bookIndex,
                        book.bookInfo,
                        book.bookIsbn,
                        book.publicationDate,
                        book.regularPrice,
                        book.salePrice,
                        book.isPacked,
                        book.stock,
                        book.bookThumbnailImageUrl,
                        book.bookViewCount,
                        book.bookDiscount,
                        book.bookStatus.stringValue().as("bookStatus"),
                        book.publisher.publisherName
                )));

        List<BookInfoResponse> books = new ArrayList<>(bookMap.values());

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.ON_SALE).or(book.bookStatus.eq(BookStatus.NO_STOCK)))
                .fetchOne();

        count = (count == null ? 0 : count);

        return new PageImpl<>(books, pageable, count);
    }

    /**
     * 책 이름을 포함하고 있는 책을 검색합니다.
     * @param pageable 페이징 처리
     * @param bookName 찾고자 하는 책 이름
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookInfoResponse> findBooksByBookName(Pageable pageable, String bookName) {
        Map<Long, BookInfoResponse> bookMap = queryFactory
                .from(book)
                .join(book.publisher, publisher)
                .where(book.bookStatus.eq(BookStatus.ON_SALE)
                        .or(book.bookStatus.eq(BookStatus.NO_STOCK))
                        .and(book.bookName.containsIgnoreCase(bookName)))
                .transform(groupBy(book.bookId).as(Projections.constructor(BookInfoResponse.class,
                        book.bookId,
                        book.bookName,
                        book.bookIndex,
                        book.bookInfo,
                        book.bookIsbn,
                        book.publicationDate,
                        book.regularPrice,
                        book.salePrice,
                        book.isPacked,
                        book.stock,
                        book.bookThumbnailImageUrl,
                        book.bookViewCount,
                        book.bookDiscount,
                        book.bookStatus.stringValue().as("bookStatus"),
                        book.publisher.publisherName
                )));

        List<BookInfoResponse> books = new ArrayList<>(bookMap.values());

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.ON_SALE).or(book.bookStatus.eq(BookStatus.NO_STOCK)),
                        book.bookName.containsIgnoreCase(bookName))
                .fetchOne();

        count = (count == null ? 0 : count);

        return new PageImpl<>(books, pageable, count);
    }

    /**
     * 판매 중, 재고 없는 상태의 모든 책을 조회합니다.
     * @param pageable 페이징 처리
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookInfoResponse> findAllAvailableBooks(Pageable pageable) {
        Map<Long, BookInfoResponse> bookMap = queryFactory
                .from(book)
                .join(book.publisher, publisher)
                .where(book.bookStatus.eq(BookStatus.ON_SALE).or(book.bookStatus.eq(BookStatus.NO_STOCK)))
                .transform(groupBy(book.bookId).as(Projections.constructor(BookInfoResponse.class,
                        book.bookId,
                        book.bookName,
                        book.bookIndex,
                        book.bookInfo,
                        book.bookIsbn,
                        book.publicationDate,
                        book.regularPrice,
                        book.salePrice,
                        book.isPacked,
                        book.stock,
                        book.bookThumbnailImageUrl,
                        book.bookViewCount,
                        book.bookDiscount,
                        book.bookStatus.stringValue().as("bookStatus"),
                        book.publisher.publisherName
                )));

        List<BookInfoResponse> books = new ArrayList<>(bookMap.values());

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.ON_SALE).or(book.bookStatus.eq(BookStatus.NO_STOCK)))  // where 절 추가
                .fetchOne();

        count = (count == null ? 0 : count);

        return new PageImpl<>(books, pageable, count);
    }

    /**
     * 재고가 없는 모든 책을 조회합니다.
     * @param pageable 페이징 처리
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookInfoResponse> findAllBooksByStatusNoStock(Pageable pageable) {
        Map<Long, BookInfoResponse> bookMap = queryFactory
                .from(book)
                .join(book.publisher, publisher)
                .where(book.bookStatus.eq(BookStatus.NO_STOCK))
                .transform(groupBy(book.bookId).as(Projections.constructor(BookInfoResponse.class,
                        book.bookId,
                        book.bookName,
                        book.bookIndex,
                        book.bookInfo,
                        book.bookIsbn,
                        book.publicationDate,
                        book.regularPrice,
                        book.salePrice,
                        book.isPacked,
                        book.stock,
                        book.bookThumbnailImageUrl,
                        book.bookViewCount,
                        book.bookDiscount,
                        book.bookStatus.stringValue().as("bookStatus"),
                        book.publisher.publisherName
                )));

        List<BookInfoResponse> books = new ArrayList<>(bookMap.values());

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.NO_STOCK))
                .fetchOne();

        count = (count == null ? 0 : count);

        return new PageImpl<>(books, pageable, count);
    }

    /**
     * 판매 중단된 책을 모두 검색합니다.
     * @param pageable 페이징 처리
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookInfoResponse> findStatusDiscontinued(Pageable pageable) {
        Map<Long, BookInfoResponse> bookMap = queryFactory
                .from(book)
                .join(book.publisher, publisher)
                .where(book.bookStatus.eq(BookStatus.DISCONTINUED))
                .transform(groupBy(book.bookId).as(Projections.constructor(BookInfoResponse.class,
                        book.bookId,
                        book.bookName,
                        book.bookIndex,
                        book.bookInfo,
                        book.bookIsbn,
                        book.publicationDate,
                        book.regularPrice,
                        book.salePrice,
                        book.isPacked,
                        book.stock,
                        book.bookThumbnailImageUrl,
                        book.bookViewCount,
                        book.bookDiscount,
                        book.bookStatus.stringValue().as("bookStatus"),
                        book.publisher.publisherName
                )));

        List<BookInfoResponse> books = new ArrayList<>(bookMap.values());

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.DISCONTINUED))
                .fetchOne();

        count = (count == null ? 0 : count);

        return new PageImpl<>(books, pageable, count);
    }

    /**
     * 특정 책 정보를 가져옵니다.
     * @param bookId 책 아이디
     * @return 책 정보
     */
    @Override
    public String findBookInfoByBookId(Long bookId) {
        return queryFactory.select(book.bookInfo)
                .from(book)
                .where(book.bookId.eq(bookId))
                .fetchOne();
    }

    /**
     * 책 ID로 책의 작가(이름)를 가져옵니다.
     * @param bookId 책 아이디
     * @return 작가 이름
     */
    @Override
    public List<String> findAuthorNamesByBookId(Long bookId) {
        QBook book = QBook.book;
        QAuthor author = QAuthor.author;
        QBookAuthor bookAuthor = QBookAuthor.bookAuthor;

        return queryFactory
            .select(author.authorName)
            .from(book)
            .join(bookAuthor).on(book.bookId.eq(bookAuthor.book.bookId))
            .join(author).on(author.authorId.eq(bookAuthor.author.authorId))
            .where(book.bookId.eq(bookId))
            .fetch();
    }


    /**
     * 책으로 출판사명을 가져옴.
     * @param bookId
     * @return 출판사명
     */
    @Override
    public String findPublisherNameByBookId(Long bookId) {
        QBook book = QBook.book;
        return queryFactory
            .select(book.publisher.publisherName)
            .from(book)
            .where(book.bookId.eq(bookId))
            .fetchOne();
    }


    /**
     * 책으로 태그들의 name을 조회
     * @param bookId
     * @return 태그명 반환 (없다면 null을 그대로 저장)
     */
    @Override
    public List<String> findTagNamesByBookId(Long bookId){
        QBookTag qBookTag = QBookTag.bookTag;
        QTag tag = QTag.tag;

        return queryFactory
            .select(tag.tagName)
            .from(bookTag)
            .join(bookTag.tag, tag)
            .where(bookTag.book.bookId.eq(bookId))
            .fetch();
    }


    /**
     * 책으로 카테고리들의 name을 조회
     * @param bookId
     * @return
     */
    @Override
    public List<String> findCategoryNamesByBookId(Long bookId){
        QBookCategory bookCategory = QBookCategory.bookCategory;
        QCategory category = QCategory.category;

        return queryFactory
            .select(category.categoryName)
            .from(bookCategory)
            .join(bookCategory.category, category)
            .where(bookCategory.book.bookId.eq(bookId))
            .fetch();
    }

}
