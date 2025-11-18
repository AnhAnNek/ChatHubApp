package com.example.common.customcontrol.phonecredential;

/**
 * Lớp `PhoneCredentialDialogModel` chứa cấu hình cho `PhoneCredentialDialogFragment`.
 *
 * Tác giả: Trần Văn An
 */
public class PhoneCredentialDialogModel {

    private PhoneCredentialApplier verifyButtonClickListener;

    /**
     * Constructor của `PhoneCredentialDialogModel`.
     *
     * @param verifyButtonClickListener Listener khi nhấn nút xác minh.
     */
    private PhoneCredentialDialogModel(PhoneCredentialApplier verifyButtonClickListener) {
        this.verifyButtonClickListener = verifyButtonClickListener;
    }

    public PhoneCredentialApplier getVerifyButtonClickListener() {
        return verifyButtonClickListener;
    }

    /**
     * Builder cho `PhoneCredentialDialogModel` để dễ dàng xây dựng đối tượng.
     */
    public static class Builder {
        private PhoneCredentialApplier verifyButtonClickListener;

        public Builder setVerifyButtonClickListener(PhoneCredentialApplier verifyButtonClickListener) {
            this.verifyButtonClickListener = verifyButtonClickListener;
            return this;
        }

        public PhoneCredentialDialogModel build() {
            return new PhoneCredentialDialogModel(verifyButtonClickListener);
        }
    }
}
