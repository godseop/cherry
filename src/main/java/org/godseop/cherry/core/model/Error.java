package org.godseop.cherry.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum Error {

    /**
     * SUCCESS
     */
    OK("0000", "정상"),

    /**
     * PARAMETER ERROR
     */
    MANDATORY_VIOLATE("1000", "필수 파라미터 오류입니다."),
    WRONG_PAGE_PARAMETER("1100", "페이지 파라미터 오류입니다"),

    USER_NOT_FOUND("2000", "등록되지 않은 사용자입니다."),
    LOGIN_FAIL("2100", "사용자 인증에 실패했습니다."),
    DUPLICATED_USER_ID("2200", "사용자 ID 중복입니다."),


    /**
     * JWT TOKEN ERROR
     */
    TOKEN_INVALID("8000", "인증토큰 오류입니다."),
    TOKEN_EXPIRED("8100", "인증토큰이 만료되었습니다."),
    TOKEN_MALFORMED("8200", "인증토큰 오류입니다."),
    AUTH_NOT_GRANTED("8200", "해당 작업의 권한이 없습니다."),


    /**
     * SYSTEM ERROR
     */
    DATABASE_ERROR("9700", "데이터베이스 오류입니다."),
    SERVICE_UNAVAILABLE("9800", "일시적인 서비스장애입니다. 관리자에게 문의해주세요."),
    INTERNAL_SERVER_ERROR("9900", "치명적인 오류가 발생했습니다. 관리자에게 문의해주세요."),
    ;

    private final String code;
    private final String message;

}
