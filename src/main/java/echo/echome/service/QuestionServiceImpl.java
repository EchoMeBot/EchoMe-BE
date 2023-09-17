package echo.echome.service;

import echo.echome.dto.ResAllQuestion;
import echo.echome.entity.Question;
import echo.echome.exception.AppException;
import echo.echome.exception.ErrorCode;
import echo.echome.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;
    @Override
    public List<ResAllQuestion> getAllQuestions() {
        List<Question> allQuestions = questionRepository.findAll();

        List<ResAllQuestion> allResQuestions = new ArrayList<>();
        for (Question question: allQuestions) {
            ResAllQuestion quesDto = ResAllQuestion.builder()
                    .content(question.getContent())
                    .quesNum(question.getQuesNum())
                    .build();
            allResQuestions.add(quesDto);

        }
        return allResQuestions;
    }

    @Override
    @Transactional(readOnly = false)
    public Long addQuestion(Long number, String content) {

        questionRepository.findByQuesNum(number)
                .ifPresent(question->{
                     throw new AppException(ErrorCode.QUESTION_NUMBER_DUPLICATED);
        });

        Question newQuestion = Question.builder()
                .quesNum(number)
                .content(content)
                .build();
        questionRepository.save(newQuestion);
        return newQuestion.getId();
    }
}
