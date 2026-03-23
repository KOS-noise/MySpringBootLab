package com.rookies5.myspringbootlab.controller.dto;

import com.rookies5.myspringbootlab.entity.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * API 요청/응답을 엔티티와 분리해서 쓰기 위한 DTO 모음.
 * 바깥 클래스는 네임스페이스 역할만 하고, 실제로 쓰는 건 안쪽 static 클래스들.
 */
public class BookDTO {

    /** 책 생성·수정 시 클라이언트가 보내는 본문 형태. */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Book title is required")
        private String title;

        @NotBlank(message = "Author name is required")
        private String author;

        // ISBN: 숫자 10자리 또는 13자리(하이픈 있어도 됨). 정규식으로 형식만 맞는지 검사.
        @NotBlank(message = "ISBN is required")
        @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$",
                message = "ISBN must be valid (10 or 13 digits, with or without hyphens)")
        private String isbn;

        // 0 이상만 허용 (음수는 막음)
        @PositiveOrZero(message = "Price must be positive or zero")
        private Integer price;

        // 오늘보다 미래 날짜면 실패 (@Past = 과거만 OK)
        @Past(message = "Publish date must be in the past")
        private LocalDate publishDate;

        // 중첩 DTO도 검증하려면 @Valid 필수 (없으면 BookDetailDTO 안의 제약은 안 탐)
        @Valid
        private BookDetailDTO detailRequest;
    }

    /** 책 상세 정보를 Request 안에 같이 실을 때 쓰는 중첩 DTO. */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailDTO {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    /** 책 한 건을 API로 내려줄 때 쓰는 응답 형태. */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailResponse detail;

        /**
         * 엔티티 → Response DTO 변환.
         * bookDetail이 없으면 detail은 null로 두고, 있으면 BookDetailResponse로 매핑.
         */
        public static Response fromEntity(Book book) {
            BookDetailResponse detailResponse = book.getBookDetail() != null
                    ? BookDetailResponse.builder()
                    .id(book.getBookDetail().getId())
                    .description(book.getBookDetail().getDescription())
                    .language(book.getBookDetail().getLanguage())
                    .pageCount(book.getBookDetail().getPageCount())
                    .publisher(book.getBookDetail().getPublisher())
                    .coverImageUrl(book.getBookDetail().getCoverImageUrl())
                    .edition(book.getBookDetail().getEdition())
                    .build()
                    : null;

            return Response.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .detail(detailResponse)
                    .build();
        }
    }

    /** 응답에서 책 상세만 따로 묶어서 보여줄 때 쓰는 형태. */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailResponse {
        private Long id;
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PatchRequest {
        private String title;
        private String author;
        @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$",
                message = "ISBN must be valid (10 or 13 digits, with or without hyphens)")
        private String isbn;
        @PositiveOrZero(message = "Price must be positive or zero")
        private Integer price;
        @Past(message = "Publish date must be in the past")
        private LocalDate publishDate;
        @Valid
        private BookDetailPatchRequest detailRequest;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailPatchRequest {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }
}
