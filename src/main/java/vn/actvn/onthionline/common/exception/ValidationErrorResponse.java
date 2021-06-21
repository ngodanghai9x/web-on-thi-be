package vn.actvn.onthionline.common.exception;

import java.util.List;

public class ValidationErrorResponse {
    private String key;
    private String errorCode;
    private String errorMessage;
    private List<KeyValue> params;

    public ValidationErrorResponse(String key, String errorCode) {
        this.key = key;
        this.errorCode = errorCode;
        this.errorMessage = key + " " + errorCode.split("\\.")[2];
    }

    public ValidationErrorResponse(String key, String errorCode, String errorMessage) {
        this.key = key;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getKey() {
        return this.key;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public List<KeyValue> getParams() {
        return this.params;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setParams(List<KeyValue> params) {
        this.params = params;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ValidationErrorResponse)) {
            return false;
        } else {
            ValidationErrorResponse other = (ValidationErrorResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59: {
                    Object this$key = this.getKey();
                    Object other$key = other.getKey();
                    if (this$key == null) {
                        if (other$key == null) {
                            break label59;
                        }
                    } else if (this$key.equals(other$key)) {
                        break label59;
                    }

                    return false;
                }

                Object this$errorCode = this.getErrorCode();
                Object other$errorCode = other.getErrorCode();
                if (this$errorCode == null) {
                    if (other$errorCode != null) {
                        return false;
                    }
                } else if (!this$errorCode.equals(other$errorCode)) {
                    return false;
                }

                Object this$errorMessage = this.getErrorMessage();
                Object other$errorMessage = other.getErrorMessage();
                if (this$errorMessage == null) {
                    if (other$errorMessage != null) {
                        return false;
                    }
                } else if (!this$errorMessage.equals(other$errorMessage)) {
                    return false;
                }

                Object this$params = this.getParams();
                Object other$params = other.getParams();
                if (this$params == null) {
                    if (other$params != null) {
                        return false;
                    }
                } else if (!this$params.equals(other$params)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof ValidationErrorResponse;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $key = this.getKey();
        result = result * 59 + ($key == null ? 43 : $key.hashCode());
        Object $errorCode = this.getErrorCode();
        result = result * 59 + ($errorCode == null ? 43 : $errorCode.hashCode());
        Object $errorMessage = this.getErrorMessage();
        result = result * 59 + ($errorMessage == null ? 43 : $errorMessage.hashCode());
        Object $params = this.getParams();
        result = result * 59 + ($params == null ? 43 : $params.hashCode());
        return result;
    }

    public String toString() {
        return "ValidationErrorResponse(key=" + this.getKey() + ", errorCode=" + this.getErrorCode() + ", errorMessage=" + this.getErrorMessage() + ", params=" + this.getParams() + ")";
    }

    public ValidationErrorResponse(String key, String errorCode, String errorMessage, List<KeyValue> params) {
        this.key = key;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.params = params;
    }

    public ValidationErrorResponse() {
    }
}
