package vn.actvn.onthionline.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.actvn.onthionline.domain.Exam;
import vn.actvn.onthionline.service.dto.ExamForUserDto;
import vn.actvn.onthionline.service.dto.QuestionForUserDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamForUserMapper {
    @Autowired
    private QuestionForUserMapper examQuestionMapper;

    public ExamForUserDto toDto(Exam exam, Integer numRework) {
        if (exam == null)
            return null;

        ExamForUserDto examDto = new ExamForUserDto();
        examDto.setId(exam.getId());
        examDto.setName(exam.getName());
        examDto.setTime(exam.getTime());
        examDto.setNumPeopleDid(exam.getNumPeopleDid());
        examDto.setNumRework(numRework);
        examDto.setDescription(exam.getDescription());

        List<QuestionForUserDto> examQuestionDtos = exam.getQuestions().stream().map(examQuestionMapper::toDto).collect(Collectors.toList());;
        if (exam.getMixedQuestion() == true) {
           Collections.shuffle(examQuestionDtos, new Random());
           examQuestionDtos.stream().forEach(question -> {
               List<String> listAnswers = new ArrayList<>();
               listAnswers.add(question.getOption1());
               listAnswers.add(question.getOption2());
               listAnswers.add(question.getOption3());
               listAnswers.add(question.getOption4());
               Collections.shuffle(listAnswers, new Random());
               question.setOption1(listAnswers.get(0));
               question.setOption2(listAnswers.get(1));
               question.setOption3(listAnswers.get(2));
               question.setOption4(listAnswers.get(3));
           });
        }

        examDto.setExamQuestions(examQuestionDtos);
        return examDto;
    }
}
