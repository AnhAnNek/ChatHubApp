package com.example.user;

import org.jetbrains.annotations.Nullable;

/**
 * Tác giả: Trần Văn An
 */
public class Validator {

    // Định nghĩa chiều dài tối thiểu của mật khẩu
    private static final int MIN_PASSWORD_LENGTH = 8;

    // Thông báo lỗi khi trường tên không được nhập
    public static final String FULL_NAME_REQUIRED_MSG = "Full name is required";

    /**
     * Kiểm tra tính hợp lệ của mật khẩu.
     *
     * @param password mật khẩu cần kiểm tra
     * @return chuỗi thông báo lỗi nếu mật khẩu không hợp lệ, null nếu mật khẩu hợp lệ
     *
     *  Tác giả: Trần Văn An
     */
    public static String validatePassword(@Nullable String password) {
        if (password == null || password.isEmpty()) {
            return "Password is required";
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long";
        }

        // Biến để kiểm tra xem mật khẩu có chứa chữ cái và số hay không
        boolean containsLetter = false;
        boolean containsNumber = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                // Nếu có ký tự chữ cái, đặt containsLetter thành true
                containsLetter = true;
            } else if (Character.isDigit(c)) {
                // Nếu có ký tự số, đặt containsNumber thành true
                containsNumber = true;
            }
        }

        if (!containsLetter) {
            return "Password must contain at least one letter";
        }

        if (!containsNumber) {
            return "Password must contain at least one number";
        }

        return null;
    }

    /**
     * Kiểm tra tính hợp lệ của mật khẩu xác nhận.
     *
     * @param password mật khẩu ban đầu
     * @param confirmPassword mật khẩu xác nhận
     * @return chuỗi thông báo lỗi nếu mật khẩu xác nhận không hợp lệ, null nếu mật khẩu xác nhận hợp lệ
     *
     *  Tác giả: Trần Văn An
     */
    public static String validateConfirmPassword(
            String password,
            @Nullable String confirmPassword
    ) {
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            return "Confirm password is required";
        }

        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        } else {
            return null;
        }
    }

    /**
     * Kiểm tra tính hợp lệ của email.
     *
     * @param email địa chỉ email cần kiểm tra
     * @return chuỗi thông báo lỗi nếu email không hợp lệ, null nếu email hợp lệ
     *
     *  Tác giả: Trần Văn An
     */
    public static String validateEmail(@Nullable String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Email is required";
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (!email.matches(emailRegex)) {
            return "Invalid email format";
        }

        return null;
    }

    /**
     * Kiểm tra tính hợp lệ của số điện thoại.
     *
     * @param phoneNumber số điện thoại cần kiểm tra
     * @return chuỗi thông báo lỗi nếu số điện thoại không hợp lệ, null nếu số điện thoại hợp lệ
     *
     *  Tác giả: Trần Văn An
     */
    public static String validPhoneNumber(@Nullable String phoneNumber) {
        if (phoneNumber == null) {
            return "Phone number is required";
        }

        // Biểu thức chính quy để kiểm tra định dạng số điện thoại
        String regex = "^[+]?[0-9]{8,15}$";

        // Kiểm tra nếu số điện thoại không khớp với biểu thức chính quy
        if (!phoneNumber.matches(regex)) {
            return "Invalid phone number";
        }

        return null;
    }
}
