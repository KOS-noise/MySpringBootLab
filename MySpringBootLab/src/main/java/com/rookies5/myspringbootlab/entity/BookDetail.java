
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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", unique = true)
    private Book book;



    //외래 키(Foreign Key, FK)는 관계형 데이터베이스에서 한 테이블의 필드(열)가 다른 테이블의 기본 키(Primary Key)를 참조하는 키
    // 데베 책 참고 ~
}