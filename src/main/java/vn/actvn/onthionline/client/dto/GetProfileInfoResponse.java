package vn.actvn.onthionline.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.domain.ExamHistory;
import vn.actvn.onthionline.service.dto.UserProfileDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProfileInfoResponse {
    private UserProfileDto userProfile;
}
