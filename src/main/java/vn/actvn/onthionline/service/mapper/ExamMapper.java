package vn.actvn.onthionline.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.actvn.onthionline.domain.Exam;
import vn.actvn.onthionline.service.dto.ExamDto;
import vn.actvn.onthionline.service.dto.QuestionDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamMapper {
    @Autowired
    private QuestionMapper questionMapper;

    public ExamDto toDto(Exam exam) {
        if (exam == null)
            return null;

        ExamDto examDto = new ExamDto();
        examDto.setId(exam.getId());
        examDto.setName(exam.getName());
        examDto.setCode(exam.getCode());
        examDto.setDescription(exam.getDescription());
        examDto.setSubject(exam.getSubject());
        examDto.setGrade(exam.getGrade());
        examDto.setNumQuestion(exam.getNumQuestion());
        examDto.setTime(exam.getTime());
        if (null != exam.getExamHistory() && exam.getExamHistory().size() > 0)
            examDto.setCanDelete(false);
        else
            examDto.setCanDelete(true);
        examDto.setIsActive(exam.isActive());
        examDto.setMixedQuestion(exam.getMixedQuestion());
        List<QuestionDto> questionDtos = exam.getQuestions().stream().map(questionMapper::toDto).collect(Collectors.toList());
        examDto.setExamQuestions(questionDtos);
        return examDto;
    }

    public Exam toEntity(ExamDto examDto) {
        if (examDto == null)
            return null;

        Exam exam = new Exam();
        exam.setId(examDto.getId());
        exam.setName(examDto.getName());
        exam.setCode(examDto.getCode());
        exam.setDescription(examDto.getDescription());
        exam.setSubject(examDto.getSubject());
        exam.setTime(examDto.getTime());
        exam.setGrade(examDto.getGrade());
        exam.setMixedQuestion(examDto.getMixedQuestion());
        return exam;
    }
}
