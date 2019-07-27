package org.godseop.cherry.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum Error {

    OK("0000", "정상"),

    USER_NOT_FOUND("1000", "등록되지 않은 사용자입니다."),
    LOGIN_FAIL("1001", "사용자 인증에 실패했습니다."),
    DUPLICATED_USER_ID("1002", "사용자 ID 중복입니다."),

    JWT_INVALID_OR_EXPIRED_TOKEN("8000", "JWT 토큰이 만료되었거나 잘못되었습니다."),

    SERVICE_UNAVAILABLE("9998", "일시적인 서비스장애입니다. 관리자에게 문의해주세요."),
    INTERNAL_SERVER_ERROR("9999", "서버 에러입니다. 관리자에게 문의해주세요."),


    ;

    private final String code;
    private final String message;

}
