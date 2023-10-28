package echo.echome.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Table(name = "answer", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ques_id", "member_id"})
})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @Column(name = "ans_content")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ques_id")
    private Question question;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateAnswer(String content){
        this.content = content;
    }

}
