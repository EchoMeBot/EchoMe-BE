package echo.echome.service;

import echo.echome.dto.ReqAnswersToQues;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

            //todo answer를 build할때, memberId, quesId쌍이 Unique하도록 되도록

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
    public void answerOneQuestion(String accessToken, Long quesId,String answerContent) {
        String email = jwtUtil.getEmail(accessToken);
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(()->new AppException(ErrorCode.EMAIL_NOT_FOUND));
        log.info("findMember: {}",findMember.getName());
        Question findQuestion = questionRepository.findById(quesId)
                .orElseThrow(()->new AppException(ErrorCode.QUESTION_NOT_FOUND));
        log.info("findQuestion: {}",findQuestion.getContent());

        Answer answer = Answer.builder()
                .question(findQuestion)
                .member(findMember)
                .content(answerContent)
                .build();

        //todo 중복저장 방지해야함.
        log.info("postedAnswer: {}",answer.getId());
        answerRepository.save(answer);

    }
}
