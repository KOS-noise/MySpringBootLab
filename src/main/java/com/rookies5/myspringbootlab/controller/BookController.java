package com.rookies5.myspringbootlab.controller;

import com.rookies5.myspringbootlab.controller.dto.BookDTO;
import com.rookies5.myspringbootlab.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // Build Add Book REST API
    @PostMapping
    public ResponseEntity<BookDTO.BookResponse> createBook(
            @Valid @RequestBody BookDTO.BookCreateRequest request
    ){
        BookDTO.BookResponse savedBook = bookService.createBook(request);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    // Build Get Book REST API
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> getBookById(@PathVariable("id") Long bookId){
        BookDTO.BookResponse book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }

    // Build Get All Books REST API
    @GetMapping
    public ResponseEntity<List<BookDTO.BookResponse>> getAllBooks(){
        List<BookDTO.BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // Build Update Book REST API
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> updateBook(
            @PathVariable("id") Long bookId,
            @Valid @RequestBody BookDTO.BookUpdateRequest request
    ){
        BookDTO.BookResponse updatedBook = bookService.updateBook(bookId, request);
        return ResponseEntity.ok(updatedBook);
    }

    // Build Delete Book REST API
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") Long bookId){
        bookService.deleteBook(bookId);
        return ResponseEntity.ok("Book deleted successfully!.");
    }

    // Build Get Book By ISBN REST API
    @GetMapping(value = {"/isbn/{isbn}", "/isbn/{isbn}/"})
    public ResponseEntity<BookDTO.BookResponse> getBookByIsbn(@PathVariable String isbn){
        BookDTO.BookResponse book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }
}