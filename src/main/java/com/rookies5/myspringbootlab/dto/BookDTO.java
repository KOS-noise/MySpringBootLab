package com.rookies5.myspringbootlab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class BookDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookCreateRequest {
        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(message = "저자는 필수입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수입니다.")
        private String isbn;

        @NotNull(message = "가격은 필수입니다.")
        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;

        @NotNull(message = "출판일은 필수입니다.")
        private LocalDate publishDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookUpdateRequest {
        private String title;
        private String author;

        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;

        private LocalDate publishDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
    }
}
package com.rookies5.myspringbootlab.dto;


public class BookDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookCreateRequest {

        // 값이 NULL이 아니어야 하고, 공백(" ")이나 빈 문자열("")이 아니어야 함.
        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(messsage = "저자는 필수입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수입니다.")
        private String isbn;

        // 값이 무조건 NULL이 아니어야 함. (주로 숫자타입의 값을 받을 때 사용)
        @NotNull(message = "가격은 필수입니다.")
        // 숫자가 무조건 양수여야 함. (0이나 음수가 들어오면 에러 발생)
        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;

        @NotNull(message = "출판일은 필수입니다.")
        private LocalDate publishDate;


        // 이러한 어노테이션 덕분에, controller에 @Valid 만 붙이면 자동으로 유효성 검사가 실행됨.
        // 제목이 비었네? 가격이 마이너스네? : 유효성 검사
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookUpdateRequest {
        // 부분 업데이트: null이면 변경하지 않는다.
        private String title;
        private String author;

        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;

        private LocalDate publishDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
    }
}