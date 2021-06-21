package vn.actvn.onthionline.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseDataRequest<T> {
    @JsonProperty("body")
    private T body;

    public T getBody() {
        return this.body;
    }

    @JsonProperty("body")
    public void setBody(T body) {
        this.body = body;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseDataRequest)) {
            return false;
        } else {
            BaseDataRequest<?> other = (BaseDataRequest)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
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
        return other instanceof BaseDataRequest;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $body = this.getBody();
        result = result * 59 + ($body == null ? 43 : $body.hashCode());
        return result;
    }

    public String toString() {
        return "BaseDataRequest(body=" + this.getBody() + ")";
    }

    public BaseDataRequest(T body) {
        this.body = body;
    }

    public BaseDataRequest() {
    }
}

