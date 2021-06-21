package vn.actvn.onthionline.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResultDto {
    private Integer id;
    private String image;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String suggestion;
    private String answer;
    private String correctAnswer;
}