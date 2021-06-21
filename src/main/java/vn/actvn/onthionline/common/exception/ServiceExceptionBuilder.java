package vn.actvn.onthionline.common.exception;

import java.util.ArrayList;
import java.util.List;

public class ServiceExceptionBuilder {
    public ServiceExceptionBuilder() {
    }

    public static ServiceExceptionBuilder.Builder newBuilder() {
        return new ServiceExceptionBuilder.Builder();
    }

    public static class Builder {
        public List<ValidationErrorResponse> errors = new ArrayList();

        public Builder() {
        }

        public ServiceExceptionBuilder.Builder addError(ValidationErrorResponse errorResponse) {
            this.errors.add(errorResponse);
            return this;
        }

        public ServiceException build() {
            return new ServiceException("failed", this.errors);
        }
    }
}
