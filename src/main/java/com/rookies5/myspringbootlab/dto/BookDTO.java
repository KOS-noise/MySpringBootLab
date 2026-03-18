package com.rookies5.myspringbootlab.dto;

public class BookDto {


    public static class BookCreateRequest {

        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(messsage = "저자는 필수입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수입니다.")
        private String isbn;

        @NotNull(message = "가격은 필수입니다.")
        @Positive(message = "가격은 양수여야 합니다.")
        private Integer price;

        @NotNull(message = "출판일은 필수입니다.")
        private LocalDate publishDate;

    }
}