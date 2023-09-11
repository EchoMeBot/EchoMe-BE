package echo.echome.service;

import echo.echome.dto.*;
import echo.echome.utils.Token;

import java.util.List;

public interface MemberService {

    /**
     * 회원 가입
     * @param newMember
     * @return
     */
    ResCreateMember createNewMember(ReqCreateMember newMember);

    Token login(String email, String password);

    /**
     *
     */
    void writeAnswerToQuestions(List<ReqAnswersToQues> request, Long memberId);

    List<ResAllAnswers> getAllAnswers(Long memberId);

    String makeContext(List<ResAllAnswers> listOfAnswer);
}
