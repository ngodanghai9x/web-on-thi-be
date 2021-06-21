package vn.actvn.onthionline.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.actvn.onthionline.client.dto.*;
import vn.actvn.onthionline.common.exception.ServiceException;
import vn.actvn.onthionline.common.utils.ResponseUtil;
import vn.actvn.onthionline.service.QuestionService;

@RestController
@RequestMapping("/api/question")
public class QuestionResource {
    private Logger LOGGER = LoggerFactory.getLogger(QuestionResource.class);

    @Autowired
    private QuestionService questionService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<BaseDataResponse<SaveQuestionResponse>> saveQuestion(@RequestBody BaseDataRequest<SaveQuestionRequest> request) {
        try {
            SaveQuestionResponse response = questionService.save(request.getBody());
            return ResponseUtil.wrap(response);
        } catch (ServiceException e) {
            LOGGER.error(this.getClass().getName(), e);
            return ResponseUtil.generateErrorResponse(e);
        } catch (Exception e) {
            LOGGER.error(this.getClass().getName(), e);
            return ResponseUtil.generateErrorResponse(e);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/get-all")
    public ResponseEntity<BaseDataResponse<GetListQuestionResponse>> getList(@RequestBody BaseDataRequest<GetListQuestionRequest> request) {
        try {
            GetListQuestionResponse response = questionService.getAll(request.getBody());
            return ResponseUtil.wrap(response);
        } catch (ServiceException e) {
            LOGGER.error(this.getClass().getName(), e);
            return ResponseUtil.generateErrorResponse(e);
        } catch (Exception e) {
            LOGGER.error(this.getClass().getName(), e);
            return ResponseUtil.generateErrorResponse(e);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/get")
    public ResponseEntity<BaseDataResponse<GetQuestionResponse>> get(@RequestBody BaseDataRequest<GetQuestionRequest> request) {
        try {
            GetQuestionResponse response = questionService.get(request.getBody());
            return ResponseUtil.wrap(response);
        } catch (ServiceException e) {
            LOGGER.error(this.getClass().getName(), e);
            return ResponseUtil.generateErrorResponse(e);
        } catch (Exception e) {
            LOGGER.error(this.getClass().getName(), e);
            return ResponseUtil.generateErrorResponse(e);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-to-exam")
    public ResponseEntity<BaseDataResponse<AddQuestionToExamResponse>> addToExam(@RequestBody BaseDataRequest<AddQuestionToExamRequest> request) {
        try {
            AddQuestionToExamResponse response = questionService.addToExam(request.getBody());
            return ResponseUtil.wrap(response);
        } catch (ServiceException e) {
            LOGGER.error(this.getClass().getName(), e);
            return ResponseUtil.generateErrorResponse(e);
        } catch (Exception e) {
            LOGGER.error(this.getClass().getName(), e);
            return ResponseUtil.generateErrorResponse(e);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public ResponseEntity<BaseDataResponse<DeleteQuestionResponse>> deleteQuestion(@RequestBody BaseDataRequest<DeleteQuestionRequest> request) {
        try {
            DeleteQuestionResponse response = questionService.delete(request.getBody());
            return ResponseUtil.wrap(response);
        } catch (ServiceException e) {
            LOGGER.error(this.getClass().getName(), e);
            return ResponseUtil.generateErrorResponse(e);
        } catch (Exception e) {
            LOGGER.error(this.getClass().getName(), e);
            return ResponseUtil.generateErrorResponse(e);
        }
    }
}
