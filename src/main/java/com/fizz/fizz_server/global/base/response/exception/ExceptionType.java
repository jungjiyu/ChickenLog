package com.fizz.fizz_server.global.base.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ExceptionType {
    // common
    UNEXPECTED_SERVER_ERROR(INTERNAL_SERVER_ERROR, "C001", "예상치못한 서버에러 발생"),
    BINDING_ERROR(BAD_REQUEST, "C002", "바인딩시 에러 발생"),
    ESSENTIAL_FIELD_MISSING_ERROR(NO_CONTENT , "C003","필수적인 필드 부재"),
    INVALID_VALUE_ERROR(NOT_ACCEPTABLE , "C004","값이 유효하지 않음"),
    DUPLICATE_VALUE_ERROR(NOT_ACCEPTABLE , "C005","값이 중복됨"),


    // user
    USER_NOT_FOUND(NOT_FOUND, "U001", "존재하지 않는 사용자"),
    DUPLICATED_USER_ID(CONFLICT, "U002", "중복 아이디(PK)"),
    DUPLICATED_USERNAME(CONFLICT, "U003", "중복 아이디(username)"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "U004", "권한이 없음"),
    UN_AUTHENTICATION(UNAUTHORIZED, "U005", "인증이 필요함"),

    //store
    STORE_NOT_FOUND(NOT_FOUND, "S001", "존재하지 않는 가게"),
    DUPLICATED_STORE_ID(CONFLICT, "S002", "중복 가게 아이디(PK)"),
    DUPLICATED_EXTERNAL_STORE_ID(CONFLICT, "S003", "중복 가게 외부 아이디(external store id)"),


    //menu
    MENU_NOT_FOUND(NOT_FOUND, "M001", "존재하지 않는 메뉴"),
    DUPLICATED_MENU_ID(CONFLICT, "M002", "중복 메뉴 ID"),

    //review
    REVIEW_NOT_FOUND(NOT_FOUND, "R001", "존재하지 않는 리뷰"),
    DUPLICATED_REVIEW_ID(CONFLICT, "R002", "중복 리뷰 ID"),
    ;


    private final HttpStatus status;
    private final String code;
    private final String message;

}
