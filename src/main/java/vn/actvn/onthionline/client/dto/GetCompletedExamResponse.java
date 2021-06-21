package vn.actvn.onthionline.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.service.dto.CompletedExamDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCompletedExamResponse {
    private List<CompletedExamDto> completedExamDtos;
}
