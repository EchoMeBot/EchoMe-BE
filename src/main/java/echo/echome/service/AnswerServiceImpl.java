package echo.echome.service;

import echo.echome.dto.ReqAnswersToQues;
import echo.echome.dto.ReqEachAnswer;
import echo.echome.dto.ResAllAnswers;
import echo.echome.entity.Answer;
import echo.echome.entity.Member;
import echo.echome.entity.Question;
import echo.echome.exception.AppException;
import echo.echome.exception.ErrorCode;
import echo.echome.repository.AnswerRepository;
import echo.echome.repository.MemberRepository;
import echo.echome.repository.QuestionRepository;
import echo.echome.utils.JwtUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    /**
     * 질문 리스트 -> 질문 id, 질문 내용이
     * @param answersToQues: <질문 id, 답변 내용>의 리스트
     * @param memberId
     */
    @Override
    public void makeAnswerToQuestions(List<ReqAnswersToQues> answersToQues,Long memberId) {

        for (ReqAnswersToQues reqAnswersToQues :answersToQues) {

            Question question = questionRepository.findById(reqAnswersToQues.getQuesId())
                    .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

            Member findMember = memberRepository.findById(memberId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            Answer answer = Answer.builder()
                    .question(question)
                    .content(reqAnswersToQues.getContent())
                    .member(findMember)
                    .build();

            answerRepository.save(answer);
        }
    }

    @Override
    public List<ResAllAnswers> getAllAnswersByMemberId(Long memberId) {

        List<Answer> allAnswerByMemberId = answerRepository.findAllByMemberId(memberId);

        List<ResAllAnswers> returnValue = new ArrayList<>();

        for (Answer answer : allAnswerByMemberId){
            Question question = questionRepository.findById(answer.getQuestion().getId())
                    .orElseThrow(()->new AppException(ErrorCode.QUESTION_NOT_FOUND));
            ResAllAnswers resAns = ResAllAnswers.builder()
                    .answer(answer.getContent())
                    .question(question.getContent())
                    .build();
            returnValue.add(resAns);
        }

        return returnValue;
    }

    @Override
    public void updateAnswer(ReqEachAnswer request) {
        Answer findAnswer = answerRepository.findByMemberIdAndQuestionId(request.getMemberId(),
                request.getQuesId())
                .orElseThrow(()->new AppException(ErrorCode.ANSWER_NOT_FOUNT));
        findAnswer.updateAnswer(request.getAnswer());
        log.info("answer Content: {}",findAnswer.getContent());
        answerRepository.save(findAnswer);
    }
}
