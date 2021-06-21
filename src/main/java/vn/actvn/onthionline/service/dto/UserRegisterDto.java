package vn.actvn.onthionline.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.domain.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    private String username;
    private String fullname;
    private String password;
    private String email;
    private String phone;
    private String city;

    public UserRegisterDto(User user) {
        this.username = user.getUsername();
        this.fullname = user.getFullname();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.city = user.getCity();
    }
}
