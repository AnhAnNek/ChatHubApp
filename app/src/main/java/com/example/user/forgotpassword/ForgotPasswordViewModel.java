package com.example.user.forgotpassword;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.repository.AuthRepos;

/**
 * ForgotPasswordViewModel là ViewModel xử lý logic cho chức năng quên mật khẩu.
 *
 * Tác giả: Trần Văn An
 */
public class ForgotPasswordViewModel extends BaseViewModel {

    private static final String TAG = ForgotPasswordViewModel.class.getSimpleName();

    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToLogin = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSending = new MutableLiveData<>();

    /**
     * Lấy đối tượng LiveData của email.
     *
     * @return LiveData của email.
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<String> getEmail() {
        return email;
    }

    /**
     * Lấy đối tượng LiveData của navigateToLogin.
     *
     * @return LiveData của navigateToLogin.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateToLogin() {
        return navigateToLogin;
    }

    /**
     * Lấy đối tượng LiveData của isSending.
     *
     * @return LiveData của isSending.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getIsSending() {
        return isSending;
    }

    /**
     * Constructor khởi tạo ViewModel với AuthRepos.
     *
     * @param authRepos đối tượng AuthRepos để thực hiện các thao tác liên quan tới xác thực.
     *
     * Tác giả: Trần Văn An
     */
    public ForgotPasswordViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    /**
     * Điều hướng tới màn hình đăng nhập.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToLogin() {
        navigateToLogin.postValue(true);
    }

    /**
     * Xử lý sự kiện khi người dùng nhấn vào nút gửi email reset mật khẩu.
     * Phương thức này sẽ kiểm tra email hợp lệ, sau đó sử dụng AuthRepos để gửi yêu cầu reset mật khẩu.
     *
     * Tác giả: Trần Văn An
     */
    public void onSendResetPasswordClick() {
        isSending.postValue(true);
        String email = this.email.getValue();
        if (email == null || email.isEmpty()) {
            // Nếu email trống, hiển thị thông báo lỗi và dừng lại.
            errorToastMessage.postValue("Enter your email");
            isSending.postValue(false);
            return;
        }

        this.email.postValue(email.trim());
        authRepos.sendPasswordResetEmail(email)
                .thenAccept(aVoid -> {
                    // Nếu gửi thành công, hiển thị thông báo thành công
                    // và điều hướng tới màn hình đăng nhập sau 500ms.
                    successToastMessage.postValue("Password reset link sent to your Email");
                    isSending.postValue(false);
                    new Handler().postDelayed(this::navigateToLogin, 500);
                })
                .exceptionally(e -> {
                    // Nếu có lỗi, hiển thị thông báo lỗi và log lỗi.
                    errorToastMessage.postValue("Failed to reset password");
                    isSending.postValue(false);
                    Log.e(TAG, "Error: " + e);
                    return null;
                });
    }
}
