package com.rookies5.myspringbootlab.repository;

import com.rookies5.myspringbootlab.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// 스프링 부트의 모든 설정 대신 JPA(DB) 관련 설정만 빠르게 로드하는 어노테이션.
// 테스트가 끝나면 DB에 넣었던 데이터를 알아서 지워줘서(롤백) 다음 테스트에 영향을 안 줘!
@DataJpaTest
class BookRepositoryTest {
    // 스프링이 미리 만들어둔 BookRepository 객체를 찾아와서 여기에 쏙 넣어줘. (의존성 주입)
    @Autowired
    private BookRepository bookRepository;

//    // 모든 @Test 메서드가 실행되기 직전에 매번 실행됩니다.
//    @BeforeEach
//    void setUp() {
//        Book book1 = Book.builder()
//                .title("스프링 부트 입문")
//                .author("홍길동")
//                .isbn("9788956746425")
//                .price(30000)
//                .publishDate(LocalDate.of(2025, 5, 7))
//                .build();
//
//        Book book2 = Book.builder()
//                .title("JPA 프로그래밍")
//                .author("박둘리")
//                .isbn("9788956746432")
//                .price(35000)
//                .publishDate(LocalDate.of(2025, 4, 30))
//                .build();
//
//        savedBook1 = bookRepository.save(book1);
//        savedBook2 = bookRepository.save(book2);
//    }

    @Test
    @DisplayName("도서 등록 테스트")
    void testCreateBook() {
        //given
        Book book = Book.builder()
                .title("스프링 부트 입문")
                .author("홍길동")
                .isbn("9788956746425")
                .price(30000)
                .publishDate(LocalDate.of(2025, 5, 7))
                .build();

        //when
        Book savedBook = bookRepository.save(book);

        //then
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("스프링 부트 입문");
    }

    @Test
    @DisplayName("ISBN으로 도서 조회 테스트")
    void testFindByIsbn() {
        //given
        Book book = Book.builder()
                .title("JPA 프로그래밍")
                .author("박둘리")
                .isbn("9788956746432")
                .price(35000)
                .publishDate(LocalDate.of(2025, 4, 30))
                .build();
        bookRepository.save(book);

        //when
        Optional<Book> foundBook = bookRepository.findByIsbn("9788956746432");

        //then
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getAuthor()).isEqualTo("박둘리");
    }

    @Test
    @DisplayName("저자명으로 도서 목록 조회 테스트")
    void testFindByAuthor() {
        // given
        Book book1 = Book.builder().title("스프링 부트 입문").author("홍길동").isbn("9788956746425").price(30000).publishDate(LocalDate.of(2025, 5, 7)).build();
        Book book2 = Book.builder().title("JPA 프로그래밍").author("박둘리").isbn("9788956746432").price(35000).publishDate(LocalDate.of(2025, 4, 30)).build();
        bookRepository.save(book1);
        bookRepository.save(book2);

        // when
        List<Book> books = bookRepository.findByAuthor("홍길동");

        // then
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("스프링 부트 입문");
    }

    @Test
    @DisplayName("도서 정보 수정 테스트")
    void testUpdateBook() {
        // given
        Book book = Book.builder().title("스프링 부트 입문").author("홍길동").isbn("9788956746425").price(30000).publishDate(LocalDate.of(2025, 5, 7)).build();
        Book savedBook = bookRepository.save(book);

        // when
        savedBook.setPrice(32000); // 가격 수정
        Book updatedBook = bookRepository.save(savedBook);

        // then
        assertThat(updatedBook.getPrice()).isEqualTo(32000);
    }

    @Test
    @DisplayName("도서 삭제 테스트")
    void testDeleteBook() {
        // given
        Book book = Book.builder().title("JPA 프로그래밍").author("박둘리").isbn("9788956746432").price(35000).publishDate(LocalDate.of(2025, 4, 30)).build();
        Book savedBook = bookRepository.save(book);

        // when
        bookRepository.delete(savedBook);
        Optional<Book> deletedBook = bookRepository.findById(savedBook.getId());

        // then
        assertThat(deletedBook).isEmpty();
    }

    // 간결한 방법
//    @SpringBootTest // 1. 스프링 부트의 모든 빈(Bean)을 로드해서 실제 환경과 유사하게 테스트하겠다는 선언
//    @Transactional
//    class BookRepositoryTest {
//
//        @Autowired // 2. 스프링 컨테이너가 관리하는 BookRepository 객체를 자동으로 주입(연결)해줘!
//        BookRepository bookRepository;
//
//        @Test
//        // 3. 이 메서드는 '테스트 케이스'임을 알려줌 (JUnit이 이 메서드를 실행함)
//        @Rollback(value=false) // 롤백 처리를 하지 마세요!
//        @Disabled
//            // 현재는 테스트를 실행하지 않음 (실행하려면 주석 처리)
//        void testCreate() {
//            // [Given] 준비: 테스트를 위해 필요한 데이터를 만드는 단계
//            Book book = new Book(); // 4. 새로운 도서(Book) 객체를 생성
//            book.setTitle("스프링 부트 입문"); // 5. 도서 제목 설정
//            book.setAuthor("홍길동"); // 6. 저자 설정
//            book.setIsbn("9788956746425");
//            book.setPrice(30000);
//            book.setPublishDate(LocalDate.of(2025, 5, 7));
//
//            // [When] 실행: 실제로 검증하고 싶은 로직(DB 저장 등)을 수행하는 단계
//            Book savedBook = bookRepository.save(book); // 7. 레포지토리를 통해 DB에 저장하고, 저장된 결과를 반환받음
//
//            // [Then] 검증: 실행 결과가 내가 예상한 값과 일치하는지 확인하는 단계
//            assertThat(savedBook).isNotNull(); // 8. 저장되어 나온 객체(savedBook)가 null이 아니어야 함 (저장 성공 확인)
//            assertThat(savedBook.getTitle()).isEqualTo("스프링 부트 입문"); // 9. 저장된 도서의 이름이 정말 "스프링 부트 입문"인지 확인
//        }
//
//        @Test
//        void testFindBy() {
//            // (미리 DB에 데이터가 있다고 가정하거나, 위 testCreate를 먼저 실행해야 조회가 가능합니다.)
//
//            // DB에서 ISBN이 "9788956746425"인 도서를 조회하고, 결과를 Optional 객체로 감싸서 반환합니다. (NullPointerException 방지 목적)
//            Optional<Book> optionalBook = bookRepository.findByIsbn("9788956746425");
//
//            // 1. isPresent() 사용: 객체 안에 실제 데이터가 들어있는지(null이 아닌지) 확인합니다.
//            if (optionalBook.isPresent()) {
//                // 데이터가 존재하므로 get()을 사용해 안전하게 Book 객체를 꺼냅니다.
//                Book book = optionalBook.get();
//                // 꺼낸 객체의 저자가 "홍길동"과 같은지 검증합니다.
//                assertThat(book.getAuthor()).isEqualTo("홍길동");
//            } else {
//                // 데이터가 존재하지 않을 때(null일 때) 실행됩니다.
//                System.out.println("Book Not Found");
//            }
//
//            // 2. ifPresent()와 람다식 사용: 데이터가 존재할 때만 괄호 안의 동작을 실행합니다.
//            // 람다식 해석: 꺼낸 객체를 'book'이라고 부르고(->) 그 도서의 제목을 출력해라.
//            optionalBook.ifPresent(book -> System.out.println(book.getTitle()));
//
//            // orElseGet(Supplier)
//            // Supplier의 추상메서드는 T get() (입력값은 없고 반환값만 있다는 뜻)
//
//            // DB에서 ID가 999인 도서를 조회합니다. (없는 데이터라고 가정)
//            // 만약 도서가 존재하면 그 데이터를 그대로 'existBook'에 저장하고,
//            // 존재하지 않으면(null이면) 람다식 `() -> new Book()`을 실행하여 새로운 빈 도서 객체를 만들어 저장합니다.
//            Book existBook = bookRepository.findById(999L).orElseGet(() -> new Book());
//
//            // DB에서 도서를 찾지 못해 새로 만들어진 빈 객체라면, 아직 식별자(ID)가 부여되지 않아 null 상태일 것입니다.
//            // 따라서 ID가 null인지 검증합니다.
//            assertThat(existBook.getId()).isNull();
//        }
//
//        @Test
//        void testFindByNotFound() {
//            // 1. ID가 999인 도서 조회 (존재하지 않는 ID)
//            // 2. 결과가 없으면(null) orElseGet 작동 -> 빈 Book 객체를 새로 생성하여 반환
//            Book existBook = bookRepository.findById(999L).orElseGet(() -> new Book());
//            // orElseGet(): 데이터가 없을 때만 작동하여 대체값을 마련해 줍니다.
//
//            // 3. 새로 만들어진 빈 객체이므로 아직 ID가 없음을(null) 확인하여 테스트 통과
//            assertThat(existBook.getId()).isNull();
//
//            // 주의: 아래 코드는 실제로 예외(RuntimeException)를 발생시켜 테스트를 실패하게 만듭니다.
//            // 예외 발생 자체를 테스트하려면 assertThrows를 써야 하지만, 제시해주신 구조를 그대로 반영했습니다.
//        /*
//        Book notFoundBook = bookRepository.findById(999L)
//                .orElseThrow(() -> new RuntimeException("Book Not Found"));
//        */
//        }
//
//        @Test
//            //@Rollback(value=false)
//        void testUpdate() {
//            // 조회를 하고 setter를 호출하면 update 됨
//            Book book = bookRepository.findByIsbn("9788956746425")
//                    .orElseThrow(() -> new RuntimeException("Book Not Found"));
//
//            book.setPrice(32000); // 엔티티 값 변경 (테이블에 업데이트 됨)
//
//            bookRepository.save(book);
//            // save 를 안하려면 @Transactional 있어야함. (현재 클래스 상단에 @Transactional이 있으므로 더티 체킹이 발생합니다)
//
//            assertThat(book.getPrice()).isEqualTo(32000);
//        }
//    }
}
