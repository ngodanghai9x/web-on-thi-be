package vn.actvn.onthionline.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.actvn.onthionline.domain.User;
import vn.actvn.onthionline.service.dto.UserProfileDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProfileMapper {
    @Autowired
    private ExamHistoryMapper examHistoryMapper;

    public UserProfileDto toDto(User user){
        if (user == null)
            return null;
        ModelMapper modelMapper = new ModelMapper();
        UserProfileDto userDto = modelMapper.map(user,UserProfileDto.class);
        return userDto;
    }

    public User toEntity(UserProfileDto userDto) {
        if (userDto == null)
            return null;
        User user = new User();
        user.setFullname(userDto.getFullname());
        user.setPhone(userDto.getPhone());
        user.setBirthday(userDto.getBirthday());
        user.setGender(userDto.getGender());
        user.setCity(userDto.getCity());
        user.setClazz(userDto.getClazz());
        user.setSchool(userDto.getSchool());
        return user;
    }
}
