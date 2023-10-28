package echo.echome.service;

import echo.echome.dto.ReqAnswersToQues;
import echo.echome.dto.ReqEachAnswer;
import echo.echome.dto.ResAllAnswers;

import java.util.List;

public interface AnswerService {

    /**
     * 회원이 받은 질문에 답변하는 기능
     * @param answersToQues: <질문 id, 답변 내용>의 리스트
     */
    void makeAnswerToQuestions(List<ReqAnswersToQues> answersToQues,Long memberId);

    /**
     * 회원이 질문에 한 답변인 <질문, 답변> 리스트를 받아온다.
     * @param memberId 답변한 회원
     * @return <질문,답변> 리스트
     */
    List<ResAllAnswers> getAllAnswersByMemberId(Long memberId);

//    void answerOneQuestion(String accessToken, Long quesId,String answerContent);

    void updateAnswer(ReqEachAnswer request);
}
