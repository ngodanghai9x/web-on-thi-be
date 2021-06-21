package vn.actvn.onthionline.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.service.dto.DeleteListDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteQuestionResponse {
    List<DeleteListDto> deleteQuestionDtos;
}
