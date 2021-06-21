package vn.actvn.onthionline.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionResult {
    private Integer id;
    private String question;
    private String type;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String suggestion;
    private List<String> answer;
    private List<String> correctAnswer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionResult)) return false;
        QuestionResult result = (QuestionResult) o;
        return Objects.equals(getId(), result.getId()) &&
                Objects.equals(getQuestion(), result.getQuestion()) &&
                Objects.equals(getType(), result.getType()) &&
                Objects.equals(getOption1(), result.getOption1()) &&
                Objects.equals(getOption2(), result.getOption2()) &&
                Objects.equals(getOption3(), result.getOption3()) &&
                Objects.equals(getOption4(), result.getOption4()) &&
                Objects.equals(getSuggestion(), result.getSuggestion()) &&
                Objects.equals(getAnswer(), result.getAnswer()) &&
                Objects.equals(getCorrectAnswer(), result.getCorrectAnswer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getQuestion(), getType(), getOption1(), getOption2(), getOption3(), getOption4(), getSuggestion(), getAnswer(), getCorrectAnswer());
    }

    @Override
    public String toString() {
        return "QuestionResult{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", type='" + type + '\'' +
                ", option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                ", option3='" + option3 + '\'' +
                ", option4='" + option4 + '\'' +
                ", suggestion='" + suggestion + '\'' +
                ", answer=" + answer +
                ", correctAnswer=" + correctAnswer +
                '}';
    }
}