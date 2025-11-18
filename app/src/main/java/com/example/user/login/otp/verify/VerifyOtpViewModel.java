package com.example.user.login.otp.verify;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.AppExecutors;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.login.otp.phonenumberinput.PhoneNumberInputActivity;
import com.example.user.repository.AuthRepos;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

/**
 * ViewModel cho quá trình xác minh OTP trong quá trình đăng nhập người dùng.
 *
 * Tác giả: Trần Văn An
 */
public class VerifyOtpViewModel extends BaseViewModel {

    private static final String TAG = VerifyOtpViewModel.class.getSimpleName();

    private final MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private final MutableLiveData<String> otp = new MutableLiveData<>();
    private final MutableLiveData<String> resendOtp = new MutableLiveData<>();
    private final MutableLiveData<String> resendContent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> resendContentStatus = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isOtpVerifying = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSignUp = new MutableLiveData<>();
    private final AuthRepos authRepos;
    private long timeoutSeconds;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    /**
     * Phương thức getter để nhận MutableLiveData lưu trữ số điện thoại.
     * @return MutableLiveData<String> lưu trữ số điện thoại.
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<String> getPhoneNumber() {
        return phoneNumber;
    }


    /**
     * Phương thức setter để thiết lập giá trị số điện thoại.
     * @param phoneNumber Chuỗi số điện thoại cần thiết lập.
     *
     * Tác giả: Trần Văn An
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.postValue(phoneNumber);
    }

    /**
     * Phương thức getter để nhận MutableLiveData lưu trữ mã OTP.
     * @return MutableLiveData<String> lưu trữ mã OTP.
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<String> getOtp() {
        return otp;
    }

    /**
     * Phương thức getter để nhận LiveData lưu trữ mã OTP được gửi lại.
     * @return LiveData<String> lưu trữ mã OTP được gửi lại.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<String> getResendOtp() {
        return resendOtp;
    }

    /**
     * Phương thức getter để nhận LiveData lưu trữ nội dung thông báo gửi lại OTP.
     * @return LiveData<String> lưu trữ nội dung thông báo gửi lại OTP.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<String> getResendContent() {
        return resendContent;
    }

    /**
     * Phương thức setter để thiết lập nội dung thông báo gửi lại OTP.
     * @param content Chuỗi nội dung thông báo cần thiết lập.
     *
     * Tác giả: Trần Văn An
     */
    public void setResendContent(String content) {
        resendContent.postValue(content);
    }

    /**
     * Phương thức getter để nhận LiveData lưu trữ trạng thái của nội dung thông báo gửi lại OTP.
     * @return LiveData<Boolean> lưu trữ trạng thái của nội dung thông báo gửi lại OTP.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getResendContentStatus() {
        return resendContentStatus;
    }

    /**
     * Phương thức setter để thiết lập trạng thái của nội dung thông báo gửi lại OTP.
     * @param status Giá trị boolean đại diện cho trạng thái của nội dung thông báo.
     *
     * Tác giả: Trần Văn An
     */
    public void setResendContentStatus(boolean status) {
        resendContentStatus.postValue(status);
    }

    /**
     * Phương thức getter để nhận LiveData lưu trữ trạng thái của quá trình xác minh OTP.
     * @return LiveData<Boolean> lưu trữ trạng thái của quá trình xác minh OTP.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getIsOtpVerifying() {
        return isOtpVerifying;
    }

    /**
     * Phương thức getter để nhận LiveData định hướng đến màn hình chính.
     * @return LiveData<Boolean> định hướng đến màn hình chính.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
    }

    /**
     * Phương thức getter để nhận LiveData định hướng đến màn hình đăng ký.
     * @return LiveData<Boolean> định hướng đến màn hình đăng ký.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateToSignUp() {
        return navigateToSignUp;
    }

    /**
     * Constructor của ViewModel.
     * Khởi tạo AuthRepos và thiết lập các giá trị ban đầu cho MutableLiveData.
     *
     * @param authRepos AuthRepos được sử dụng để xác thực OTP
     * Tác giả: Trần Văn An
     */
    public VerifyOtpViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;

        this.resendContentStatus.postValue(false);
        resetTimeoutSeconds();
    }

    /**
     * Gửi yêu cầu gửi lại OTP.
     * Lấy số điện thoại từ MutableLiveData và đặt giá trị cho MutableLiveData resendOtp.
     * Tác giả: Trần Văn An
     */
    public void resendOtp() {
        String phoneNumber = this.phoneNumber.getValue();
        this.resendOtp.postValue(phoneNumber);
    }

    /**
     * Xác minh OTP.
     * Lấy giá trị OTP từ MutableLiveData và gọi phương thức verifyOtp(otp).
     * Tác giả: Trần Văn An
     */
    public void verifyOtp() {
        String enteredOtp = otp.getValue() != null ? otp.getValue() : "";
        verifyOtp(enteredOtp);
    }

    /**
     * Xác minh OTP với một mã OTP đã cung cấp.
     * Tạo PhoneAuthCredential từ verificationId và enteredOtp,
     * sau đó gọi phương thức signIn.
     *
     * @param enteredOtp Mã OTP đã nhập
     * Tác giả: Trần Văn An
     */
    private void verifyOtp(String enteredOtp) {
        try {
            PhoneAuthCredential phoneCredential = PhoneAuthProvider.getCredential(verificationId, enteredOtp);
            signIn(phoneCredential);
        } catch (IllegalArgumentException e) {
            errorToastMessage.postValue("Failed to verify OTP");
            Log.e(TAG, "Error: " + e.getMessage(), e);
        }
    }

    /**
     * Trả về một đối tượng PhoneAuthProvider.OnVerificationStateChangedCallbacks.
     * Sử dụng để xử lý các sự kiện liên quan đến quá trình xác minh OTP.
     *
     * @return Đối tượng PhoneAuthProvider.OnVerificationStateChangedCallbacks
     * Tác giả: Trần Văn An
     */
    public PhoneAuthProvider.OnVerificationStateChangedCallbacks getOnVerificationStateChangedCallbacks() {
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // Xác minh thành công, thực hiện đăng nhập và điều hướng
                signIn(phoneAuthCredential);
                isOtpVerifying.postValue(false);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // Xác minh thất bại, cập nhật trạng thái và hiển thị thông báo lỗi
                errorToastMessage.postValue("OTP verification failed");
                isOtpVerifying.postValue(false);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                // Mã OTP đã được gửi thành công, cập nhật verificationId và resendingToken
                verificationId = s;
                resendingToken = forceResendingToken;
                successToastMessage.postValue("OTP sent successfully");
                isOtpVerifying.postValue(false);
            }
        };
    }

    /**
     * Đăng nhập bằng PhoneAuthCredential.
     * Xác minh thông tin và điều hướng người dùng đến màn hình chính sau khi đăng nhập thành công.
     *
     * @param phoneCredential PhoneAuthCredential để đăng nhập
     * Tác giả: Trần Văn An
     */
    private void signIn(PhoneAuthCredential phoneCredential) {
        this.isOtpVerifying.postValue(true);
        authRepos.signInWithCredential(phoneCredential)
                .thenAccept(aVoid -> {
                    // Đăng nhập thành công, điều hướng người dùng đến màn hình chính
                    this.isOtpVerifying.postValue(false);
                    successToastMessage.postValue("Verify successfully");
                    new Handler().postDelayed(this::navigateToHome, 200);
                })
                .exceptionally(e -> {
                    // Đăng nhập thất bại, cập nhật trạng thái và hiển thị thông báo lỗi
                    this.isOtpVerifying.postValue(false);
                    errorToastMessage.postValue("Verify unsuccessfully");
                    Log.e(PhoneNumberInputActivity.class.getSimpleName(), "Error: ", e);
                    return null;
                });
    }

    /**
     * Xử lý sự kiện khi nội dung OTP được thay đổi.
     * Kiểm tra xem OTP có hợp lệ không và xác minh nếu hợp lệ.
     *
     * @param text Nội dung mới của ô nhập OTP
     * Tác giả: Trần Văn An
     */
    public void onOtpChanged(CharSequence text) {
        String otp = text.toString();
        if (Utils.isValidOtp(otp)) {
            verifyOtp(otp);
        }
    }

    /**
     * Điều hướng tới màn hình chính (Home).
     * Gửi sự kiện điều hướng tới LiveData navigateToHome.
     * Tác giả: Trần Văn An
     */
    public void navigateToHome() {
        this.navigateToHome.postValue(true);
    }

    /**
     * Điều hướng tới màn hình đăng ký (Sign Up).
     * Gửi sự kiện điều hướng tới LiveData navigateToSignUp.
     * Tác giả: Trần Văn An
     */
    public void navigateToSignUp() {
        this.navigateToSignUp.postValue(true);
    }

    /**
     * Lấy giá trị thời gian chờ đợi (timeout) cho OTP.
     *
     * @return Giá trị thời gian chờ đợi
     */
    public long getTimeoutSeconds() {
        return timeoutSeconds;
    }

    /**
     * Thiết lập giá trị thời gian chờ đợi (timeout) cho OTP.
     *
     * @param timeoutSeconds Giá trị thời gian chờ đợi mới
     * Tác giả: Trần Văn An
     */
    public void setTimeoutSeconds(long timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    /**
     * Thiết lập lại giá trị thời gian chờ đợi (timeout) cho OTP về giá trị mặc định.
     * Tác giả: Trần Văn An
     */
    public void resetTimeoutSeconds() {
        timeoutSeconds = Utils.OTP_TIME_OUT_SECONDS;
    }

    /**
     * Lấy mã thông báo để gửi lại OTP.
     *
     * @return Mã thông báo để gửi lại OTP
     * Tác giả: Trần Văn An
     */
    public PhoneAuthProvider.ForceResendingToken getResendingToken() {
        return resendingToken;
    }
}
