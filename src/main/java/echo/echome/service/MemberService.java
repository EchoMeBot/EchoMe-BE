package echo.echome.service;

import echo.echome.dto.ReqAnswersToQues;
import echo.echome.dto.ReqCreateMember;
import echo.echome.dto.ResAllAnswers;
import echo.echome.dto.ResCreateMember;

import java.util.List;

public interface MemberService {

    /**
     * 회원 생성
     * @param newMember
     * @return
     */
    ResCreateMember createNewMember(ReqCreateMember newMember);

    /**
     *
     */
    void writeAnswerToQuestions(List<ReqAnswersToQues> request, Long memberId);

    List<ResAllAnswers> getAllAnswers(Long memberId);
}
