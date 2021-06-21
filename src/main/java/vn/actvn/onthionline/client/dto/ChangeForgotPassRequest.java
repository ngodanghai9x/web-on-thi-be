package vn.actvn.onthionline.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeForgotPassRequest {
    private String newPassword;
    private Integer otp;
    private String username;
    private String email;
}
