package echo.echome.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "question")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ques_id")
    private Long id;

    @Column(name = "ques_num")
    private Long quesNum;
    @Column(name = "ques_content")
    private String content;


}
