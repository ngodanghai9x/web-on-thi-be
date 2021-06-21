package vn.actvn.onthionline.common.utils;

import vn.actvn.onthionline.common.exception.ServiceException;
import vn.actvn.onthionline.common.exception.ValidationErrorResponse;

import java.util.ArrayList;
import java.util.List;

public class ServiceUtil {
    public ServiceUtil() {
    }

    public static void generateEmptyPayloadError() throws ServiceException {
        List<ValidationErrorResponse> errors = new ArrayList();
        errors.add(new ValidationErrorResponse("payload", "validation.constraints.NotNull"));
        throw new ServiceException("failed", errors);
    }

    public static List<ValidationErrorResponse> generateErrorList(ValidationErrorResponse errorResponse) {
        List<ValidationErrorResponse> errors = new ArrayList();
        errors.add(errorResponse);
        return errors;
    }
}

