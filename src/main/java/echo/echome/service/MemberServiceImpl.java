package echo.echome.service;

import echo.echome.dto.ReqAnswersToQues;
import echo.echome.dto.ReqCreateMember;
import echo.echome.dto.ResAllAnswers;
import echo.echome.dto.ResCreateMember;
import echo.echome.entity.Member;
import echo.echome.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final AnswerService answerService;


    @Override
    public ResCreateMember createNewMember(ReqCreateMember newMember) {

        Member member = Member.builder()
                .name(newMember.getName())
                .email(newMember.getEmail())
                .encryptedPwd("encrypted_pwd")
                .phoneNum(newMember.getPhoneNum())
                .build();
        memberRepository.save(member);

        return ResCreateMember.builder()
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }

    @Override
    public void writeAnswerToQuestions(List<ReqAnswersToQues> request, Long memberId) {
        answerService.makeAnswerToQuestions(request,memberId);
    }

    @Override
    public List<ResAllAnswers> getAllAnswers(Long memberId) {
        return answerService.getAllAnswersByMemberId(memberId);
    }
}
