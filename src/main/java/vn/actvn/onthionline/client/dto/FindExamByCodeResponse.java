package vn.actvn.onthionline.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.service.dto.ExamDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindExamByCodeResponse {
    private List<ExamDto> exams;
}
