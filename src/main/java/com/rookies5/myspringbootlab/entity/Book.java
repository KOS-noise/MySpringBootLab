package com.rookies5.myspringbootlab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private Integer price;
    private LocalDate publishDate;

    // 2-4 실습문제에 추가된 내용
    // @OneToOne : 두 엔티티가 1:1 관계를 가진다. (예: 책 1권당 상세 정보 1개)
    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true) // BookDetail 쪽 book 필드가 관계를 관리한다는 의미
    // FK 컬럼은 Book 테이블에 없고, book_details.book_id에만 존재
    private BookDetail bookDetail;

    /*
     * 1. 관계의 주인 설정: 이 클래스(Book)는 관계의 주인이 아님을 표시함
     * 2. 외래 키의 위치: 실제 DB 테이블을 보면 Book 테이블에는 연결 고리(외래 키)가 아예 생성되지 않음
     * 3. 진짜 주인: 두 테이블을 묶어주는 진짜 열쇠(book_id)는 상대방인 BookDetail 테이블 쪽에 만들어짐
     * 4. 참조 전용 선언: "나는 외래 키가 없으니, 저쪽 BookDetail 클래스가 가진 'book' 변수를 거쳐서 연결된 데이터만 편하게 읽어오겠다(참조만 하겠다)"는 뜻
     */

}
