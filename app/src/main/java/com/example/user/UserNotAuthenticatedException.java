package com.example.user;

/**
 * Ngoại lệ được ném khi người dùng chưa xác thực.
 *
 * Tác giả: Trần Văn An
 */
public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
