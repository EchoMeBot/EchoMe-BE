package echo.echome.service;

import echo.echome.dto.*;
import echo.echome.entity.Answer;
import echo.echome.entity.Member;
import echo.echome.entity.Question;
import echo.echome.exception.AppException;
import echo.echome.exception.ErrorCode;
import echo.echome.repository.AnswerRepository;
import echo.echome.repository.MemberRepository;
import echo.echome.repository.QuestionRepository;
import echo.echome.utils.JwtUtil;
import echo.echome.utils.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final AnswerService answerService;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;


    @Override
    public ResCreateMember createNewMember(ReqCreateMember newMember) {
        memberRepository.findByEmail(newMember.getEmail())
                .ifPresent(member->{
                    throw new AppException(ErrorCode.EMAIL_DUPLICATED);
                });

        Member member = Member.builder()
                .name(newMember.getName())
                .email(newMember.getEmail())
                .encryptedPwd(encoder.encode(newMember.getPassword()))
                .phoneNum(newMember.getPhoneNum())
                .createdAt(LocalDateTime.now())
                .build();
        memberRepository.save(member);

        return ResCreateMember.builder()
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }

    @Override
    public Token login(String email, String password) {
        Member findeMember = memberRepository.findByEmail(email)
                .orElseThrow(()-> new AppException(ErrorCode.EMAIL_NOT_FOUND));

        if (!encoder.matches(password,findeMember.getEncryptedPwd())){
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
        Token token = jwtUtil.createToken(email);
        findeMember.updateRefreshToken(token.getRefreshToken());
        return token;

    }

    @Override
    public void writeAnswerToQuestions(List<ReqAnswersToQues> request, Long memberId) {
        answerService.makeAnswerToQuestions(request,memberId);
    }

    @Override
    public List<ResAllAnswers> getAllAnswers(Long memberId) {
        return answerService.getAllAnswersByMemberId(memberId);
    }

    @Override
    public String makeContext(List<ResAllAnswers> listOfAnswer) {
        StringBuilder context = new StringBuilder();
        int cnt = 1;
        for (ResAllAnswers resAllAnswers : listOfAnswer) {
            context.append(cnt).append(". ").append(resAllAnswers.getQuestion()).append("라는 질문에 대한 대답은 ").append(resAllAnswers.getAnswer()).append(", ");
            cnt++;
        }
        return context.toString();
    }

    @Override
    public List<ResAllAnswers> getAllAnswersByMemberId(Long memberId) {
        List<ResAllAnswers> result = new ArrayList<>();
        List<Answer> allAnswersFromMember = answerRepository.findAllByMemberId(memberId);
        List<Question> allQuestions = questionRepository.findAll();
        for (Question questions: allQuestions) {

            ResAllAnswers noReply = ResAllAnswers.builder()
                    .quesNum(questions.getQuesNum())
                    .question(questions.getContent())
                    .answer("")
                    .build();

            ResAllAnswers ResAnswer = allAnswersFromMember.stream()
                    .filter(answer -> answer.getId().equals(questions.getId()))
                    .map(answer -> ResAllAnswers.builder()
                            .answer(answer.getContent())
                            .quesNum(questions.getQuesNum())
                            .question(questions.getContent())
                            .build())
                    .findAny()
                    .orElse(noReply);
            result.add(ResAnswer);
        }
        return result;
    }
}
