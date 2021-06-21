package vn.actvn.onthionline.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question")
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "question")
    private String question;

    @Column(name = "type")
    private String type;

    @Column(name = "option1")
    private String option1;

    @Column(name = "option2")
    private String option2;

    @Column(name = "option3")
    private String option3;

    @Column(name = "option4")
    private String option4;

    @Column(name = "suggestion")
    private String suggestion;

    @Column(name = "correct_answer")
    private String correctAnswer;

    @Column(name = "mode")
    private String mode;      // Mode

    @Column(name = "grade")
    private String grade;      // Cấp

    @Column(name = "subject")
    private String subject;    // Môn học

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();
//    public void setCreatedDate(Date createdDate) {
//        this.createdDate = new Date();
//    }
//    public void setUpdatedDate(Date updatedDate) {
//        this.updatedDate = new Date();
//    }

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate = new Date();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "questions")
    private List<Exam> exams = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question question1 = (Question) o;
        return Objects.equals(getId(), question1.getId()) &&
                Objects.equals(getQuestion(), question1.getQuestion()) &&
                Objects.equals(getType(), question1.getType()) &&
                Objects.equals(getOption1(), question1.getOption1()) &&
                Objects.equals(getOption2(), question1.getOption2()) &&
                Objects.equals(getOption3(), question1.getOption3()) &&
                Objects.equals(getOption4(), question1.getOption4()) &&
                Objects.equals(getSuggestion(), question1.getSuggestion()) &&
                Objects.equals(getCorrectAnswer(), question1.getCorrectAnswer()) &&
                Objects.equals(getMode(), question1.getMode()) &&
                Objects.equals(getGrade(), question1.getGrade()) &&
                Objects.equals(getSubject(), question1.getSubject()) &&
                Objects.equals(getCreatedDate(), question1.getCreatedDate()) &&
                Objects.equals(getUpdatedDate(), question1.getUpdatedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getQuestion(), getType(), getOption1(), getOption2(), getOption3(), getOption4(), getSuggestion(), getCorrectAnswer(), getMode(), getGrade(), getSubject(), getCreatedDate(), getUpdatedDate());
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", type='" + type + '\'' +
                ", option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                ", option3='" + option3 + '\'' +
                ", option4='" + option4 + '\'' +
                ", suggestion='" + suggestion + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", mode='" + mode + '\'' +
                ", grade='" + grade + '\'' +
                ", subject='" + subject + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
