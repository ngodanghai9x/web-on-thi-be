package vn.actvn.onthionline.common.exception;

public class KeyValue {
    private String key;
    private String value;

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof KeyValue)) {
            return false;
        } else {
            KeyValue other = (KeyValue)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$key = this.getKey();
                Object other$key = other.getKey();
                if (this$key == null) {
                    if (other$key != null) {
                        return false;
                    }
                } else if (!this$key.equals(other$key)) {
                    return false;
                }

                Object this$value = this.getValue();
                Object other$value = other.getValue();
                if (this$value == null) {
                    if (other$value != null) {
                        return false;
                    }
                } else if (!this$value.equals(other$value)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof KeyValue;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $key = this.getKey();
        result = result * 59 + ($key == null ? 43 : $key.hashCode());
        Object $value = this.getValue();
        result = result * 59 + ($value == null ? 43 : $value.hashCode());
        return result;
    }

    public String toString() {
        return "KeyValue(key=" + this.getKey() + ", value=" + this.getValue() + ")";
    }

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValue() {
    }
}
