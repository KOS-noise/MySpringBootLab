
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "book_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String language;
    private Integer pageCount;
    private String publisher;
    private String coverImgUrl;
    private String edition;

    /*
     * Book 엔티티와 1:1 관계를 맺고, 이 클래스(BookDetail)가 관계의 주인이 됩니다.
     * * * [ 어노테이션 핵심 의미 ]
     * 1. @OneToOne: Book과 일대일 관계를 가짐. 각 책은 하나의 상세 정보만 가질 수 있음.
     * 2. fetch = FetchType.LAZY (지연 로딩): BookDetail을 조회할 때 Book 정보까지 한꺼번에 DB에서 다 가져오지 않고,
     * 나중에 진짜로 Book 데이터가 필요할 때만 쿼리를 날려서 가져옴.
     * 3. @JoinColumn(name = "book_id"): 실제 DB의 `book_detail` 테이블 안에 `book_id`라는 이름으로
     *       외래 키(Foreign Key) 컬럼을 생성함. 즉, 외래 키를 직접 소유하고 관리함
     * 4. unique = true: 하나의 책에 여러 개의 상세 정보가 연결되는 것을 막기 위해 유니크 제약조건
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "book_id", unique = true)
    private Book book;

    // 영속성 컨텍스트 : JPA가 데이터를 DB에 반영하기 전에 엔티티를 임시로 저장하고 관리하는 '가상의 메모리 공간'
    // 부모(Book)에게 하는 모든 행동(저장, 삭제, 수정 등)을 자식(BookDetail)에게도 똑같이 종속(Cascade)

    // orphanRemoval = true
    // 부모와의 연결이 끊어져 혼자가 된 자식 객체(고아 객체)를 쓰레기로 간주하고 알아서 DB에서 지워준다
    //만약 book.setBookDetail(null);처럼 자식과의 연결을 끊어버리면, 부모를 잃은 기존 BookDetail 데이터를 DB에서 자동으로 DELETE

    //외래 키(Foreign Key, FK)는 관계형 데이터베이스에서 한 테이블의 필드(열)가 다른 테이블의 기본 키(Primary Key)를 참조하는 키
    // 데베 책 참고 ~
}