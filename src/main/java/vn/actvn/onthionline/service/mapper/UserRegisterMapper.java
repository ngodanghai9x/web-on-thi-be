package vn.actvn.onthionline.service.mapper;

import org.springframework.stereotype.Service;
import vn.actvn.onthionline.domain.User;
import vn.actvn.onthionline.service.dto.UserRegisterDto;
import org.modelmapper.ModelMapper;

@Service
public class UserRegisterMapper {
    public User toEntity(UserRegisterDto userRegisterDto){
        if (userRegisterDto == null)
            return null;
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userRegisterDto,User.class);
        return user;
    }
}
