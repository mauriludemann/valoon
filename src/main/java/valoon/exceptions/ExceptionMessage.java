package valoon.exceptions;

import java.util.List;

public class ExceptionMessage {

    private Integer status;
    private String message;
    private String error;

    public ExceptionMessage() {
    }

    public ExceptionMessage(Integer status, String message, String error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public ExceptionMessage(BaseException bae) {
        this(bae.getStatusCode(), bae.getMessage(), bae.getErrorCode());
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
