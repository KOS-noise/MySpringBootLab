package com.rookies5.myspringbootlab.controller;

import com.rookies5.myspringbootlab.entity.Book;
import com.rookies5.myspringbootlab.exception.BusinessException;
import com.rookies5.myspringbootlab.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 이 클래스가 REST API 요청을 처리하는 컨트롤러임을 나타냅니다.
@RequestMapping("/api/books") // 이 컨트롤러 내 모든 API의 기본 URL 경로를 "/api/books"로 지정합니다.
public class BookRestController {

    @Autowired // 스프링 컨테이너가 BookRepository 객체를 찾아 자동으로 주입(연결)해 줍니다.
    private BookRepository bookRepository; // DB와 직접 통신하여 도서 데이터를 처리하는 리포지토리 객체입니다.

    // 1. 새 도서 등록 (POST /api/books)
    @PostMapping // HTTP POST 요청을 매핑합니다. (데이터 생성 시 사용)
    public ResponseEntity<Book> createBook(@RequestBody Book book) { // HTTP 요청 본문의 JSON 데이터를 Book 객체로 변환하여 받습니다.
        Book savedBook = bookRepository.save(book); // 리포지토리를 통해 전달받은 도서 정보를 DB에 저장합니다.
        return ResponseEntity.ok(savedBook); // 저장된 도서 정보와 함께 상태 코드 200(OK)을 응답합니다.
    }

    // 2. 모든 도서 조회 (GET /api/books)
    @GetMapping // HTTP GET 요청을 매핑합니다. (전체 데이터 조회 시 사용)
    public List<Book> getAllBooks() {
        return bookRepository.findAll(); // DB에 저장된 모든 도서 목록을 조회하여 반환합니다. (스프링이 자동으로 JSON 변환 및 200 상태 코드 응답)
    }

    // 3. ID로 특정 도서 조회 (GET /api/books/{id}) - map() / orElse() 사용
    @GetMapping("/{id}") // URL 경로의 {id}를 이용한 HTTP GET 요청을 매핑합니다.
    public ResponseEntity<Book> getBookById(@PathVariable Long id) { // URL의 {id} 값을 Long 타입 변수로 추출합니다.
        return bookRepository.findById(id) // 리포지토리에서 ID로 도서를 조회합니다. (데이터가 없을 수도 있으므로 Optional<Book> 반환)
                .map(ResponseEntity::ok) // 도서가 존재하면 상태 코드 200(OK)과 함께 해당 객체를 응답으로 포장합니다.
                .orElse(ResponseEntity.notFound().build()); // 도서가 없으면 상태 코드 404(Not Found)를 응답합니다.
    }

    // 4. ISBN으로 도서 조회 (GET /api/books/isbn/{isbn}/) - BusinessException 사용
    @GetMapping(value = { "/isbn/{isbn}", "/isbn/{isbn}/" }) // 끝에 슬래시(/)가 있거나 없는 두 가지 URL 패턴을 모두 매핑합니다.
    public Book getBookByIsbn(@PathVariable String isbn) { // URL의 {isbn} 값을 String 타입 변수로 추출합니다.
        return bookRepository.findByIsbn(isbn) // 리포지토리에서 ISBN으로 도서를 조회합니다. (Optional<Book> 반환)
                .orElseThrow(() -> new BusinessException("해당 ISBN의 도서를 찾을 수 없습니다.")); // 도서가 없으면 사용자 정의 예외(BusinessException)를 발생시킵니다.
    }

    // 5. 도서 정보 수정 (PUT /api/books/{id})
    @PutMapping("/{id}") // HTTP PUT 요청을 매핑합니다. (데이터 수정 시 사용)
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) { // URL에서 수정할 대상 ID를, 본문에서 새 데이터를 받습니다.
        return bookRepository.findById(id) // 수정할 대상을 DB에서 먼저 찾습니다.
                .map(book -> { // 대상 도서가 존재한다면 아래 로직을 통해 데이터를 덮어씁니다.
                    book.setTitle(bookDetails.getTitle()); // 기존 도서의 제목을 새 값으로 변경합니다.
                    book.setAuthor(bookDetails.getAuthor()); // 저자를 새 값으로 변경합니다.
                    book.setIsbn(bookDetails.getIsbn()); // ISBN을 새 값으로 변경합니다.
                    book.setPrice(bookDetails.getPrice()); // 가격을 새 값으로 변경합니다.
                    book.setPublishDate(bookDetails.getPublishDate()); // 출판일을 새 값으로 변경합니다.
                    return ResponseEntity.ok(bookRepository.save(book)); // 변경된 객체를 DB에 저장하고, 상태 코드 200(OK)과 함께 응답합니다.
                })
                .orElse(ResponseEntity.notFound().build()); // 수정할 도서가 존재하지 않으면 404(Not Found)를 응답합니다.
    }

    // 6. 도서 삭제 (DELETE /api/books/{id})
    @DeleteMapping("/{id}") // HTTP DELETE 요청을 매핑합니다. (데이터 삭제 시 사용)
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) { // URL에서 삭제할 대상 ID를 추출합니다.
        return bookRepository.findById(id) // 삭제할 대상을 DB에서 먼저 찾습니다.
                .map(book -> { // 대상 도서가 존재한다면 아래 로직을 통해 삭제합니다.
                    bookRepository.delete(book); // 리포지토리를 통해 해당 도서를 DB에서 삭제합니다.
                    return ResponseEntity.ok().<Void>build(); // 삭제가 완료되었음을 의미하는 상태 코드 200(OK)을 응답합니다. (응답 본문 없음)
                })
                .orElse(ResponseEntity.notFound().build()); // 삭제할 도서가 존재하지 않으면 404(Not Found)를 응답합니다.
    }
}