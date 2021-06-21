package vn.actvn.onthionline.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.actvn.onthionline.domain.Exam;
import vn.actvn.onthionline.repository.ExamHistoryRepository;
import vn.actvn.onthionline.service.dto.ExamInfoDto;

@Service
public class ExamInfoMapper {
    public static ExamInfoDto toDto(Exam exam) {
        if (exam == null)
            return null;
        ModelMapper modelMapper = new ModelMapper();
        ExamInfoDto examInfoDto = modelMapper.map(exam,ExamInfoDto.class);
        return examInfoDto;
    }
}
