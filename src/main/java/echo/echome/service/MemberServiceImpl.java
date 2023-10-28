package echo.echome.service;

import echo.echome.dto.ReqCreateMember;
import echo.echome.dto.ReqEachAnswer;
import echo.echome.dto.ResAllAnswers;
import echo.echome.dto.ResCreateMember;
import echo.echome.dto.ResMemberInfo;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final AnswerService answerService;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    private static final String EMPTY_STRING = "";

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

        List<Question> allQuestion = questionRepository.findAll();
        for (Question ques : allQuestion){
            Answer answer = Answer.builder()
                    .content(EMPTY_STRING)
                    .member(member)
                    .question(ques)
                    .build();
            answerRepository.save(answer);
        }
        return ResCreateMember.builder()
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }

    @Override
    public Token login(String email, String password) {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(()-> new AppException(ErrorCode.EMAIL_NOT_FOUND));

        if (!encoder.matches(password,findMember.getEncryptedPwd())){
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
        Token token = jwtUtil.createToken(email);
        findMember.updateRefreshToken(token.getRefreshToken());
        return token;

    }

    @Override
    public ResMemberInfo getMemberInfo(String accessToken) {
        String email = jwtUtil.getEmail(accessToken);
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(()->new AppException(ErrorCode.EMAIL_NOT_FOUND));

        return ResMemberInfo.builder()
                .memberId(findMember.getId())
                .email(email)
                .name(findMember.getName())
                .build();
    }

    @Override
    public void updateMember(String accessToken, ReqUpdateMember request) {
        Member findMemberById = memberRepository.findById(request.getMemberId())
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        log.info("ById : {}",findMemberById);

        String email = jwtUtil.getEmail(accessToken);
        Member findMemberByEmail = memberRepository.findByEmail(email)
                        .orElseThrow(()->new AppException(ErrorCode.EMAIL_NOT_FOUND));
        log.info("ByEmail : {}",findMemberByEmail);
        if (findMemberById.getId().equals(findMemberByEmail.getId())){
            findMemberById.updateName(request.getName());
            memberRepository.save(findMemberById);
        }else {
            throw new AppException(ErrorCode.AUTHORIZATION_ERROR);
        }
    }

    @Override
    public void writeAnswerToQuestions(List<ReqAnswersToQues> request, String accessToken) {
        String email = jwtUtil.getEmail(accessToken);
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(()->new AppException(ErrorCode.EMAIL_NOT_FOUND));
        Long memberId = findMember.getId();

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
    public List<ResAllAnswers> getAllAnswersByMemberId(String accessToken) {
        List<ResAllAnswers> result = new ArrayList<>();
        String email = jwtUtil.getEmail(accessToken);
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(()->new AppException(ErrorCode.EMAIL_NOT_FOUND));
        Long memberId = findMember.getId();

        List<Answer> allAnswersFromMember = answerRepository.findAllByMemberId(memberId);
        List<Question> allQuestions = questionRepository.findAll();
        for (Question questions: allQuestions) {

            ResAllAnswers noReply = ResAllAnswers.builder()
                    .quesId(questions.getId())
                    .question(questions.getContent())
                    .answer("")
                    .build();

            ResAllAnswers ResAnswer = allAnswersFromMember.stream()
                    .filter(answer -> answer.getQuestion().getId().equals(questions.getId()))
                    .map(answer -> ResAllAnswers.builder()
                            .answer(answer.getContent())
                            .quesId(questions.getId())
                            .question(questions.getContent())
                            .build())
                    .findAny()
                    .orElse(noReply);
            result.add(ResAnswer);
        }
        return result;
    }

    @Override
    public void answerToOneQuestion(String accessToken, ReqEachAnswer request) {
        log.info("request: {}",request.getQuesId());
        answerService.answerOneQuestion(accessToken,request.getQuesId(), request.getAnswer());
    }
}
