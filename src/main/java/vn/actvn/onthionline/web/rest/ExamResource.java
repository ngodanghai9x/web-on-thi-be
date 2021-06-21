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
import vn.actvn.onthionline.service.ExamService;

import java.security.Principal;

@RestController
@RequestMapping("/api/exam")
public class ExamResource {
    private Logger LOGGER = LoggerFactory.getLogger(ExamResource.class);

    @Autowired
    private ExamService examService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<BaseDataResponse<AddExamResponse>> addExam(@RequestBody BaseDataRequest<AddExamRequest> request, Principal currentUser) {
        try {
            AddExamResponse response = examService.add(request.getBody(), currentUser.getName());
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
    @PostMapping("/update")
    public ResponseEntity<BaseDataResponse<UpdateExamResponse>> updateExam(@RequestBody BaseDataRequest<UpdateExamRequest> request) {
        try {
            UpdateExamResponse response = examService.update(request.getBody());
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
    public ResponseEntity<BaseDataResponse<GetExamResponse>> getExam(@RequestBody BaseDataRequest<GetExamRequest> request) {
        try {
            GetExamResponse response = examService.getExam(request.getBody());
            return ResponseUtil.wrap(response);
        } catch (ServiceException e) {
            LOGGER.error(this.getClass().getName(), e);
            return ResponseUtil.generateErrorResponse(e);
        } catch (Exception e) {
            LOGGER.error(this.getClass().getName(), e);
            return ResponseUtil.generateErrorResponse(e);
        }
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PostMapping("/find-by-code")
//    public ResponseEntity<BaseDataResponse<AddExamResponse>> findByCode(@RequestBody BaseDataRequest<AddExamRequest> request, Principal currentUser) {
//        try {
//            AddExamResponse response = examService.add(request.getBody(), currentUser.getName());
//            return ResponseUtil.wrap(response);
//        } catch (ServiceException e) {
//            LOGGER.error(this.getClass().getName(), e);
//            return ResponseUtil.generateErrorResponse(e);
//        } catch (Exception e) {
//            LOGGER.error(this.getClass().getName(), e);
//            return ResponseUtil.generateErrorResponse(e);
//        }
//    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/get-all")
    public ResponseEntity<BaseDataResponse<GetAllExamResponse>> getAllExam(@RequestBody BaseDataRequest<GetAllExamRequest> request) {
        try {
            GetAllExamResponse response = examService.getAllExam(request.getBody());
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
    @PostMapping("/change-active")
    public ResponseEntity<BaseDataResponse<ChangeActiveExamResponse>> changeActiveExam(@RequestBody BaseDataRequest<ChangeActiveExamRequest> request) {
        try {
            ChangeActiveExamResponse response = examService.changeActive(request.getBody());
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
    public ResponseEntity<BaseDataResponse<DeleteExamResponse>> deleteExam(@RequestBody BaseDataRequest<DeleteExamRequest> request) {
        try {
            DeleteExamResponse response = examService.deleteExam(request.getBody());
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
