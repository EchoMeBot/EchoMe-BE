package echo.echome.service;

import echo.echome.dto.ReqAnswersToQues;
import echo.echome.dto.ResAllAnswers;
import echo.echome.entity.Answer;
import echo.echome.entity.Member;
import echo.echome.entity.Question;
import echo.echome.repository.AnswerRepository;
import echo.echome.repository.MemberRepository;
import echo.echome.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    @Override
    public void makeAnswerToQuestions(List<ReqAnswersToQues> answersToQues,Long memberId) {


        for (ReqAnswersToQues reqAnswersToQues :answersToQues) {
            Optional<Question> quesOpt = questionRepository.findById(reqAnswersToQues.getQuesId());
            Question ques = quesOpt.get();
            Optional<Member> memberOpt = memberRepository.findById(memberId);
            Member member = memberOpt.get();
            Answer answer = Answer.builder()
                    .question(ques)
                    .content(reqAnswersToQues.getContent())
                    .member(member)
                    .build();
            answerRepository.save(answer);
        }
    }

    @Override
    public List<ResAllAnswers> getAllAnswersByMemberId(Long memberId) {

        List<Answer> allAnswerByMemberId = answerRepository.findAllByMemberId(memberId);

        List<ResAllAnswers> returnValue = new ArrayList<>();

        for (Answer answer : allAnswerByMemberId){
            Question question = questionRepository.findById(answer.getQuestion().getId()).get();
            ResAllAnswers resAns = ResAllAnswers.builder()
                    .answer(answer.getContent())
                    .question(question.getContent())
                    .build();
            returnValue.add(resAns);
        }


        return returnValue;
    }
}
