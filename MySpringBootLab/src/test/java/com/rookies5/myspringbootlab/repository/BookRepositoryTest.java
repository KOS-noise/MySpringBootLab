package com.rookies5.myspringbootlab.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.rookies5.myspringbootlab.entity.Book;
import com.rookies5.myspringbootlab.entity.BookDetail;
import com.rookies5.myspringbootlab.repository.BookRepository;
import com.rookies5.myspringbootlab.repository.BookDetailRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// JPA 레포지토리 테스트 전용 어노테이션.
// 내장 DB(H2)를 사용하고, 각 테스트가 끝나면 데이터를 자동으로 싹 지워줘서(Rollback) 안전해!
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookDetailRepository bookDetailRepository;

    // 1. 책과 상세 정보가 1:1 관계로 잘 저장되는지 확인하는 테스트
    @Test
    public void createBookWithBookDetail() {
        // [Given] 테스트할 데이터 준비
        Book book = Book.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .price(45)
                .publishDate(LocalDate.of(2008, 8, 1))
                .build();

        BookDetail bookDetail = BookDetail.builder()
                .description("A handbook of agile software craftsmanship")
                .language("English")
                .pageCount(464)
                .publisher("Prentice Hall")
                .coverImageUrl("https://example.com/cleancode.jpg")
                .edition("1st")
                .book(book) // 상세 정보에 어느 책인지 연결
                .build();

        // 부모(Book) 객체에도 자식(BookDetail)을 연결해 줌 (양방향 연결)
        book.setBookDetail(bookDetail);

        // [When] 실제 데이터베이스에 저장 실행
        Book savedBook = bookRepository.save(book);

        // [Then] 저장된 결과가 예상과 맞는지 검증
        assertThat(savedBook).isNotNull(); // 저장이 잘 되어서 객체가 비어있지 않은지
        assertThat(savedBook.getId()).isNotNull(); // DB에서 ID(Primary Key)를 잘 발급해 줬는지
        assertThat(savedBook.getTitle()).isEqualTo("Clean Code"); // 제목이 맞는지
        assertThat(savedBook.getIsbn()).isEqualTo("9780132350884");

        // 연관된 BookDetail 데이터도 자동으로 같이 잘 저장되었는지 확인 (Cascade 효과)
        assertThat(savedBook.getBookDetail()).isNotNull();
        assertThat(savedBook.getBookDetail().getPublisher()).isEqualTo("Prentice Hall");
        assertThat(savedBook.getBookDetail().getPageCount()).isEqualTo(464);
    }

    // 2. ISBN 번호로 책을 잘 찾아오는지 확인하는 테스트
    @Test
    public void findBookByIsbn() {
        // [Given] DB에 미리 책 데이터 하나를 저장해 둠
        Book book = Book.builder()
                // ... 데이터 셋팅 (위와 동일) ...
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .price(45)
                .publishDate(LocalDate.of(2008, 8, 1))
                .build();

        BookDetail bookDetail = BookDetail.builder()
                // ... 데이터 셋팅 (위와 동일) ...
                .description("A handbook of agile software craftsmanship")
                .language("English")
                .pageCount(464)
                .publisher("Prentice Hall")
                .coverImageUrl("https://example.com/cleancode.jpg")
                .edition("1st")
                .book(book)
                .build();

        book.setBookDetail(bookDetail);
        bookRepository.save(book);

        // [When] 방금 저장한 그 ISBN 번호로 DB에서 검색 실행
        Optional<Book> foundBook = bookRepository.findByIsbn("9780132350884");

        // [Then] 무사히 책을 찾았고, 그 책의 제목이 "Clean Code"가 맞는지 검증
        assertThat(foundBook).isPresent(); // 찾은 결과가 존재하는지 확인
        assertThat(foundBook.get().getTitle()).isEqualTo("Clean Code");
    }

    // 3. 책 ID로 검색할 때 상세정보(BookDetail)까지 한 번에 잘 조인해서 가져오는지 확인
    @Test
    public void findByIdWithBookDetail() {
        // [Given] 책과 상세 정보 저장
        Book book = Book.builder() /* ... */ .title("Clean Code").build();
        BookDetail bookDetail = BookDetail.builder() /* ... */ .publisher("Prentice Hall").book(book).build();
        book.setBookDetail(bookDetail);

        Book savedBook = bookRepository.save(book); // DB에 넣고 발급된 ID를 확보

        // [When] 발급된 ID를 이용해 Fetch Join이 적용된 커스텀 메서드로 검색
        Optional<Book> foundBook = bookRepository.findByIdWithBookDetail(savedBook.getId());

        // [Then] 책 정보뿐만 아니라 조인된 상세 정보(출판사 등)도 쿼리 한 번으로 잘 가져왔는지 검증
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getBookDetail()).isNotNull();
        assertThat(foundBook.get().getBookDetail().getPublisher()).isEqualTo("Prentice Hall");
    }

    // 4. 작가 이름의 "일부"만 입력해도 대소문자 상관없이 잘 검색되는지 확인
    @Test
    public void findBooksByAuthor() {
        // [Given] 3권의 책 저장 (Martin 작가 2권, Joshua 작가 1권)
        Book book1 = Book.builder().title("Clean Code").author("Robert C. Martin").build();
        Book book2 = Book.builder().title("Clean Architecture").author("Robert C. Martin").build();
        Book book3 = Book.builder().title("Effective Java").author("Joshua Bloch").build();

        bookRepository.saveAll(List.of(book1, book2, book3));

        // [When] 대소문자가 섞인 전체 이름 대신 "martin"이라는 소문자 일부만으로 검색
        List<Book> martinBooks = bookRepository.findByAuthorContainingIgnoreCase("martin");

        // [Then] 조건에 맞는 책이 딱 2권 나왔는지, 그리고 그 두 권의 제목이 맞는지 검증
        assertThat(martinBooks).hasSize(2); // 결과가 2개인지
        assertThat(martinBooks).extracting(Book::getTitle) // 결과 리스트에서 제목만 쏙쏙 뽑아서
                .containsExactlyInAnyOrder("Clean Code", "Clean Architecture"); // 순서 상관없이 이 두 제목이 맞는지
    }

    // 5. 책(Book)의 ID를 가지고 자식 테이블인 상세정보(BookDetail)를 바로 찾아올 수 있는지 확인
    @Test
    public void findBookDetailByBookId() {
        // [Given] 책과 상세 정보 저장
        Book book = Book.builder().title("Clean Code").build();
        BookDetail bookDetail = BookDetail.builder().description("A handbook of agile software craftsmanship").book(book).build();
        book.setBookDetail(bookDetail);

        Book savedBook = bookRepository.save(book);

        // [When] BookRepository가 아니라 BookDetailRepository를 사용해서, 책 ID를 조건으로 상세 정보 검색
        Optional<BookDetail> foundBookDetail = bookDetailRepository.findByBookId(savedBook.getId());

        // [Then] 검색된 상세 정보의 내용이 저장했던 것과 일치하는지 검증
        assertThat(foundBookDetail).isPresent();
        assertThat(foundBookDetail.get().getDescription()).contains("agile software craftsmanship");
    }
}