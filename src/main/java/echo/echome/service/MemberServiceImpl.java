package echo.echome.service;

import echo.echome.dto.ReqCreateMember;
import echo.echome.dto.ReqUpdateMember;
import echo.echome.dto.ResAllAnswers;
import echo.echome.dto.ResCreateMember;
import echo.echome.dto.ResMakeContext;
import echo.echome.dto.ResMemberInfo;
import echo.echome.dto.ResMemberLink;
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
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService {

    private static final String EMPTY_STRING = "";
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final AnswerService answerService;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    @Override
    public ResCreateMember createNewMember(ReqCreateMember newMember) {
        memberRepository.findByEmail(newMember.getEmail())
                .ifPresent(member -> {
                    throw new AppException(ErrorCode.EMAIL_DUPLICATED);
                });

        Member member = Member.builder()
                .name(newMember.getName())
                .email(newMember.getEmail())
                .encryptedPwd(encoder.encode(newMember.getPassword()))
                .phoneNum(newMember.getPhoneNum())
                .createdAt(LocalDateTime.now())
                .unique(UUID.randomUUID())
                .build();
        memberRepository.save(member);

        List<Question> allQuestion = questionRepository.findAll();
        for (Question ques : allQuestion) {
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
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));

        if (!encoder.matches(password, findMember.getEncryptedPwd())) {
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
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));

        return ResMemberInfo.builder()
                .memberId(findMember.getId())
                .email(email)
                .name(findMember.getName())
                .unique(findMember.getUnique())
                .build();
    }

    @Override
    public void updateMember(String accessToken, ReqUpdateMember request) {
        Member findMemberById = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        log.info("ById : {}", findMemberById);

        String email = jwtUtil.getEmail(accessToken);
        Member findMemberByEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));
        log.info("ByEmail : {}", findMemberByEmail);
        if (findMemberById.getId().equals(findMemberByEmail.getId())) {
            findMemberById.updateName(request.getName());
            memberRepository.save(findMemberById);
        } else {
            throw new AppException(ErrorCode.AUTHORIZATION_ERROR);
        }
    }

    @Override
    public List<ResAllAnswers> getAllAnswers(Long memberId) {
        return answerService.getAllAnswersByMemberId(memberId);
    }

    @Override
    public ResMakeContext makeContext(Long memberId) {
        StringBuilder context = new StringBuilder();

        List<Answer> allAnswer = answerRepository.findAllByMemberId(memberId);

        int cnt = 1;
        for (Answer answer : allAnswer) {
            Question findQuestion = answer.getQuestion();
            String memberAnswer = "";
            if (EMPTY_STRING.equals(answer.getContent())) {
                memberAnswer = "미응답 상태";
            } else {
                memberAnswer = answer.getContent();
            }
            String contextItem = cnt + ". " + findQuestion.getContent() + "라는 질문에 대한 답은 " + memberAnswer + "\n";
            log.info("추가된 답변 : {}", contextItem);
            context.append(contextItem);

            cnt++;
        }
        return ResMakeContext.builder()
                .context(context.toString())
                .build();
    }

    @Override
    public List<ResAllAnswers> getAllAnswersByMemberId(String accessToken) {
        List<ResAllAnswers> result = new ArrayList<>();
        String email = jwtUtil.getEmail(accessToken);
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));
        Long memberId = findMember.getId();

        List<Answer> allAnswersFromMember = answerRepository.findAllByMemberId(memberId);
        List<Question> allQuestions = questionRepository.findAll();
        for (Question questions : allQuestions) {

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
    public ResMemberLink getUniqueLink(String accessToken) {
        String email = jwtUtil.getEmail(accessToken);
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));

        return ResMemberLink.builder()
                .link(findMember.getUnique())
                .build();
    }

    @Override
    public ResMakeContext makeContextByUUID(UUID unique) {
        Member findMember = memberRepository.findByUnique(unique)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Long memberId = findMember.getId();

        StringBuilder context = new StringBuilder();

        List<Answer> allAnswer = answerRepository.findAllByMemberId(memberId);

        int cnt = 1;
        for (Answer answer : allAnswer) {
            Question findQuestion = answer.getQuestion();
            String memberAnswer = "";
            if (EMPTY_STRING.equals(answer.getContent())) {
                memberAnswer = "미응답 상태";
            } else {
                memberAnswer = answer.getContent();
            }
            String contextItem = cnt + ". " + findQuestion.getContent() + "라는 질문에 대한 답은 " + memberAnswer + "\n";
            log.info("추가된 답변 : {}", contextItem);
            context.append(contextItem);

            cnt++;
        }
        return ResMakeContext.builder()
                .context(context.toString())
                .build();
    }
}
