package org.godseop.cherry.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum Error {

    OK("0000", "정상"),

    USER_NOT_FOUND("1000", "사용자를 찾을 수 없습니다."),

    SERVICE_UNAVAILABLE("9998", "일시적인 서비스장애입니다. 관리자에게 문의해주세요."),
    INTERNAL_SERVER_ERROR("9999", "서버 에러입니다. 관리자에게 문의해주세요."),

    ;

    private final String code;
    private final String message;

}
