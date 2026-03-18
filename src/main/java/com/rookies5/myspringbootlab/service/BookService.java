package com.rookies5.myspringbootlab.service;


public class BookService {

    private final BookRepository bookRepository;

    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {

        // Book book = Book.builder() ... build();
        // - Book은 DB 테이블(books)과 매핑되는 엔티티
        // builder()는 롬북 어노테이션 덕분에 가능한 문법이고
        // request 에서 받은 값들을 Book 엔티티 필드에 복사해서 새 객체를 만든다...
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
        return bookRepository,findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public BookDTO.BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 ID의 도서를 찾을 수 없습니다.", HttpStatuas.NOT_FOUND));
        return toResponse(book);
    }

    @Transactional(readOnly = true)
    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
            .orElseThrow(() -> new NotFoundException("해당 ISBN의 도서를 찾을 수 없습니다.", HttpStatuas.NOT_FOUND));
        return toResponse(book);
    }

    
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        Book existBook = bookRepository.findById(id)
            .orElseThrow(() -> new BusinessException("해당 ID의 도서를 찾을 수 없습니다.", HttpStatuas.NOT_FOUND));

        // 변경이 필요한 필드만 업데이트
        if (request.getTitle() != null) {
            existBook.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            existBook.setAuthor(request.getAuthor());
        }
        if (request.getIsbn() != null) {
            existBook.setIsbn(request.getIsbn());
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
            .orElseThrow(() -> new NotFoundException("해당 ID의 도서를 찾을 수 없습니다.", HttpStatuas.NOT_FOUND));
        bookRepository.delete(book);
    }

    private BookDTO.BookRepository toResponse(Book book) {
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