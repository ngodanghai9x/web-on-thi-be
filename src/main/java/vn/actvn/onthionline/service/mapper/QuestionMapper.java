package vn.actvn.onthionline.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.actvn.onthionline.common.Constant;
import vn.actvn.onthionline.domain.Question;
import vn.actvn.onthionline.service.dto.QuestionDto;

import java.util.Arrays;
import java.util.List;

@Service
public class QuestionMapper {
    public Question toEntity(QuestionDto questionDto) {
        if (questionDto == null)
            return null;
        Question question = new Question();
        question.setId(questionDto.getId());
        question.setQuestion(questionDto.getQuestion());
        question.setType(questionDto.getType());
        question.setOption1(questionDto.getOption1());
        question.setOption2(questionDto.getOption2());
        question.setOption3(questionDto.getOption3());
        question.setOption4(questionDto.getOption4());
        question.setMode(questionDto.getMode());
        question.setGrade(questionDto.getGrade());
        question.setSubject(questionDto.getSubject());
        question.setSuggestion(questionDto.getSuggestion());
        question.setCorrectAnswer(listCorrectAnswerToString(questionDto.getCorrectAnswer()));
        return question;
    }

    public QuestionDto toDto(Question question) {
        if (question == null)
            return null;

        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(question.getId());
        questionDto.setQuestion(question.getQuestion());
        questionDto.setType(question.getType());
        questionDto.setOption1(question.getOption1());
        questionDto.setOption2(question.getOption2());
        questionDto.setOption3(question.getOption3());
        questionDto.setOption4(question.getOption4());
        questionDto.setMode(question.getMode());
        questionDto.setGrade(question.getGrade());
        questionDto.setSubject(question.getSubject());
        questionDto.setSuggestion(question.getSuggestion());
        questionDto.setCorrectAnswer(correctAnswerToList(question.getCorrectAnswer()));

        return questionDto;
    }

    public String listCorrectAnswerToString(List<String> correctAnswers) {
        String correctAnswer = "";
        for (int i = 0; i < correctAnswers.size(); i++) {
            if (i != correctAnswers.size() - 1){
                correctAnswer += correctAnswers.get(i) + Constant.SEPARATOR;
            } else {
                correctAnswer += correctAnswers.get(i);
            }
        }
        return correctAnswer;
    }

    public List<String> correctAnswerToList(String correctAnswer) {
        return Arrays.asList(correctAnswer.split(Constant.SEPARATOR).clone());
    }
}
