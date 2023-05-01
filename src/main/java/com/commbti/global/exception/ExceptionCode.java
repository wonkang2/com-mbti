package com.commbti.global.exception;

import lombok.Getter;

public enum ExceptionCode {
    INVALID_PAGE_REQUEST(400, "페이지 요청을 다시 확인해주세요."),
    UNSUPPORTED_EXTENSIONS(400, "지원하지 않는 확장자입니다."),
    FORBIDDEN(403, "권한이 없습니다."),
    IMAGE_NOT_FOUND(404, "해당 이미지가 존재하지 않습니다."),
    BULLETIN_NOT_FOUND(404, "해당 게시글이 존재하지 않습니다."),
    COMMENT_NOT_FOUND(404, "해당 댓글이 존재하지 않습니다."),
    USERNAME_ALREADY_EXISTS(409, "해당 아이디는 이미 존재합니다."),
    EMAIL_ALREADY_EXISTS(409, "해당 이메일은 이미 존재합니다."),
    FILE_UPLOAD_FAILED(500, "서버의 문제로 파일 업로드에 실패하였습니다."),;
    @Getter
    private int status;
    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
