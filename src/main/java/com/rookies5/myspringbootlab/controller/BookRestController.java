package com.rookies5.myspringbootlab.controller;

import com.rookies5.myspringbootlab.entity.Book;
import com.rookies5.myspringbootlab.exception.BusinessException;
import com.rookies5.myspringbootlab.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    @Autowired
    private BookRepository bookRepository;

    // 1. 새 도서 등록 (POST /api/books)
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(savedBook);
    }

    // 2. 모든 도서 조회 (GET /api/books)
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // 3. ID로 특정 도서 조회 (GET /api/books/{id}) - map() / orElse() 사용
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found 반환
    }

    // 4. ISBN으로 도서 조회 (GET /api/books/isbn/{isbn}/) - BusinessException 사용
    @GetMapping(value = { "/isbn/{isbn}", "/isbn/{isbn}/" })
    public Book getBookByIsbn(@PathVariable String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("해당 ISBN의 도서를 찾을 수 없습니다."));
    }

    // 5. 도서 정보 수정 (PUT /api/books/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setIsbn(bookDetails.getIsbn());
                    book.setPrice(bookDetails.getPrice());
                    book.setPublishDate(bookDetails.getPublishDate());
                    return ResponseEntity.ok(bookRepository.save(book));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 6. 도서 삭제 (DELETE /api/books/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}