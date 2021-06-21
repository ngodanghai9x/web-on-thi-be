package vn.actvn.onthionline.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "exam")
public class Exam implements Serializable {
    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "grade")
    private String grade;      // Cấp

    @Column(name = "subject")
    private String subject;    // Môn học

    @Column(name = "num_question")
    private Integer numQuestion;   // Số câu hỏi

    @Column(name = "num_people_did")
    private Integer numPeopleDid;   // Số người làm bài

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "time")
    private Integer time;   // Thời gian làm bài

    @Column(name = "mixed_question")
    private Boolean mixedQuestion;

    @Column(name = "user_created")
    private String userCreated;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "exam_question",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private List<Question> questions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "exam")
    private Set<ExamHistory> examHistory = new HashSet<>();;

    @OneToMany(mappedBy = "exam")
    private Set<Comment> comments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exam)) return false;
        Exam exam = (Exam) o;
        return isActive() == exam.isActive() &&
                Objects.equals(getId(), exam.getId()) &&
                Objects.equals(getName(), exam.getName()) &&
                Objects.equals(getCode(), exam.getCode()) &&
                Objects.equals(getGrade(), exam.getGrade()) &&
                Objects.equals(getSubject(), exam.getSubject()) &&
                Objects.equals(getNumQuestion(), exam.getNumQuestion()) &&
                Objects.equals(getNumPeopleDid(), exam.getNumPeopleDid()) &&
                Objects.equals(getDescription(), exam.getDescription()) &&
                Objects.equals(getTime(), exam.getTime()) &&
                Objects.equals(getMixedQuestion(), exam.getMixedQuestion()) &&
                Objects.equals(getUserCreated(), exam.getUserCreated()) &&
                Objects.equals(getCreatedDate(), exam.getCreatedDate()) &&
                Objects.equals(getUpdatedDate(), exam.getUpdatedDate()) &&
                Objects.equals(getQuestions(), exam.getQuestions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCode(), getGrade(), getSubject(), getNumQuestion(), getNumPeopleDid(), getDescription(), isActive(), getTime(), getMixedQuestion(), getUserCreated(), getCreatedDate(), getUpdatedDate(), getQuestions());
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", grade='" + grade + '\'' +
                ", subject='" + subject + '\'' +
                ", numQuestion=" + numQuestion +
                ", numPeopleDid=" + numPeopleDid +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", time=" + time +
                ", mixedQuestion=" + mixedQuestion +
                ", userCreated='" + userCreated + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", questions=" + questions +
                '}';
    }
}
