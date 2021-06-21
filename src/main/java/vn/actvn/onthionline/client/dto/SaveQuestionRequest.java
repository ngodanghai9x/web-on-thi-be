package vn.actvn.onthionline.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.service.dto.QuestionDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveQuestionRequest {
    private QuestionDto question;
}
