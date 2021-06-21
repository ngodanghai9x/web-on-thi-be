package vn.actvn.onthionline.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionForUserDto {
    private Integer id;
    private String question;
    private String type;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
}
