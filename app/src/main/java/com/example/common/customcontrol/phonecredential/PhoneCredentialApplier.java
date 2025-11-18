package com.example.common.customcontrol.phonecredential;

import com.google.firebase.auth.PhoneAuthCredential;

/**
 * Interface PhoneCredentialApplier dùng để áp dụng thông tin xác thực điện thoại `PhoneAuthCredential`.
 *
 * Tác giả: Trần Văn An
 */
public interface PhoneCredentialApplier {
    /**
     * Phương thức `apply` để áp dụng thông tin xác thực điện thoại.
     *
     * @param phoneAuthCredential Thông tin xác thực điện thoại (`PhoneAuthCredential`) được cung cấp từ Firebase.
     */
    void apply(PhoneAuthCredential phoneAuthCredential);
}
