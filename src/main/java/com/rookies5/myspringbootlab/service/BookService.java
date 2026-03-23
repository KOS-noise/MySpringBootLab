package com.rookies5.myspringbootlab.service;
import com.rookies5.myspringbootlab.controller.dto.BookDTO;
import com.rookies5.myspringbootlab.entity.Book;
import com.rookies5.myspringbootlab.entity.BookDetail;
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
    public BookDTO.Response createBook(BookDTO.Request request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();
        if (request.getDetailRequest() != null) {
            BookDetail detail = BookDetail.builder()
                    .description(request.getDetailRequest().getDescription())
                    .language(request.getDetailRequest().getLanguage())
                    .pageCount(request.getDetailRequest().getPageCount())
                    .publisher(request.getDetailRequest().getPublisher())
                    .coverImageUrl(request.getDetailRequest().getCoverImageUrl())
                    .edition(request.getDetailRequest().getEdition())
                    .book(book)
                    .build();
            book.setBookDetail(detail);
        }
        Book saved = bookRepository.save(book);
        return BookDTO.Response.fromEntity(saved);
    }
    @Transactional(readOnly = true)
    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }
    @Transactional(readOnly = true)
    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }
    @Transactional(readOnly = true)
    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException("해당 ISBN의 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }
    @Transactional(readOnly = true)
    public List<BookDTO.Response> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<BookDTO.Response> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }
    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        Book existingBook = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        existingBook.setTitle(request.getTitle());
        existingBook.setAuthor(request.getAuthor());
        existingBook.setIsbn(request.getIsbn());
        existingBook.setPrice(request.getPrice());
        existingBook.setPublishDate(request.getPublishDate());
        if (request.getDetailRequest() != null) {
            BookDetail detail = existingBook.getBookDetail();
            if (detail == null) {
                detail = BookDetail.builder().book(existingBook).build();
                existingBook.setBookDetail(detail);
            }
            detail.setDescription(request.getDetailRequest().getDescription());
            detail.setLanguage(request.getDetailRequest().getLanguage());
            detail.setPageCount(request.getDetailRequest().getPageCount());
            detail.setPublisher(request.getDetailRequest().getPublisher());
            detail.setCoverImageUrl(request.getDetailRequest().getCoverImageUrl());
            detail.setEdition(request.getDetailRequest().getEdition());
        } else {
            existingBook.setBookDetail(null);
        }
        Book saved = bookRepository.save(existingBook);
        return BookDTO.Response.fromEntity(saved);
    }
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        bookRepository.delete(book);
    }

    public BookDTO.Response patchBook(Long id, BookDTO.PatchRequest request) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            book.setAuthor(request.getAuthor());
        }
        if (request.getPrice() != null) {
            book.setPrice(request.getPrice());
        }
        if (request.getPublishDate() != null) {
            book.setPublishDate(request.getPublishDate());
        }
        // ISBN 변경 시에만 중복 검사
        if (request.getIsbn() != null) {
            if (!book.getIsbn().equals(request.getIsbn()) && bookRepository.existsByIsbn(request.getIsbn())) {
                throw new BusinessException("이미 사용 중인 ISBN입니다: " + request.getIsbn(), HttpStatus.CONFLICT);
            }
            book.setIsbn(request.getIsbn());
        }
        // PATCH /api/books/{id}에서 detailRequest도 같이 받는 경우 처리
        if (request.getDetailRequest() != null) {
            BookDetail detail = book.getBookDetail();
            if (detail == null) {
                detail = BookDetail.builder().book(book).build();
                book.setBookDetail(detail);
            }
            if (request.getDetailRequest().getDescription() != null) {
                detail.setDescription(request.getDetailRequest().getDescription());
            }
            if (request.getDetailRequest().getLanguage() != null) {
                detail.setLanguage(request.getDetailRequest().getLanguage());
            }
            if (request.getDetailRequest().getPageCount() != null) {
                detail.setPageCount(request.getDetailRequest().getPageCount());
            }
            if (request.getDetailRequest().getPublisher() != null) {
                detail.setPublisher(request.getDetailRequest().getPublisher());
            }
            if (request.getDetailRequest().getCoverImageUrl() != null) {
                detail.setCoverImageUrl(request.getDetailRequest().getCoverImageUrl());
            }
            if (request.getDetailRequest().getEdition() != null) {
                detail.setEdition(request.getDetailRequest().getEdition());
            }
        }
        return BookDTO.Response.fromEntity(bookRepository.save(book));
    }
    public BookDTO.Response patchBookDetail(Long id, BookDTO.BookDetailPatchRequest request) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        BookDetail detail = book.getBookDetail();
        if (detail == null) {
            detail = BookDetail.builder().book(book).build();
            book.setBookDetail(detail);
        }
        if (request.getDescription() != null) {
            detail.setDescription(request.getDescription());
        }
        if (request.getLanguage() != null) {
            detail.setLanguage(request.getLanguage());
        }
        if (request.getPageCount() != null) {
            detail.setPageCount(request.getPageCount());
        }
        if (request.getPublisher() != null) {
            detail.setPublisher(request.getPublisher());
        }
        if (request.getCoverImageUrl() != null) {
            detail.setCoverImageUrl(request.getCoverImageUrl());
        }
        if (request.getEdition() != null) {
            detail.setEdition(request.getEdition());
        }
        return BookDTO.Response.fromEntity(bookRepository.save(book));
    }
}