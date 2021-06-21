package vn.actvn.onthionline.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.service.dto.UserDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequest {
    private UserDto userDto;
}
