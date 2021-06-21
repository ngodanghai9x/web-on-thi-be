package vn.actvn.onthionline.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import vn.actvn.onthionline.common.exception.ValidationErrorResponse;

import java.util.List;

public class BaseDataResponse<T> {
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseMessage")
    private String responseMessage;
    @JsonProperty("responseEntityMessages")
    private List<ValidationErrorResponse> responseEntityMessages;
    @JsonProperty("body")
    private T body;

    public String getResponseCode() {
        return this.responseCode;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public List<ValidationErrorResponse> getResponseEntityMessages() {
        return this.responseEntityMessages;
    }

    public T getBody() {
        return this.body;
    }

    @JsonProperty("responseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("responseMessage")
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @JsonProperty("responseEntityMessages")
    public void setResponseEntityMessages(List<ValidationErrorResponse> responseEntityMessages) {
        this.responseEntityMessages = responseEntityMessages;
    }

    @JsonProperty("body")
    public void setBody(T body) {
        this.body = body;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseDataResponse)) {
            return false;
        } else {
            BaseDataResponse<?> other = (BaseDataResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59: {
                    Object this$responseCode = this.getResponseCode();
                    Object other$responseCode = other.getResponseCode();
                    if (this$responseCode == null) {
                        if (other$responseCode == null) {
                            break label59;
                        }
                    } else if (this$responseCode.equals(other$responseCode)) {
                        break label59;
                    }

                    return false;
                }

                Object this$responseMessage = this.getResponseMessage();
                Object other$responseMessage = other.getResponseMessage();
                if (this$responseMessage == null) {
                    if (other$responseMessage != null) {
                        return false;
                    }
                } else if (!this$responseMessage.equals(other$responseMessage)) {
                    return false;
                }

                Object this$responseEntityMessages = this.getResponseEntityMessages();
                Object other$responseEntityMessages = other.getResponseEntityMessages();
                if (this$responseEntityMessages == null) {
                    if (other$responseEntityMessages != null) {
                        return false;
                    }
                } else if (!this$responseEntityMessages.equals(other$responseEntityMessages)) {
                    return false;
                }

                Object this$body = this.getBody();
                Object other$body = other.getBody();
                if (this$body == null) {
                    if (other$body != null) {
                        return false;
                    }
                } else if (!this$body.equals(other$body)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof BaseDataResponse;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $responseCode = this.getResponseCode();
        result = result * 59 + ($responseCode == null ? 43 : $responseCode.hashCode());
        Object $responseMessage = this.getResponseMessage();
        result = result * 59 + ($responseMessage == null ? 43 : $responseMessage.hashCode());
        Object $responseEntityMessages = this.getResponseEntityMessages();
        result = result * 59 + ($responseEntityMessages == null ? 43 : $responseEntityMessages.hashCode());
        Object $body = this.getBody();
        result = result * 59 + ($body == null ? 43 : $body.hashCode());
        return result;
    }

    public String toString() {
        return "BaseDataResponse(responseCode=" + this.getResponseCode() + ", responseMessage=" + this.getResponseMessage() + ", responseEntityMessages=" + this.getResponseEntityMessages() + ", body=" + this.getBody() + ")";
    }

    public BaseDataResponse(String responseCode, String responseMessage, List<ValidationErrorResponse> responseEntityMessages, T body) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.responseEntityMessages = responseEntityMessages;
        this.body = body;
    }

    public BaseDataResponse() {
    }
}
