package com.rookies5.myspringbootlab.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter // 필드 Getter 메서드 자동 생성
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L; // 객체 직렬화 ID

    private String message; // 클라이언트에게 전달할 에러 메시지
    private HttpStatus httpStatus; // 반환할 HTTP 상태 코드

    public BusinessException(String message) {
        // 상태 코드 생략 시 417(Expectation Failed) 기본 할당
        this(message, HttpStatus.EXPECTATION_FAILED);
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        // 입력받은 메시지와 상태 코드로 초기화
        this.message = message;
        this.httpStatus = httpStatus;
    }
}