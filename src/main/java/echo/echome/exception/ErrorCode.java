package echo.echome.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 회원을 찾을 수 없습니다."),
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 id를 가진 질문을 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 email을 가진 회원을 찾을 수 없습니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT,"해당 email을 가진 회원이 이미 존재합니다."),
    QUESTION_NUMBER_DUPLICATED(HttpStatus.CONFLICT,"해당 번호의 질문이 이미 존재합니다."),
    CANNOT_MAKE_ANSWER(HttpStatus.NO_CONTENT,"해당 답변을 반환할 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"잘못된 비밀번호 입니다.");



    private HttpStatus httpStatus;
    private String message;

}
