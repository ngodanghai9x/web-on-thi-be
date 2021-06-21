package vn.actvn.onthionline.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllExamRequest {
    private Integer pageNumber;
    private Integer pageSize;
    private String inputSearch;
    private String grade;
    private String subject;
}
