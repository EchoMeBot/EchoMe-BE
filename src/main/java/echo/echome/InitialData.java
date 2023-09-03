package echo.echome;

import echo.echome.dto.ReqAnswersToQues;
import echo.echome.dto.ReqCreateMember;
import echo.echome.entity.Answer;
import echo.echome.entity.Member;
import echo.echome.entity.Question;
import echo.echome.repository.AnswerRepository;
import echo.echome.repository.MemberRepository;
import echo.echome.repository.QuestionRepository;
import echo.echome.service.AnswerService;
import echo.echome.service.AnswerServiceImpl;
import echo.echome.service.MemberServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InitialData {

    private final InitService initService;
    @PostConstruct
    public void init(){
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final MemberServiceImpl memberService;
        private final MemberRepository memberRepository;
        private final QuestionRepository questionRepository;
        private final AnswerRepository answerRepository;
        public void dbInit() {
            ReqCreateMember rm = new ReqCreateMember("lcy923@naver.com", "이채영", "1111", "12341234123");
            memberService.createNewMember(rm);

           /* List<ReqAnswersToQues> answersToQues = new ArrayList<>();

            answersToQues.add(new ReqAnswersToQues(1L, "닭발"));
            answersToQues.add(new ReqAnswersToQues(2L, "edm"));
            answersToQues.add(new ReqAnswersToQues(3L, "웹툰"));
            answersToQues.add(new ReqAnswersToQues(4L, "160"));
            answersToQues.add(new ReqAnswersToQues(5L, "24"));


            for (ReqAnswersToQues reqAnswersToQues : answersToQues) {
                Optional<Question> quesOpt = questionRepository.findById(reqAnswersToQues.getQuesId());
                Question ques = quesOpt.get();
                Optional<Member> memberOpt = memberRepository.findById(1L);
                Member member = memberOpt.get();
                Answer answer = Answer.builder()
                        .question(ques)
                        .content(reqAnswersToQues.getContent())
                        .member(member)
                        .build();
                answerRepository.save(answer);

            }*/

        }
    }
}
