package vn.actvn.onthionline.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content")
    private String content;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "users_liked")
    private String userLiked;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userCreated;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(getId(), comment.getId()) &&
                Objects.equals(getContent(), comment.getContent()) &&
                Objects.equals(getParentId(), comment.getParentId()) &&
                Objects.equals(getUserLiked(), comment.getUserLiked()) &&
                Objects.equals(getCreatedDate(), comment.getCreatedDate()) &&
                Objects.equals(getUpdatedDate(), comment.getUpdatedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getContent(), getParentId(), getUserLiked(), getCreatedDate(), getUpdatedDate());
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", parentId=" + parentId +
                ", userLiked='" + userLiked + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
