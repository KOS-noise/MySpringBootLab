package com.rookies5.myspringbootlab.repository;
import com.rookies5.myspringbootlab.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByAuthor(String author);
    // 저자 문자열 부분일치, 대소문자 무시 테스트
    List<Book> findByTitleContainingIgnoreCase(String title);
    // 새로운 도서를 등록/수정할 때, 중복된 ISBN 이 있는지 참/거짓 판별
    boolean existByIsbn(String isbn);

    // sql 쿼리문을 날림
    // JOIN FETCH : 연관된 엔티티(BookDetail)를 한 번의 SQL 쿼리로 함께 묶어와!
    // BookDetail 을 LAZY로 해서 필요할 때만 따로 가져오도록 했는데 특정 책을 조회할 때는 어차피 상세 정보도 필요함
    // 이때 쿼리를 두 번 날리지 말고 페치 조인으로 두 데이터를 동시에 가져와서 성능 최적화 (N+1) 문제 해결
    @Query("SELECT b FROM Book b JOIN FETCH b.bookDetail WHERE b.id = :id")
    Optional<Book> findByIdWithBookDetail(@Param("id") Long id);

    @Query("SELECT b FROM Book b JOIN FETCH b.bookDetail WHERE b.isbn = :isbn")
    Optional<Book> findByIsbnWithBookDetail(@Param("isbn") String isbn);
}
