package vn.actvn.onthionline.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamInfoDto {
    private Integer id;
    private String name;
    private String subject;
    private String grade;
    private String description;
    private Integer numQuestion;
    private Integer numPeopleDid;
    private Integer time;
}
