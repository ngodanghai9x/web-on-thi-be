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
import vn.actvn.onthionline.common.utils.OptimizedPage;
import vn.actvn.onthionline.common.utils.ServiceUtil;
import vn.actvn.onthionline.domain.Exam;
import vn.actvn.onthionline.domain.Question;
import vn.actvn.onthionline.repository.ExamRepository;
import vn.actvn.onthionline.repository.QuestionRepository;
import vn.actvn.onthionline.service.dto.DeleteListDto;
import vn.actvn.onthionline.service.dto.QuestionDto;
import vn.actvn.onthionline.service.mapper.QuestionMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final Logger LOGGER = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private ExamRepository examRepository;

    public SaveQuestionResponse save(SaveQuestionRequest request) throws ServiceException {
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getQuestion())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Question", ValidationError.NotNull))
                        .build();

            Question newQuestion = questionMapper.toEntity(request.getQuestion());
            if (null != request.getQuestion().getId()) {
                Optional<Question> question = questionRepository.findById(request.getQuestion().getId());
                if (question.isPresent()) {
                    newQuestion.setCreatedDate(question.get().getCreatedDate());
                    newQuestion.setUpdatedDate(new Date());
                } else {
                    newQuestion.setCreatedDate(new Date());
                    newQuestion.setUpdatedDate(new Date());
                }
            } else {
                newQuestion.setCreatedDate(new Date());
                newQuestion.setUpdatedDate(new Date());
            }

            Optional<Question> savedQuestion = Optional.of(
                    Optional.of(questionRepository.saveAndFlush(newQuestion)))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(newQuestionAdded -> {
                        LOGGER.info("Save question {}", newQuestionAdded);
                        return newQuestionAdded;
                    });
            SaveQuestionResponse response = new SaveQuestionResponse();
            response.setQuestion(questionMapper.toDto(savedQuestion.get()));
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public DeleteQuestionResponse delete(DeleteQuestionRequest request) throws ServiceException{
        try {
            if (null == request) ServiceUtil.generateEmptyPayloadError();
            if (null == request.getQuestionIds() || request.getQuestionIds().size() == 0)
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Question", ValidationError.NotNull))
                        .build();

            List<DeleteListDto> deleteListDtos = new ArrayList<>();
            for (Integer id : request.getQuestionIds()) {
                DeleteListDto deleteQuestionDto = new DeleteListDto();
                deleteQuestionDto.setId(id);
                Optional<Question> question = questionRepository.findById(id);
                if (!question.isPresent()) {
                    deleteQuestionDto.setSuccess(false);
                    deleteQuestionDto.setError("Question Id invalid");
                    deleteListDtos.add(deleteQuestionDto);
                    continue;
                }
                question.get().getExams().forEach(exam -> {
                    exam.setNumQuestion(exam.getNumQuestion() - 1);
                    exam.getQuestions().remove(question.get());
                    examRepository.saveAndFlush(exam);
                });
                questionRepository.delete(question.get());
                deleteQuestionDto.setSuccess(true);
                deleteQuestionDto.setError(null);
                deleteListDtos.add(deleteQuestionDto);
            }
            return new DeleteQuestionResponse(deleteListDtos);
        } catch (ServiceException e) {
            throw e;
        }
    }

    public GetListQuestionResponse getAll(GetListQuestionRequest request) throws ServiceException{
        try {
            if (null == request)
                ServiceUtil.generateEmptyPayloadError();
            if (request.getPageNumber() < 0)
                request.setPageNumber(0);
            if (request.getPageSize() < 1)
                request.setPageSize(Constant.DEFAULT_PAGE_SIZE);
            Page<Question> questions = questionRepository.findAllQuestion(PageRequest.of(request.getPageNumber(), request.getPageSize()),
                    request.getQuestion(), request.getMode(), request.getSubject(), request.getGrade(), request.getType());
            Page<QuestionDto> questionDtos = questions.map(questionMapper::toDto);
            GetListQuestionResponse response = new GetListQuestionResponse();
            response.setQuestions(OptimizedPage.convert(questionDtos));
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    public GetQuestionResponse get(GetQuestionRequest request) throws ServiceException{
        try {
            if (null == request)
                ServiceUtil.generateEmptyPayloadError();
            if (null == request.getId())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Id", ValidationError.NotNull))
                        .build();

            Optional<Question> question = questionRepository.findById(request.getId());
            if (!question.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Id", ValidationError.Invalid))
                        .build();


            GetQuestionResponse response = new GetQuestionResponse();
            response.setQuestion(questionMapper.toDto(question.get()));
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }

    public AddQuestionToExamResponse addToExam(AddQuestionToExamRequest request) throws ServiceException{
        try {
            if (null == request)
                ServiceUtil.generateEmptyPayloadError();
            if (null == request.getExamId())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Exam Id", ValidationError.NotNull))
                        .build();
            if (null == request.getQuestionIds() || request.getQuestionIds().size() == 0)
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Question Ids", ValidationError.NotNull))
                        .build();
            Optional<Exam> oldExam = examRepository.findById(request.getExamId());
            if (!oldExam.isPresent())
                throw ServiceExceptionBuilder.newBuilder()
                        .addError(new ValidationErrorResponse("Exam Id", ValidationError.Invalid))
                        .build();

            AtomicInteger addCount = new AtomicInteger(0);

            request.getQuestionIds().forEach(id -> {
                Optional<Question> question = questionRepository.findById(id);
                if (question.isPresent()) {
                    List<Question> find = oldExam.get().getQuestions().stream().filter(question1 -> question1.getId() == id).collect(Collectors.toList());
                    if (find.size() == 0) {
                        oldExam.get().getQuestions().add(question.get());
                        addCount.getAndIncrement();
                    }
                }
            });
            oldExam.get().setNumQuestion(oldExam.get().getNumQuestion() + addCount.get());
            Optional<Exam> newExam = Optional.of(
                    Optional.of(examRepository.saveAndFlush(oldExam.get())))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(examSaved -> {
                        LOGGER.info("Update list question to exam {}", examSaved);
                        return examSaved;
                    });

            AddQuestionToExamResponse response = new AddQuestionToExamResponse();
            if (newExam.isPresent()) response.setSuccess(true);
            else response.setSuccess(false);
            return response;
        } catch (ServiceException e) {
            throw e;
        }
    }
}
