package vn.actvn.onthionline.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.actvn.onthionline.domain.Question;
import vn.actvn.onthionline.domain.QuestionResult;
import vn.actvn.onthionline.repository.QuestionRepository;
import vn.actvn.onthionline.service.dto.ExamAnswerDto;
import vn.actvn.onthionline.service.dto.QuestionAnswerDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionAnswerMapper {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionMapper questionMapper;

    public List<QuestionResult> toListEntity(List<QuestionAnswerDto> questionAnswerDtos) {
        if (questionAnswerDtos.size() == 0) return null;

        List<QuestionResult> questionResults = new ArrayList<>();
        for (QuestionAnswerDto questionAnswerDto : questionAnswerDtos) {
            Optional<Question> question = questionRepository.findById(questionAnswerDto.getId());

            QuestionResult questionResult = new QuestionResult();
            questionResult.setId(questionAnswerDto.getId());
            questionResult.setQuestion(questionAnswerDto.getQuestion());
            questionResult.setType(questionAnswerDto.getType());
            questionResult.setOption1(questionAnswerDto.getOption1());
            questionResult.setOption2(questionAnswerDto.getOption2());
            questionResult.setOption3(questionAnswerDto.getOption3());
            questionResult.setOption4(questionAnswerDto.getOption4());
            questionResult.setSuggestion(question.get().getSuggestion());
            questionResult.setAnswer(questionAnswerDto.getAnswer());
            questionResult.setCorrectAnswer(questionMapper.correctAnswerToList(question.get().getCorrectAnswer()));
            questionResults.add(questionResult);
        }
        return questionResults;
    }
}