package de.balogha.footballint.dbrestservice.model;

public class ApiError {

    private String request;
    private int status;
    private String message;

    public ApiError() {
    }

    public ApiError(String request, int status, String message) {
        this.request = request;
        this.status = status;
        this.message = message;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
