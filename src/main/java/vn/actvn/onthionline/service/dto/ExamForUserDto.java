package vn.actvn.onthionline.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamForUserDto {
    private Integer id;
    private String name;
    private Integer numPeopleDid;
    private Integer numRework;
    private Integer time;
    private String description;
    private List<QuestionForUserDto> examQuestions;
}
