package com.example.common.customcontrol.snackbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.function.Consumer;

/**
 * Lớp SnackbarModel dùng để cấu hình và tạo đối tượng Snackbar tùy chỉnh.
 *
 * Tác giả: Trần Văn An
 */
public class SnackbarModel {
    private int duration;
    private String message;
    private String actionText;
    private Consumer<Void> customAction;
    private Consumer<Integer> onDismissedAction;
    private Consumer<Void> onShownAction;

    /**
     * Constructor riêng tư để khởi tạo đối tượng SnackbarModel thông qua Builder.
     *
     * @param duration Thời lượng hiển thị Snackbar.
     * @param message Nội dung thông báo của Snackbar.
     * @param actionText Văn bản cho nút hành động.
     * @param customAction Hành động tùy chỉnh khi nhấn vào nút hành động.
     * @param onDismissedAction Hành động khi Snackbar bị loại bỏ.
     * @param onShownAction Hành động khi Snackbar được hiển thị.
     */
    private SnackbarModel(int duration, String message, String actionText,
                          Consumer<Void> customAction,
                          Consumer<Integer> onDismissedAction,
                          Consumer<Void> onShownAction) {
        this.duration = duration;
        this.message = message;
        this.actionText = actionText;
        this.customAction = customAction;
        this.onDismissedAction = onDismissedAction;
        this.onShownAction = onShownAction;
    }

    public int getDuration() {
        return duration;
    }

    public String getMessage() {
        return message;
    }

    public String getActionText() {
        return actionText;
    }

    public Consumer<Void> getCustomAction() {
        return customAction;
    }

    public Consumer<Integer> getOnDismissedAction() {
        return onDismissedAction;
    }

    public Consumer<Void> getOnShownAction() {
        return onShownAction;
    }

    /**
     * Lớp Builder dùng để tạo đối tượng SnackbarModel.
     */
    public static class Builder {
        private int duration;
        private String message;
        private String actionText;
        private Consumer<Void> customAction;
        private Consumer<Integer> onDismissedAction;
        private Consumer<Void> onShownAction;

        public Builder() {
            duration = Snackbar.LENGTH_SHORT;
            message = "";
            actionText = "";
            customAction = null;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder actionText(String actionText) {
            this.actionText = actionText;
            return this;
        }

        public Builder customAction(Consumer<Void> customAction) {
            this.customAction = customAction;
            return this;
        }

        public Builder onDismissedAction(Consumer<Integer> onDismissedAction) {
            this.onDismissedAction = onDismissedAction;
            return this;
        }

        public Builder onShownAction(Consumer<Void> onShownAction) {
            this.onShownAction = onShownAction;
            return this;
        }

        public SnackbarModel build() {
            return new SnackbarModel(duration, message, actionText,
                    customAction, onDismissedAction, onShownAction);
        }
    }
}
