package com.rookies5.myspringbootlab.repository;

import com.rookies5.myspringbootlab.entity.BookDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface BookDetailRepository extends JpaRepository <BookDetail, Long> {
    @Query("SELECT bd FROM BookDetail bd WHERE bd.book.id = :bookId")
    Optional<BookDetail> findByBookId(@Param("bookId") Long bookId);

    @Query("SELECT bd FROM BookDetail bd JOIN FETCH bd.book WHERE bd.id = :id")
    Optional<BookDetail> findByIdWithBook(@Param("id") Long id);

    List<BookDetail> findByPublisher(String publisher);
}