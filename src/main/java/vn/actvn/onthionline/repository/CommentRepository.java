package vn.actvn.onthionline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.actvn.onthionline.domain.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "from Comment c where c.exam.id = :examId")
    List<Comment> findAllByExamId(@Param("examId") Integer examId);

    void deleteAllByExamId(Integer examId);
}
