package vn.actvn.onthionline.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.service.dto.UserProfileDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileInfoResponse {
    private UserProfileDto userProfile;
}
