package echo.echome.service;

import echo.echome.dto.ResAllQuestion;

import java.util.List;

public interface QuestionService {

    /**
     * 등록된 모든 질문들을 받아온다.
     * @return 등록된 모든 질문리스트
     */
    List<ResAllQuestion> getAllQuestions();

    Long addQuestion(Long number, String content);
}
