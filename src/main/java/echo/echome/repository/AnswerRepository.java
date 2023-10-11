package echo.echome.repository;

import echo.echome.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {
    List<Answer> findAllByMemberId(Long memberId);
    Optional<Answer> findByMemberIdAndQuestionId(Long memberId, Long quesId);
}
