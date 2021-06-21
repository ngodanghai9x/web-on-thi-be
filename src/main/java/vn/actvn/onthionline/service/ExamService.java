package vn.actvn.onthionline.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.actvn.onthionline.client.dto.*;
import vn.actvn.onthionline.common.Constant;
import vn.actvn.onthionline.common.ValidationError;
import vn.actvn.onthionline.common.exception.ServiceException;
import vn.actvn.onthionline.common.exception.ServiceExceptionBuilder;
import vn.actvn.onthionline.common.exception.ValidationErrorResponse;
import vn.actvn.onthionline.common.service.EmailService;
import vn.actvn.onthionline.common.utils.OptimizedPage;
import vn.actvn.onthionline.common.utils.ServiceUtil;
import vn.actvn.onthionline.domain.*;
import vn.actvn.onthionline.repository.*;
import vn.actvn.onthionline.service.dto.*;
import vn.actvn.onthionline.service.mapper.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ExamService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamHistoryRepository examHistoryRepository;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private QuestionAnswerMapper questionAnswerMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private ExamForUserMapper examForUserMapper;

    @Autowired
    private ExamHistoryMapper examHistoryMapper;

    @Transactional(rollbackFor = Throwable.class)
    public AddExamResponse add(AddExamRequest request, String username) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getExam())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Exam", ValidationError.NotNull))
                        .build();

            // Save question
            List<Question> questions = request.getExam().getExamQuestions()
                    .stream().map(questionMapper::toEntity).collect(Collectors.toList());
            questions.stream().forEach(question -> {
                question.setCreatedDate(new Date());
                question.setUpdatedDate(new Date());
                question.setGrade(request.getExam().getGrade());
                question.setSubject(request.getExam().getSubject());
            });
            List<Question> questionsSaved = questionRepository.saveAll(questions);
            LOGGER.info("Save list question {}");

            // Save exam
            Exam exam = examMapper.toEntity(request.getExam());
            if (request.getExam().getSubject().equalsIgnoreCase("van")) {
                exam.setNumQuestion(request.getExam().getNumQuestion());
            } else {
                exam.setNumQuestion(request.getExam().getExamQuestions().size());
            }
            exam.setNumPeopleDid(0);
            exam.setActive(false);
            exam.setUserCreated(username);
            exam.setCreatedDate(new Date());
            exam.setUpdatedDate(new Date());
            exam.setQuestions(questionsSaved);
            Exam examSaved = examRepository.save(exam);
            LOGGER.info("Save exam {}", examSaved);

            AddExamResponse response = new AddExamResponse();
            response.setExam(examMapper.toDto(examSaved));

            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public UpdateExamResponse update(UpdateExamRequest request) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getExam())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Exam", ValidationError.NotNull))
                        .build();

            Optional<Exam> exam = examRepository.findById(request.getExam().getId());

            // Update list question
            List<Question> questions = request.getExam().getExamQuestions()
                    .stream().map(questionMapper::toEntity).collect(Collectors.toList());
            questions.stream().forEach(question -> {
                if (null != question.getId()) {
                    Optional<Question> examQuestion = questionRepository.findByIdAndExamId(question.getId(), exam.get().getId());
                    if (examQuestion.isPresent()) {
                        question.setCreatedDate(examQuestion.get().getCreatedDate());
                        question.setUpdatedDate(new Date());
                    }
                } else {
                    question.setCreatedDate(new Date());
                }
            });
            List<Question> questionsSaved = questionRepository.saveAll(questions);
            LOGGER.info("Update list question {}", questions);

            // Update exam
            Exam newExam = examMapper.toEntity(request.getExam());
            newExam.setNumPeopleDid(exam.get().getNumPeopleDid());
            newExam.setNumQuestion(request.getExam().getExamQuestions().size());
            newExam.setActive(exam.get().isActive());
            newExam.setUserCreated(exam.get().getUserCreated());
            newExam.setCreatedDate(exam.get().getCreatedDate());
            newExam.setUpdatedDate(new Date());
            newExam.setExamHistory(exam.get().getExamHistory());
            newExam.setComments(exam.get().getComments());
            newExam.setQuestions(questionsSaved);

            Optional<Exam> updatedExam = Optional.of(
                    Optional.of(examRepository.saveAndFlush(newExam)))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(examUpdate -> {
                        LOGGER.info("Update exam {}", examUpdate);
                        return examUpdate;
                    });


            UpdateExamResponse response = new UpdateExamResponse();
            response.setExam(examMapper.toDto(updatedExam.get()));
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    public GetExamBySubjectResponse getExamBySubject(GetExamBySubjectRequest request) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getSubject())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Subject", ValidationError.NotNull))
                        .build();

            Optional<List<Exam>> exams = examRepository.findAllExamActiveBySubjectAndGrade(request.getSubject(), request.getGrade());
            GetExamBySubjectResponse response = new GetExamBySubjectResponse();
            List<ExamInfoDto> examInfoDtos = new ArrayList<>();

            if (!exams.isPresent()) {
                response.setExam(examInfoDtos);
                return response;
            }

            examInfoDtos = exams.get().stream().map(ExamInfoMapper::toDto).collect(Collectors.toList());

            LOGGER.info("Get list exam by subject");
            response.setExam(examInfoDtos);
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    public GetAllExamResponse getAllExam(GetAllExamRequest request) throws ServiceException {
        try {
            if (null == request)
                ServiceUtil.generateEmptyPayloadError();
            if (request.getPageNumber() < 0)
                request.setPageNumber(0);
            if (request.getPageSize() < 1)
                request.setPageSize(Constant.DEFAULT_PAGE_SIZE);
            Page<Exam> exams = examRepository.findAllExam(PageRequest.of(request.getPageNumber(), request.getPageSize()), request.getInputSearch(), request.getSubject(), request.getGrade());
            Page<ExamDto> examDtos = exams.map(examMapper::toDto);
            GetAllExamResponse response = new GetAllExamResponse();
            response.setExamDtos(OptimizedPage.convert(examDtos));
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    public GetExamResponse getExam(GetExamRequest request) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getId())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Id", ValidationError.NotNull))
                        .build();

            Optional<Exam> exam = examRepository.findById(request.getId());

            if (!exam.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Id", ValidationError.Invalid))
                        .build();
            LOGGER.info("Get exam {}",exam);
            GetExamResponse response = new GetExamResponse();
            response.setExam(examMapper.toDto(exam.get()));
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    public GetExamFromUserResponse getExamForUser(GetExamFromUserRequest request, String username) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getId())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Id", ValidationError.NotNull))
                        .build();

            Optional<Exam> exam = examRepository.findExamActiveById(request.getId());

            Integer numRework = 0;
            if (null != username) {
                User user = userRepository.findByUsername(username);
                numRework = examHistoryRepository.countAllByExamIdAndUserId(request.getId(), user.getId());
            }

            if (!exam.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Id", ValidationError.Invalid))
                        .build();
            LOGGER.info("Get exam user {}", exam.get());
            GetExamFromUserResponse response = new GetExamFromUserResponse();
            response.setExam(examForUserMapper.toDto(exam.get(), numRework));
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    public DoExamResponse doExam(DoExamRequest request, String username) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getId())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Id", ValidationError.NotNull))
                        .build();
            if (null == request.getTime())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Time", ValidationError.NotNull))
                        .build();
            if (null == request.getQuestionAnswer())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Question Answer", ValidationError.NotNull))
                        .build();
            Optional<Exam> exam = examRepository.findExamActiveById(request.getId());
            User user = userRepository.findByUsername(username);
            if (!exam.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Id", ValidationError.Invalid))
                        .build();
            LOGGER.info("{} do exam {}", username, exam);
            //Check correct answer
            Integer numOfCorrect = 0;
            Integer numAnswer = 0;

            for (QuestionAnswerDto answer : request.getQuestionAnswer()) {
                if (null == answer.getAnswer() || answer.getAnswer().size() == 0) continue;
                Optional<Question> question = questionRepository.findByIdAndExamId(answer.getId(), exam.get().getId());
                if (!question.isPresent()) continue;
                if (answer.getAnswer().size() > 0) numAnswer++;
                if (answer.getType().equalsIgnoreCase(Constant.TYPE_ONE)) {
                    if (question.get().getCorrectAnswer().equalsIgnoreCase(answer.getAnswer().get(0))) numOfCorrect++;
                    else continue;
                } else {
                    List<String> correctAnswer = questionMapper.correctAnswerToList(question.get().getCorrectAnswer());
                    if (answer.getAnswer().size() != correctAnswer.size()) continue;
                    AtomicInteger count = new AtomicInteger(0);
                    correctAnswer.stream().forEach(c -> {
                        answer.getAnswer().stream().forEach(a -> {
                            if (c.equalsIgnoreCase(a)) count.getAndIncrement();
                        });
                    });
                    if (count.get() == correctAnswer.size()) numOfCorrect++;
                }
            }

            //Update exam history
            Integer countHistory = examHistoryRepository.countAllByExamIdAndUserId(request.getId(), user.getId());
            ExamHistory newExamHistory = new ExamHistory();
            newExamHistory.setTime(request.getTime());
            newExamHistory.setNumCorrectAns(numOfCorrect);
            newExamHistory.setNumAns(numAnswer);
            newExamHistory.setQuestionResults(questionAnswerMapper.toListEntity(request.getQuestionAnswer()));
            newExamHistory.setNumOptionPicked(examHistoryMapper.listNumOptionPickedToString(request.getNumOptionPicked()));
            newExamHistory.setCreatedDate(new Date());
            newExamHistory.setUserCreated(user);
            newExamHistory.setExam(exam.get());
            ExamHistory savedHistory = examHistoryRepository.saveAndFlush(newExamHistory);
            LOGGER.info("New history {}", newExamHistory);

            //Update exam
            if (countHistory == 0) {
                exam.get().setNumPeopleDid(exam.get().getNumPeopleDid()+1);
                examRepository.saveAndFlush(exam.get());
            }

            DoExamResponse response = new DoExamResponse();
            response.setResult(examHistoryMapper.toDto(savedHistory));
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    public ChangeActiveExamResponse changeActive(ChangeActiveExamRequest request) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getId())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Id", ValidationError.NotNull))
                        .build();
            if (null == request.getIsActive())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("IsActive", ValidationError.NotNull))
                        .build();

            Optional<Exam> exam = examRepository.findById(request.getId());
            if (!exam.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Id", ValidationError.Invalid))
                        .build();
            exam.get().setActive(request.getIsActive());

//            if (request.getIsActive() == true && exam.get().getNumPeopleDid() == 0) {
//                List<User> users = userRepository.findAll();
//                users.stream().forEach(user -> {
//                    EmailDto emailDTO = new EmailDto();
//                    emailDTO.setSubject("WebOnThi - Thi trắc nghiệm trực tuyến miễn phí 2020");
//                    emailDTO.setBody("<table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" bgcolor=\"#f0f0f0\">\n" +
//                            "   <tr>\n" +
//                            "      <td style=\"padding: 30px 30px 20px 30px;\">\n" +
//                            "         <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" bgcolor=\"#ffffff\" style=\"max-width: 650px; margin: auto;\">\n" +
//                            "            <tr>\n" +
//                            "               <td colspan=\"2\" align=\"center\" style=\"background-color: #f3f8fd; padding: 40px;\">\n" +
//                            "                  <a href=\"http://webonthi.com\" target=\"_blank\"><img src=\"https://hoctot.com/images/logo.png\" border=\"0\" /></a>\n" +
//                            "               </td>\n" +
//                            "            </tr>\n" +
//                            "            <tr>\n" +
//                            "               <td colspan=\"2\" align=\"center\" style=\"padding: 50px 50px 0px 50px;\">\n" +
//                            "                  <h1 style=\"padding-right: 0em; margin: 0; line-height: 40px; font-weight:300; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #666; text-align: left; padding-bottom: 1em;\">\n" +
//                                                    (exam.get().getGrade().equalsIgnoreCase("highSchool") ? Constant.HIGH_SCHOOL : Constant.COLLEGE) +
//                            "                  </h1>\n" +
//                            "               </td>\n" +
//                            "            </tr>\n" +
//                            "            <tr>\n" +
//                            "               <td style=\"text-align: left; padding: 0px 50px;\" valign=\"top\">\n" +
//                            "                  <p style=\"font-size: 18px; margin: 0; line-height: 24px; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #666; text-align: left; padding-bottom: 3%;\">\n" +
//                            "                        Chào bạn " + user.getFullname() + ", \n" +
//                            "                  </p>\n" +
//                            "                  <p style=\"font-size: 18px; margin: 0; line-height: 24px; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #666; text-align: left; padding-bottom: 3%;\">\n" +
//                            "                    Chúng tớ vừa upload <a href=\" " + generateLinkExam(exam.get()) +  "\" style=\"background-color: #F0F0F0; font-size: 20px; color: #3DA9F5\" target=\"_blank\">" + exam.get().getName() + "</a>.\n" +
//                            "                    <br>Còn chờ đợi gì nữa, vào làm nhanh thôi nào. Chúc bạn làm bài thi thật tốt nhé!<br>\n" +
//                            "                    <img style=\"width: 500px; height: 300px; align: center;\" src=\"https://unia.vn/wp-content/uploads/2020/08/loi-chuc-thi-tot-tieng-anh-800x500.jpg\">\n" +
//                            "                  </p>\n" +
//                            "               </td>\n" +
//                            "            </tr>\n" +
//                            "            <tr>\n" +
//                            "               <td style=\"text-align: left; padding: 30px 50px 50px 50px\" valign=\"top\">\n" +
//                            "                  <p style=\"font-size: 18px; margin: 0; line-height: 24px; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #505050; text-align: left;\">\n" +
//                            "                     Thanks,<br/>WebOnThi Team\"\n" +
//                            "                  </p>\n" +
//                            "               </td>\n" +
//                            "            </tr>\n" +
//                            "            <tr>\n" +
//                            "               <td colspan=\"2\" align=\"center\" style=\"padding: 20px 40px 40px 40px;\" bgcolor=\"#f0f0f0\">\n" +
//                            "                  <p style=\"font-size: 12px; margin: 0; line-height: 24px; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #777;\">&copy; 2020\n" +
//                            "                     <a href=\"http://webonthi.com/\" target=\"_blank\" style=\"color: #777; text-decoration: none\">Webonthi.com</a>\n" +
//                            "                     <br>\n" +
//                            "                     141 Chiến Thắng, Xã Tân Triều, H. Thanh Trì, Tp. Hà Nội\n" +
//                            "                  </p>\n" +
//                            "               </td>\n" +
//                            "            </tr>\n" +
//                            "         </table>\n" +
//                            "      </td>\n" +
//                            "   </tr>\n" +
//                            "</table>");
//
//                    List<String> recipients = new ArrayList<>();
//                    recipients.add(user.getEmail());
//                    emailDTO.setRecipients(recipients);
//                    emailDTO.setIsHtml(true);
//
//                    // send generated e-mail
//                    try {
//                        emailService.sendMessage(emailDTO);
//                    } catch (MessagingException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }

            ChangeActiveExamResponse response = new ChangeActiveExamResponse();
            Optional<Exam> updated = Optional.of(
                    Optional.of(examRepository.saveAndFlush(exam.get())))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(examUpdate -> {
                        LOGGER.info("Change active exam {}", examUpdate);
                        response.setSuccess(true);
                        return examUpdate;
                    });

            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    private String generateLinkExam(Exam exam) {
        String url = "http://localhost:3000";
        url += exam.getGrade().equalsIgnoreCase("highSchool") ? "/lop-10/" : "/dai-hoc/";
        url += exam.getSubject() + "/ket-qua/" + exam.getId();
        return url;
    }

    public GetRankingInExamResponse rankingByExam(GetRankingInExamRequest request) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getExamId())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Exam Id", ValidationError.NotNull))
                        .build();

            Optional<Exam> exam = examRepository.findById(request.getExamId());
            if (!exam.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Exam Id", ValidationError.Invalid))
                        .build();

            int minCorrectAnswer = (int) (exam.get().getNumQuestion() * Constant.MIN_RANK_CONSTANT);
            List<ExamHistory> examHistories = examHistoryRepository.findListHigherCorrectAnswerInExam(request.getExamId(), minCorrectAnswer);
            List<RankingDto> rankingDtos = new ArrayList<>();
            examHistories.stream().forEach(history -> {
                RankingDto rankingDto = new RankingDto();
                rankingDto.setFullName(history.getUserCreated().getFullname());
                rankingDto.setNumCorrectAns(history.getNumCorrectAns());
                rankingDto.setTotalQuestion(history.getExam().getNumQuestion());
                try {
                    if (null != history.getUserCreated().getAvatar())
                        rankingDto.setAvatarBase64(imageService.getFile(history.getUserCreated().getAvatar()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rankingDtos.add(rankingDto);
            });
            LOGGER.info("Get list rank {} by examid {}", rankingDtos, request.getExamId());

            GetRankingInExamResponse response = new GetRankingInExamResponse();
            response.setRanking(rankingDtos);
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public DeleteExamResponse deleteExam(DeleteExamRequest request) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();

            List<DeleteListDto> deleteExamDtos = new ArrayList<>();
            for (Integer id : request.getExamIds()) {
                DeleteListDto deleteExamDto = new DeleteListDto();
                deleteExamDto.setId(id);
                Optional<Exam> exam = examRepository.findById(id);
                if (!exam.isPresent()) {
                    deleteExamDto.setSuccess(false);
                    deleteExamDto.setError("Exam Id invalid");
                    deleteExamDtos.add(deleteExamDto);
                    continue;
                }
                if (exam.get().getExamHistory().size() > 0) {
                    deleteExamDto.setSuccess(false);
                    deleteExamDto.setError("Exam can't be delete");
                    deleteExamDtos.add(deleteExamDto);
                    continue;
                }
                exam.get().getQuestions().removeAll(exam.get().getQuestions());
                Exam newExam = examRepository.saveAndFlush(exam.get());
                commentRepository.deleteAllByExamId(exam.get().getId());
                examRepository.delete(newExam);
                deleteExamDto.setSuccess(true);
                deleteExamDto.setError(null);
                deleteExamDtos.add(deleteExamDto);
            }

            LOGGER.info("Delete list exam", deleteExamDtos);
            DeleteExamResponse response = new DeleteExamResponse();
            response.setDeleteExamDtos(deleteExamDtos);
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    public GetCompletedExamResponse getCompletedExam(String username) {
        User user = userRepository.findByUsername(username);
        List<CompletedExamDto> completedExamDtos = new ArrayList<>();
        List<Integer> examId = examHistoryRepository.findAllExamIdByUserId(user.getId());
        examId.stream().forEach(id -> {
            Optional<ExamHistory> examHistory = examHistoryRepository.findLastHistory(id, user.getId());
            CompletedExamDto completedExamDto = new CompletedExamDto();
            completedExamDto.setId(examHistory.get().getExam().getId());
            completedExamDto.setName(examHistory.get().getExam().getName());
            completedExamDtos.add(completedExamDto);
        });

        LOGGER.info("Get completed exam", completedExamDtos);
        GetCompletedExamResponse response = new GetCompletedExamResponse();
        response.setCompletedExamDtos(completedExamDtos);
        return response;
    }

    public GetLastHistoryResponse getLastHistory(GetLastHistoryRequest request, String username) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getExamId())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Exam Id", ValidationError.NotNull))
                        .build();

            User user = userRepository.findByUsername(username);
            Optional<ExamHistory> examHistory = examHistoryRepository.findLastHistory(request.getExamId(), user.getId());
            LOGGER.info("Get last history exam {}", examHistory);
            GetLastHistoryResponse response = new GetLastHistoryResponse();
            if (!examHistory.isPresent())
                response.setLastHistory(null);
            else
                response.setLastHistory(examHistoryMapper.toDto(examHistory.get()));

            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    public GetListHistoryResponse getListHistory(GetListHistoryRequest request, String username) throws ServiceException {
        try {
            if (null == request)
                ServiceUtil.generateEmptyPayloadError();
            if (request.getPageNumber() < 0)
                request.setPageNumber(0);
            if (request.getPageSize() < 1)
                request.setPageSize(Constant.DEFAULT_PAGE_SIZE);
            User user = userRepository.findByUsername(username);
            Page<ExamHistory> examHistories = examHistoryRepository.findAllByUserId(PageRequest.of(request.getPageNumber(), request.getPageSize()), user.getId());
            Page<ExamHistoryDto> examHistoryDtos = examHistories.map(examHistoryMapper::toDto);
            GetListHistoryResponse response = new GetListHistoryResponse();
            response.setExamHistoryDtos(OptimizedPage.convert(examHistoryDtos));
            LOGGER.info("Get list history", examHistories);

            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }
}
