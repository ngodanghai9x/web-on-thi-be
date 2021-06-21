package vn.actvn.onthionline.common.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import vn.actvn.onthionline.client.dto.BaseDataResponse;
import vn.actvn.onthionline.common.exception.KeyValue;
import vn.actvn.onthionline.common.exception.ServiceException;
import vn.actvn.onthionline.common.exception.ValidationErrorResponse;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ResponseUtil {
    static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
        return wrapOrNotFound(maybeResponse, (HttpHeaders)null);
    }

    static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
        return (ResponseEntity)maybeResponse.map((response) -> {
            return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(header)).body(response);
        }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    static ResponseEntity generateErrorResponse() {
        return generateErrorResponse("failed");
    }

    static ResponseEntity generateErrorResponse(String message) {
        BaseDataResponse response = new BaseDataResponse();
        response.setResponseCode("400");
        response.setResponseMessage(message);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    static ResponseEntity generateErrorResponse(ServiceException exception) {
        BaseDataResponse response = new BaseDataResponse();
        response.setResponseCode("400");
        response.setResponseMessage(exception.getMessage());
        response.setResponseEntityMessages(exception.getErrors());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    static ResponseEntity generateErrorResponse(Exception exception) {
        Throwable t = exception.getCause();
        if (exception instanceof ConstraintViolationException) {
            t = exception;
        } else {
            while(t != null && !(t instanceof ConstraintViolationException)) {
                t = ((Throwable)t).getCause();
            }
        }

        if (t instanceof ConstraintViolationException) {
            return generateErrorResponse((ConstraintViolationException)t);
        } else {
            return t instanceof ServiceException ? generateErrorResponse((ServiceException)t) : generateErrorResponse();
        }
    }

    static ResponseEntity generateErrorResponse(ConstraintViolationException exception) {
        return generateErrorResponse(exception, (String)null);
    }

    static ResponseEntity generateErrorResponse(ConstraintViolationException exception, String messageKeyPrefix) {
        BaseDataResponse response = new BaseDataResponse();
        response.setResponseCode("400");
        response.setResponseMessage("failed");
        List<ValidationErrorResponse> errors = new ArrayList();

        ArrayList errorMessageParams;
        ConstraintViolation cv;
        String errorMessage;
        for(Iterator var5 = exception.getConstraintViolations().iterator(); var5.hasNext(); errors.add(new ValidationErrorResponse(cv.getPropertyPath().toString(), cv.getMessageTemplate(), errorMessage, errorMessageParams))) {
            cv = (ConstraintViolation)var5.next();
            errorMessageParams = null;
            errorMessage = cv.getMessageTemplate();
            if (cv.getConstraintDescriptor() != null && cv.getConstraintDescriptor().getAttributes() != null && !StringUtils.isEmpty(errorMessage)) {
                Map<String, Object> constraints = cv.getConstraintDescriptor().getAttributes();
                Pattern pattern = Pattern.compile("\\{(.*?)\\}");
                Matcher match = pattern.matcher(errorMessage);

                while(match.find()) {
                    if (errorMessageParams == null) {
                        errorMessageParams = new ArrayList();
                    }

                    String messageParam = match.group().replace("{", "").replace("}", "");
                    String constraintValue = constraints.get(messageParam) != null ? constraints.get(messageParam).toString() : null;
                    if (!StringUtils.isEmpty(constraintValue)) {
                        errorMessageParams.add(new KeyValue(messageParam, constraintValue));
                    }
                }
            }
        }

        response.setResponseEntityMessages(errors);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    static ResponseEntity wrap(Object object) {
        return wrap((HttpHeaders)null, object);
    }

    static ResponseEntity wrap(HttpHeaders headers, Object object) {
        BaseDataResponse response = new BaseDataResponse();
        response.setResponseCode("200");
        response.setResponseMessage("successful");
        response.setBody(object);
        return new ResponseEntity(response, headers, HttpStatus.OK);
    }

    static ResponseEntity unwrap(HttpHeaders headers, Object object) {
        return new ResponseEntity(object, headers, HttpStatus.OK);
    }
}
