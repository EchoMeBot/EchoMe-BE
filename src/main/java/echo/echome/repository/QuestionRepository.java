package echo.echome.repository;

import echo.echome.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

    Optional<Question> findByQuesNum(Long quesNum);
}
