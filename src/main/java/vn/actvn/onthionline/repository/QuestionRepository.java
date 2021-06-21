package vn.actvn.onthionline.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.actvn.onthionline.domain.Question;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer>  {

    @Query(value = "select * from question q where q.id = (select question_id from exam_question e where e.exam_id = :examId and e.question_id = :id)", nativeQuery = true)
    Optional<Question> findByIdAndExamId(@Param("id") Integer id, @Param("examId") Integer examId);

    @Query(value = "from Question q where (:question is null or q.question like %:question%) \n" +
                    "and (:mode is null or q.mode like %:mode%) \n" +
                    "and (:subject is null or q.subject like %:subject%) \n" +
                    "and (:grade is null or q.grade like %:grade%) \n" +
                    "and (:type is null or q.type like %:type%) \n" +
                    "and q.subject not like 'van' order by q.updatedDate desc")
    Page<Question> findAllQuestion(Pageable pageable,
                                   @Param("question") String question,
                                   @Param("mode") String mode,
                                   @Param("subject") String subject,
                                   @Param("grade") String grade,
                                   @Param("type") String type);
}
