package com.example.infrastructure.api;

/**
 * Class ErrorResponse đại diện cho một đối tượng chứa thông tin lỗi từ response của API
 * và cấu trúc của đối tượng phụ thuộc vào cấu trúc lỗi mà API trả về.
 *
 * Tác giả: Trần Văn An
 */
public class ErrorResponse {
    private String status;
    private String message;
    private String timestamp;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}