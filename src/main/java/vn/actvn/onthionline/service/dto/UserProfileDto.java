package vn.actvn.onthionline.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.domain.ExamHistory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private String fullname;
    private String email;
    private String phone;
    private String avatar;
    private Date birthday;
    private String gender;
    private String city;
    private String clazz;
    private String school;
    private Integer onlineTime;
    private Date lastLogin;
}
