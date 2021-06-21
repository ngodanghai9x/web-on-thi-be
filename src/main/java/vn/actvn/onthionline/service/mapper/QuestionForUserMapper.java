package vn.actvn.onthionline.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.actvn.onthionline.domain.Question;
import vn.actvn.onthionline.service.dto.QuestionForUserDto;

@Service
public class QuestionForUserMapper {
    public QuestionForUserDto toDto(Question question) {
        if (question == null)
            return null;

        ModelMapper modelMapper = new ModelMapper();
        QuestionForUserDto examQuestionDto = modelMapper.map(question, QuestionForUserDto.class);
        return examQuestionDto;
    }
}
