package com.rookies5.myspringbootlab.service;

import com.rookies5.myspringbootlab.dto.BookDTO;
import com.rookies5.myspringbootlab.entity.Book;
import com.rookies5.myspringbootlab.exception.BusinessException;
import com.rookies5.myspringbootlab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BookDTO.BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookDTO.BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return toResponse(book);
    }

    @Transactional(readOnly = true)
    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("해당 ISBN의 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return toResponse(book);
    }

    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        Book existBook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (request.getTitle() != null) {
            existBook.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            existBook.setAuthor(request.getAuthor());
        }
        if (request.getPrice() != null) {
            existBook.setPrice(request.getPrice());
        }
        if (request.getPublishDate() != null) {
            existBook.setPublishDate(request.getPublishDate());
        }

        Book saved = bookRepository.save(existBook);
        return toResponse(saved);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        bookRepository.delete(book);
    }

    private BookDTO.BookResponse toResponse(Book book) {
        return BookDTO.BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .publishDate(book.getPublishDate())
                .build();
    }
}