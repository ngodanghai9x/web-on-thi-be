package vn.actvn.onthionline.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamDto {
    private Integer id;
    private String name;
    private String code;
    private String subject;
    private String grade;
    private String description;
    private Boolean mixedQuestion;
    private Integer numQuestion;
    private Integer time;
    private Boolean isActive;
    private Boolean canDelete;
    private List<QuestionDto> examQuestions;
}
