package com.example.user.signup;

/**
 * Lớp SignUpRequest chứa thông tin cần thiết để đăng ký người dùng mới.
 * Bao gồm họ tên, địa chỉ email, mật khẩu và xác nhận mật khẩu.
 *
 * Tác giả: [Tên tác giả]
 */
public class SignUpRequest {
    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;

    public SignUpRequest() {
    }

    /**
     * Constructor của lớp SignUpRequest với các thông tin cần thiết để đăng ký người dùng.
     *
     * @param fullName         Họ tên của người dùng.
     * @param email            Địa chỉ email của người dùng.
     * @param password         Mật khẩu của người dùng.
     * @param confirmPassword  Mật khẩu xác nhận để kiểm tra sự khớp nhau với mật khẩu.
     */
    public SignUpRequest(String fullName, String email, String password, String confirmPassword) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
