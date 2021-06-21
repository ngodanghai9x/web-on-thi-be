package vn.actvn.onthionline.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.domain.QuestionResult;
import vn.actvn.onthionline.service.dto.ExamAnswerDto;
import vn.actvn.onthionline.service.dto.QuestionAnswerDto;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoExamRequest {
    private Integer id;
    private Integer time;
    private List<Integer> numOptionPicked = new ArrayList<>();
    private List<QuestionAnswerDto> questionAnswer = new ArrayList<>();
}
