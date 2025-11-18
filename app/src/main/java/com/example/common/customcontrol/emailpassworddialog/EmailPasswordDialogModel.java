package com.example.common.customcontrol.emailpassworddialog;

/**
 * Lớp EmailPasswordDialogModel chứa dữ liệu cần thiết để hiển thị và xử lý
 * một hộp thoại yêu cầu email và mật khẩu.
 *
 * Tác giả: Trần Văn An
 */
public class EmailPasswordDialogModel {
    private String title;
    private String subTitle;
    private String email;
    private String password;
    private EmailPasswordApplier submitButtonClickListener;

    private EmailPasswordDialogModel(String title, String subTitle, String email, String password, EmailPasswordApplier submitButtonClickListener) {
        this.title = title;
        this.subTitle = subTitle;
        this.email = email;
        this.password = password;
        this.submitButtonClickListener = submitButtonClickListener;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public EmailPasswordApplier getSubmitButtonClickListener() {
        return submitButtonClickListener;
    }

    /**
     * Builder cho EmailPasswordDialogModel để tạo đối tượng một cách dễ dàng và linh hoạt.
     */
    public static class Builder {
        private String title;
        private String subTitle;
        private String email;
        private String password;
        private EmailPasswordApplier submitButtonClickListener;

        // Phương thức thiết lập tiêu đề
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        // Phương thức thiết lập phụ đề
        public Builder setSubTitle(String subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        // Phương thức thiết lập email
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        // Phương thức thiết lập mật khẩu
        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        // Phương thức thiết lập listener cho nút submit
        public Builder setSubmitButtonClickListener(EmailPasswordApplier submitButtonClickListener) {
            this.submitButtonClickListener = submitButtonClickListener;
            return this;
        }

        // Phương thức build() để tạo đối tượng EmailPasswordDialogModel từ các thuộc tính đã được thiết lập
        public EmailPasswordDialogModel build() {
            EmailPasswordDialogModel model = new EmailPasswordDialogModel(title, subTitle,
                    email, password, submitButtonClickListener);
            return model;
        }
    }
}
