package com.example.user.signup;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.AppExecutors;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.Validator;
import com.example.user.repository.AuthRepos;

/**
 * ViewModel cho SignUpActivity.
 * Quản lý dữ liệu và logic xử lý cho giao diện đăng ký.
 *
 * Tác giả: Trần Văn An
 */
public class SignUpViewModel extends BaseViewModel {

    private static final String TAG = SignUpViewModel.class.getSimpleName();

    // Dữ liệu người dùng nhập
    private final MutableLiveData<String> fullName = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPassword = new MutableLiveData<>();

    // Trạng thái điều hướng
    private final MutableLiveData<Boolean> navigateToLogin = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();

    // Trạng thái đăng ký
    private final MutableLiveData<Boolean> isSigningUp = new MutableLiveData<>();

    /**
     * Trả về LiveData chứa giá trị của tên đầy đủ được người dùng nhập.
     * LiveData này sẽ cập nhật mỗi khi giá trị tên đầy đủ thay đổi.
     *
     * @return LiveData<String> chứa giá trị của tên đầy đủ.
     */
    public MutableLiveData<String> getFullName() {
        return fullName;
    }

    /**
     * Trả về LiveData chứa giá trị của email được người dùng nhập.
     * LiveData này sẽ cập nhật mỗi khi giá trị email thay đổi.
     *
     * @return LiveData<String> chứa giá trị của email.
     */
    public MutableLiveData<String> getEmail() {
        return email;
    }

    /**
     * Trả về LiveData chứa giá trị của mật khẩu được người dùng nhập.
     * LiveData này sẽ cập nhật mỗi khi giá trị mật khẩu thay đổi.
     *
     * @return LiveData<String> chứa giá trị của mật khẩu.
     */
    public MutableLiveData<String> getPassword() {
        return password;
    }

    /**
     * Trả về LiveData chứa giá trị của mật khẩu xác nhận được người dùng nhập.
     * LiveData này sẽ cập nhật mỗi khi giá trị mật khẩu xác nhận thay đổi.
     *
     * @return LiveData<String> chứa giá trị của mật khẩu xác nhận.
     */
    public MutableLiveData<String> getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Trả về LiveData biểu thị xem có cần điều hướng sang màn hình đăng nhập hay không.
     * LiveData này sẽ cập nhật mỗi khi có yêu cầu điều hướng đến màn hình đăng nhập.
     *
     * @return LiveData<Boolean> biểu thị yêu cầu điều hướng sang màn hình đăng nhập.
     */
    public LiveData<Boolean> getNavigateToLogin() {
        return navigateToLogin;
    }

    /**
     * Trả về LiveData biểu thị xem có cần điều hướng sang màn hình chính sau khi đăng ký thành công hay không.
     * LiveData này sẽ cập nhật mỗi khi có yêu cầu điều hướng đến màn hình chính.
     *
     * @return LiveData<Boolean> biểu thị yêu cầu điều hướng sang màn hình chính.
     */
    public LiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
    }

    /**
     * Trả về LiveData biểu thị xem quá trình đăng ký đang diễn ra hay không.
     * LiveData này sẽ cập nhật mỗi khi trạng thái của quá trình đăng ký thay đổi.
     *
     * @return LiveData<Boolean> biểu thị trạng thái của quá trình đăng ký.
     */
    public LiveData<Boolean> getIsSigningUp() {
        return isSigningUp;
    }

    /**
     * Khởi tạo một ViewModel mới với AuthRepos được cung cấp.
     *
     * @param authRepos Repository để xử lý các thao tác xác thực người dùng.
     *
     * Tác giả: Trần Văn An
     */
    public SignUpViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    /**
     * Điều hướng sang màn hình đăng nhập.
     * Gửi một sự kiện điều hướng tới View để chuyển sang màn hình đăng nhập.
     */
    public void navigateToLogin() {
        navigateToLogin.postValue(true);
    }

    /**
     * Phương thức xử lý đăng ký người dùng.
     * Kiểm tra dữ liệu, thực hiện đăng ký qua AuthRepos và điều hướng sau khi đăng ký thành công.
     */
    public void signUp() {
        isSigningUp.postValue(true);

        // Lấy các giá trị tên, email, mật khẩu và mật khẩu xác nhận từ LiveData và chuyển đổi chúng thành chuỗi.
        String fullName = Utils.convertToString(this.fullName);
        String email = Utils.convertToString(this.email);
        String password = Utils.convertToString(this.password);
        String confirmPassword = Utils.convertToString(this.confirmPassword);

        // Kiểm tra tính hợp lệ của tên.
        if (fullName.isEmpty()) {
            errorToastMessage.postValue(Validator.FULL_NAME_REQUIRED_MSG);
            isSigningUp.postValue(false);
            return;
        }

        // Kiểm tra tính hợp lệ của địa chỉ email.
        String emailError = Validator.validateEmail(email);
        if (emailError != null) {
            errorToastMessage.postValue(emailError);
            isSigningUp.postValue(false);
            return;
        }

        // Kiểm tra tính hợp lệ của mật khẩu.
        String passError = Validator.validatePassword(password);
        if (passError != null) {
            errorToastMessage.postValue(passError);
            isSigningUp.postValue(false);
            return;
        }

        // Kiểm tra tính hợp lệ của mật khẩu xác nhận.
        String confirmPassError = Validator.validateConfirmPassword(password, confirmPassword);
        if (confirmPassError != null) {
            errorToastMessage.postValue(confirmPassError);
            isSigningUp.postValue(false);
            return;
        }

        // Tạo đối tượng SignUpRequest từ các giá trị đã lấy và gửi yêu cầu đăng ký đến AuthRepos.
        SignUpRequest signUpRequest = new SignUpRequest(fullName, email, password, confirmPassword);
        AppExecutors.getIns().networkIO().execute(() -> {
            authRepos.signUp(signUpRequest)
                    // Xử lý kết quả đăng ký thành công.
                    .thenAccept(aVoid -> {
                        successToastMessage.postValue("Sign up successful");
                        isSigningUp.postValue(false);
                        new Handler().postDelayed(this::navigateToHome, 500);
                    })
                    // Xử lý lỗi nếu có.
                    .exceptionally(e -> {
                        errorToastMessage.postValue(e.getMessage());
                        new Handler().postDelayed(() -> isSigningUp.postValue(false), 500);
                        Log.e(TAG, "Error: " + e);
                        return null;
                    });
        });
    }

    /**
     * Điều hướng sang màn hình chính sau khi đăng ký thành công.
     */
    private void navigateToHome() {
        navigateToHome.postValue(true);
    }
}
