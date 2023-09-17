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

    /**
     * 회원이 한 모든 질문, 답변 List 불러오기
     * 답변을 하지 않고 새로 추가된 질문이라면, 빈스트링으로 받아오기 (Null이 아닌)
     * @param memberId
     * @return
     */
    List<ResAllAnswers> getAllAnswersByMemberId(Long memberId);
}
