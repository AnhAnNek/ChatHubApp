package com.example.user.login;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.AppExecutors;
import com.example.infrastructure.BaseViewModel;
import com.example.user.repository.AuthRepos;

/**
 * LoginViewModel là ViewModel xử lý logic cho chức năng đăng nhập.
 *
 * Tác giả: Trần Văn An
 */
public class LoginViewModel extends BaseViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();

    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToForgotPassword = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSignUp = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSendOtp = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToGoogleSignIn = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLogging = new MutableLiveData<>();

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
     * Lấy đối tượng LiveData của password.
     *
     * @return LiveData của password.
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<String> getPassword() {
        return password;
    }

    /**
     * Lấy đối tượng LiveData của navigateToForgotPassword.
     *
     * @return LiveData của navigateToForgotPassword.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateToForgotPassword() {
        return navigateToForgotPassword;
    }

    /**
     * Lấy đối tượng LiveData của navigateToSignUp.
     *
     * @return LiveData của navigateToSignUp.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateToSignUp() {
        return navigateToSignUp;
    }

    /**
     * Lấy đối tượng LiveData của navigateToHome.
     *
     * @return LiveData của navigateToHome.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
    }

    /**
     * Lấy đối tượng LiveData của navigateToSendOtp.
     *
     * @return LiveData của navigateToSendOtp.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateToSendOtp() {
        return navigateToSendOtp;
    }

    /**
     * Lấy đối tượng LiveData của navigateToGoogleSignIn.
     *
     * @return LiveData của navigateToGoogleSignIn.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateToGoogleSignIn() {
        return navigateToGoogleSignIn;
    }

    /**
     * Lấy đối tượng LiveData của isLogging.
     *
     * @return LiveData của isLogging.
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<Boolean> getIsLogging() {
        return isLogging;
    }

    /**
     * Constructor khởi tạo ViewModel với AuthRepos.
     *
     * @param authRepos đối tượng AuthRepos để thực hiện các thao tác liên quan tới xác thực.
     *
     * Tác giả: Trần Văn An
     */
    public LoginViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    /**
     * Xử lý sự kiện khi người dùng nhấn nút đăng nhập.
     * Kiểm tra và xác thực thông tin đăng nhập, sau đó thực hiện đăng nhập.
     *
     * Tác giả: Trần Văn An
     */
    public void onLoginBtnClick() {
        isLogging.postValue(true);
        Log.i(TAG, "Login button clicked");

        trimAllInputs();
        String email = this.email.getValue() != null ? this.email.getValue() : "";
        String password = this.password.getValue() != null ? this.password.getValue() : "";

        if (!isValidEmail(email)) {
            errorToastMessage.postValue("Enter your email.");
            isLogging.postValue(false);
            return;
        }

        if (!isValidPassword(password)) {
            errorToastMessage.postValue("Enter proper password.");
            isLogging.postValue(false);
            return;
        }

        SignInRequest signInRequest = new SignInRequest(email, password);
        AppExecutors.getIns().networkIO().execute(() -> {
            signIn(signInRequest);
        });
    }

    /**
     * Thực hiện đăng nhập với thông tin yêu cầu đăng nhập.
     * Nếu đăng nhập thành công, điều hướng tới màn hình chính.
     * Nếu đăng nhập thất bại, hiển thị thông báo lỗi.
     *
     * @param signInRequest thông tin yêu cầu đăng nhập.
     *
     * Tác giả: Trần Văn An
     */
    private void signIn(SignInRequest signInRequest) {
        // Gọi phương thức signInWithEmailPassword của AuthRepos để thực hiện đăng nhập với email và password.
        authRepos.signInWithEmailPassword(signInRequest)
                // Nếu đăng nhập thành công, xử lý trong thenAccept.
                .thenAccept(aVoid -> {
                    isLogging.postValue(false);
                    // Hiển thị thông báo thành công thông qua successToastMessage.
                    successToastMessage.postValue("Login successfully");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            navigateToHome();
                        }
                    }, 100);
                })
                // Nếu đăng nhập thất bại, xử lý trong exceptionally.
                .exceptionally(e -> {
                    // Hiển thị thông báo lỗi thông qua errorToastMessage.
                    errorToastMessage.postValue(e.getMessage());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isLogging.postValue(false);
                        }
                    }, 500);
                    Log.e(TAG, "Error: " + e);
                    return null;
                });
    }

    /**
     * Cắt bỏ khoảng trắng ở đầu và cuối của email và password.
     *
     * Tác giả: Trần Văn An
     */
    private void trimAllInputs() {
        String email = this.email.getValue() != null ? this.email.getValue() : "";
        String password = this.password.getValue() != null ? this.password.getValue() : "";

        this.email.postValue(email.trim());
        this.password.postValue(password.trim());
    }

    /**
     * Kiểm tra tính hợp lệ của email.
     *
     * @param email email cần kiểm tra.
     * @return true nếu email hợp lệ, ngược lại false.
     *
     * Tác giả: Trần Văn An
     */
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Kiểm tra tính hợp lệ của password.
     *
     * @param password password cần kiểm tra.
     * @return true nếu password hợp lệ, ngược lại false.
     *
     * Tác giả: Trần Văn An
     */
    private boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    /**
     * Điều hướng tới màn hình quên mật khẩu.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToForgotPassword() {
        navigateToForgotPassword.postValue(true);
    }

    /**
     * Điều hướng tới màn hình đăng ký.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToSignUp() {
        navigateToSignUp.postValue(true);
    }

    /**
     * Điều hướng tới màn hình chính nếu người dùng đã đăng nhập.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateIfAuthenticated() {
        if (authRepos.isLoggedIn()) {
            navigateToHome.postValue(true);
        }
    }

    /**
     * Điều hướng tới màn hình chính.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToHome() {
        navigateToHome.postValue(true);
    }

    /**
     * Điều hướng tới màn hình gửi mã OTP.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToSendOtp() {
        navigateToSendOtp.postValue(true);
    }

    /**
     * Điều hướng tới màn hình đăng nhập Google.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToGoogleSignIn() {
        navigateToGoogleSignIn.postValue(true);
    }
}