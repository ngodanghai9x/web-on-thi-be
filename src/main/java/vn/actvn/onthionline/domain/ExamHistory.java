package vn.actvn.onthionline.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.actvn.onthionline.repository.converter.QuestionResultConverter;

import javax.persistence.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_history")
public class ExamHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "time")
    private Integer time;             // Thời gian làm bài

    @Column(name = "num_correct_ans")
    private Integer numCorrectAns;    // Số câu trả lời đúng

    @Column(name = "num_option_picked")
    private String numOptionPicked;    // Số câu trả lời đúng

    @Column(name = "num_ans")
    private Integer numAns;    // Số câu trả lời

    @Column(name = "exam_answer")
    @Lob
    @Convert(converter = QuestionResultConverter.class)
    private List<QuestionResult> questionResults;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();
//    public void setCreatedDate(Date createdDate) {
//        this.createdDate = new Date();
//    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userCreated;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExamHistory)) return false;
        ExamHistory that = (ExamHistory) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getTime(), that.getTime()) &&
                Objects.equals(getNumCorrectAns(), that.getNumCorrectAns()) &&
                Objects.equals(getNumOptionPicked(), that.getNumOptionPicked()) &&
                Objects.equals(getNumAns(), that.getNumAns()) &&
                Objects.equals(getQuestionResults(), that.getQuestionResults()) &&
                Objects.equals(getCreatedDate(), that.getCreatedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTime(), getNumCorrectAns(), getNumOptionPicked(), getNumAns(), getQuestionResults(), getCreatedDate());
    }

    @Override
    public String toString() {
        return "ExamHistory{" +
                "id=" + id +
                ", time=" + time +
                ", numCorrectAns=" + numCorrectAns +
                ", numOptionPicked='" + numOptionPicked + '\'' +
                ", numAns=" + numAns +
                ", questionResults=" + questionResults +
                ", createdDate=" + createdDate +
                '}';
    }
}
