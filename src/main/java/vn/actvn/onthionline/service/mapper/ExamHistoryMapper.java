package vn.actvn.onthionline.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.actvn.onthionline.common.Constant;
import vn.actvn.onthionline.domain.QuestionResult;
import vn.actvn.onthionline.domain.ExamHistory;
import vn.actvn.onthionline.service.dto.ExamHistoryDto;
import vn.actvn.onthionline.service.dto.QuestionResultDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamHistoryMapper {

    public ExamHistoryDto toDto(ExamHistory history) {
        ExamHistoryDto historyResultDto = new ExamHistoryDto();
        historyResultDto.setNumAnswer(history.getNumAns());
        historyResultDto.setNumCorrectAns(history.getNumCorrectAns());
        historyResultDto.setDoTime(history.getTime());
        historyResultDto.setTotalQuestion(history.getExam().getNumQuestion());
        historyResultDto.setTotalTime(history.getExam().getTime());
        historyResultDto.setExamName(history.getExam().getName());
        historyResultDto.setExamDescription(history.getExam().getDescription());
        historyResultDto.setQuestionResult(history.getQuestionResults());
        historyResultDto.setNumOptionPicked(numOptionPickedToList(history.getNumOptionPicked()));
        return historyResultDto;
    }

    public String listNumOptionPickedToString(List<Integer> numOptionPicked) {
        String numPicked = "";
        for (int i = 0; i < numOptionPicked.size(); i++) {
            if (i != numOptionPicked.size() - 1){
                numPicked += numOptionPicked.get(i) + Constant.SEPARATOR;
            } else {
                numPicked += numOptionPicked.get(i);
            }
        }
        return numPicked;
    }

    public List<Integer> numOptionPickedToList(String numOptionPicked) {
        String []arrString = numOptionPicked.split(Constant.SEPARATOR);
        List<Integer> numPickedList = new ArrayList<>();
        for (String index : arrString) {
            numPickedList.add(Integer.parseInt(index));
        }
        return numPickedList;
    }
}
