package vn.actvn.onthionline.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.common.utils.OptimizedPage;
import vn.actvn.onthionline.service.dto.ExamHistoryDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetListHistoryResponse {
    private OptimizedPage<ExamHistoryDto> examHistoryDtos;
}
