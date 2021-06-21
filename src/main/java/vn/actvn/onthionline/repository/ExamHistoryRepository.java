package vn.actvn.onthionline.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.actvn.onthionline.domain.ExamHistory;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamHistoryRepository extends JpaRepository<ExamHistory, Integer> {

    @Query(value = "from ExamHistory h where h.id = :id and h.userCreated.id = :userId")
    Optional<ExamHistory> findByIdAndUserId(@Param("id") Integer id, @Param("userId") Integer userId);

    @Query(value = "select count(h) from ExamHistory h where h.exam.id = :examId and h.userCreated.id = :userId")
    Integer countAllByExamIdAndUserId(@Param("examId") Integer examId, @Param("userId") Integer userId);

    @Query(value = "select distinct h.exam.id from ExamHistory h where h.userCreated.id = :userId")
    List<Integer> findAllExamIdByUserId(@Param("userId") Integer userId);

    @Query(value = "from ExamHistory h where h.id = (select max(h.id) from ExamHistory h where h.exam.id = :examId and h.userCreated.id = :userId)")
    Optional<ExamHistory> findLastHistory(@Param("examId") Integer examId, @Param("userId") Integer userId);

    @Query(value = "select * from exam_history o where o.exam_id = :id\n" +
            "and o.num_correct_ans = (select max(b.num_correct_ans) from exam_history b where b.num_correct_ans > :min and b.user_id = o.user_id and b.exam_id = o.exam_id)\n" +
            "and o.id = (select min(a.id) from exam_history a where a.user_id = o.user_id and a.num_correct_ans = o.num_correct_ans and a.exam_id = o.exam_id) \n" +
            "order by num_correct_ans desc limit 20",
            nativeQuery = true)
    List<ExamHistory> findListHigherCorrectAnswerInExam(@Param("id") Integer examId, @Param("min") Integer minCorrectAnswer);

    @Query(value = "from ExamHistory h where h.userCreated.id = :userId order by h.createdDate desc")
    Page<ExamHistory> findAllByUserId(Pageable pageable, @Param("userId") Integer userId);

}
