package vn.actvn.onthionline.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.domain.QuestionResult;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamHistoryDto {
    private String examName;
    private String examDescription;
    private List<Integer> numOptionPicked;
    private Integer numAnswer;
    private Integer numCorrectAns;
    private Integer totalQuestion;
    private Integer doTime;
    private Integer totalTime;
    private List<QuestionResult> questionResult;
}
