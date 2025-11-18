package com.example.common.customcontrol.customalertdialog;

import java.util.function.Consumer;

/**
 * AlertDialogModel là một lớp dữ liệu chứa thông tin hiển thị cho hộp thoại cảnh báo.
 *
 * Tác giả: Trần Văn An
 */
public class AlertDialogModel {
    private String title;
    private String message;
    private String positiveBtnTitle;
    private String negativeBtnTitle;
    private Consumer<Void> positiveButtonClickListener;
    private Consumer<Void> negativeButtonClickListener;

    private AlertDialogModel() {
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getPositiveBtnTitle() {
        return positiveBtnTitle;
    }

    public String getNegativeBtnTitle() {
        return negativeBtnTitle;
    }

    public Consumer<Void> getPositiveButtonClickListener() {
        return positiveButtonClickListener;
    }

    public Consumer<Void> getNegativeButtonClickListener() {
        return negativeButtonClickListener;
    }

    /**
     * Builder là lớp con tĩnh để xây dựng đối tượng AlertDialogModel.
     */
    public static class Builder {
        private String title;
        private String message;
        private String positiveBtnTitle;
        private String negativeBtnTitle;
        private Consumer<Void> positiveButtonClickListener;
        private Consumer<Void> negativeButtonClickListener;

        // Phương thức thiết lập tiêu đề
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        // Phương thức thiết lập tin nhắn
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        // Phương thức thiết lập tiêu đề và sự kiện cho positive button
        public Builder setPositiveButton(String title, Consumer<Void> listener) {
            positiveBtnTitle = title;
            positiveButtonClickListener = listener;
            return this;
        }

        // Phương thức thiết lập tiêu đề và sự kiện cho negative button
        public Builder setNegativeButton(String title, Consumer<Void> listener) {
            negativeBtnTitle = title;
            negativeButtonClickListener = listener;
            return this;
        }

        public AlertDialogModel build() {
            AlertDialogModel alertDialogModel = new AlertDialogModel();
            alertDialogModel.title = this.title;
            alertDialogModel.message = this.message;
            alertDialogModel.positiveBtnTitle = this.positiveBtnTitle;
            alertDialogModel.negativeBtnTitle = this.negativeBtnTitle;
            alertDialogModel.positiveButtonClickListener = this.positiveButtonClickListener;
            alertDialogModel.negativeButtonClickListener = this.negativeButtonClickListener;
            return alertDialogModel;
        }
    }
}
