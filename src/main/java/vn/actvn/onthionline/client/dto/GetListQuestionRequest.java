package vn.actvn.onthionline.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetListQuestionRequest {
    private Integer pageNumber;
    private Integer pageSize;
    private String question;
    private String mode;
    private String subject;
    private String grade;
    private String type;
}
