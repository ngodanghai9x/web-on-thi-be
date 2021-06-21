package vn.actvn.onthionline.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.service.dto.ExamForUserDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetExamFromUserResponse {
    private ExamForUserDto exam;
}
