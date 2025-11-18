package com.example.common.customcontrol.emailpassworddialog;

/**
 * Interface EmailPasswordApplier được sử dụng để định nghĩa hàm xử lý khi nhấn nút submit
 * trong EmailPasswordDialog.
 */
public interface EmailPasswordApplier {

    /**
     * Phương thức xử lý khi người dùng nhấn nút submit trong hộp thoại.
     *
     * @param email    Email mà người dùng nhập vào
     * @param password Mật khẩu mà người dùng nhập vào
     *
     * Tác giả: Trần Văn An
     */
    void apply(String email, String password);
}
