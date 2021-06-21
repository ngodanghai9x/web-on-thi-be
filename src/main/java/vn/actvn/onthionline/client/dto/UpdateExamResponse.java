package vn.actvn.onthionline.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.service.dto.ExamDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExamResponse {
    private ExamDto exam;
}
