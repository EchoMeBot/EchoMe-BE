package echo.echome;

import echo.echome.dto.ReqAnswersToQues;
import echo.echome.dto.ReqCreateMember;
import echo.echome.dto.ReqNewQuestion;
import echo.echome.entity.Answer;
import echo.echome.entity.Member;
import echo.echome.entity.Question;
import echo.echome.repository.AnswerRepository;
import echo.echome.repository.MemberRepository;
import echo.echome.repository.QuestionRepository;
import echo.echome.service.AnswerService;
import echo.echome.service.AnswerServiceImpl;
import echo.echome.service.MemberServiceImpl;
import echo.echome.service.QuestionService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
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
        private final QuestionService questionService;
        private final AnswerRepository answerRepository;
        public void dbInit() {
//            ReqCreateMember rm = new ReqCreateMember("lcy923@naver.com", "이채영", "1111", "12341234123");
//            memberService.createNewMember(rm);
            List<ReqNewQuestion> questionList = Arrays.asList(
                    new ReqNewQuestion(1L,"좋아하는 음식이 뭐야?"),
                    new ReqNewQuestion(2L,"좋아하는 음악이 뭐야?"),
                    new ReqNewQuestion(3L,"취미가 뭐야?"),
                    new ReqNewQuestion(4L,"키가 몇cm야?"),
                    new ReqNewQuestion(5L,"몇 살이야?"),
                    new ReqNewQuestion(6L,"MBTI가 뭐야?"),
                    new ReqNewQuestion(7L,"다니는 학교가 어디야?"),
                    new ReqNewQuestion(8L,"어디에 살아?"),
                    new ReqNewQuestion(9L,"성별은?"),
                    new ReqNewQuestion(10L,"아무일 없이 쉬는날엔 뭐해?"),
                    new ReqNewQuestion(11L,"연애중이니?"),
                    new ReqNewQuestion(12L, "가고 싶은 여행지 있어?"),
                    new ReqNewQuestion(13L,"전공은 뭐야?"),
                    new ReqNewQuestion(14L, "깻잎논쟁에 찬성해 반대해?"),
                    new ReqNewQuestion(15L,"면vs밥 뭐가 더 좋아?")

            );

            for (ReqNewQuestion request : questionList) {
                questionService.addQuestion(request.getQuesNumber(),request.getQuestionContent());
            }

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
