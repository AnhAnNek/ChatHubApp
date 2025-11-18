package com.example.user.login.otp.verify;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.ActivityVerifyOtpBinding;
import com.example.infrastructure.BaseActivity;
import com.example.infrastructure.Utils;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Activity cho việc xác minh OTP.
 *
 * Tác giả: Trần Văn An
 */
public class VerifyOtpActivity extends BaseActivity<VerifyOtpViewModel, ActivityVerifyOtpBinding> {

    private AuthRepos authRepos;

    /**
     * Phương thức để lấy layout cho activity.
     *
     * @return ID của layout.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_verify_otp;
    }

    /**
     * Phương thức để lấy lớp ViewModel cho activity.
     *
     * @return Lớp ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected Class<VerifyOtpViewModel> getViewModelClass() {
        return VerifyOtpViewModel.class;
    }

    /**
     * Phương thức để tạo ra ViewModelFactory cho activity.
     *
     * @return ViewModelFactory.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        MediaRepos mediaRepos = new MediaReposImpl();
        UserRepos userRepos = new UserReposImpl(mediaRepos);
        authRepos = new AuthReposImpl(userRepos);
        return new VerifyOtpViewModelFactory(authRepos);
    }

    /**
     * Phương thức được gọi khi activity được tạo ra.
     *
     * @param savedInstanceState Dữ liệu trạng thái trước đó của activity.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lấy dữ liệu từ Intent và gửi OTP
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String phoneNumber = extras.getString(Utils.EXTRA_PHONE_NUMBER, "");
            viewModel.setPhoneNumber(phoneNumber);
            sendOtp(phoneNumber, false);
        }

        // Thiết lập các observer cho LiveData
        setupObservers();
    }

    /**
     * Thiết lập các observer cho LiveData để xử lý các sự kiện.
     *
     * Tác giả: Trần Văn An
     */
    private void setupObservers() {
        viewModel.getResendOtp().observe(this, phoneNumber -> sendOtp(phoneNumber, true));

        viewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToHome(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToSignUp().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToSignUp(EAnimationType.FADE_IN);
            }
        });
    }

    /**
     * Gửi OTP tới số điện thoại đã nhập.
     *
     * @param phoneNumber Số điện thoại để gửi OTP.
     * @param isResend    True nếu đây là yêu cầu gửi lại OTP.
     *
     * Tác giả: Trần Văn An
     */
    private void sendOtp(String phoneNumber, boolean isResend) {
        startResendTimer();
        viewModel.setResendContentStatus(false);

        authRepos.sendOtp(this,
                phoneNumber,
                isResend,
                viewModel.getResendingToken(),
                viewModel.getOnVerificationStateChangedCallbacks());
    }

    /**
     * Bắt đầu đếm ngược cho việc gửi lại OTP.
     *
     * Tác giả: Trần Văn An
     */
    private void startResendTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long timeoutSeconds = viewModel.getTimeoutSeconds();
                timeoutSeconds--;
                viewModel.setTimeoutSeconds(timeoutSeconds);
                String content = String.format("Resend OTP in " + timeoutSeconds + " seconds");
                viewModel.setResendContent(content);
                if (timeoutSeconds <= 0) {
                    viewModel.resetTimeoutSeconds();
                    timer.cancel();
                    runOnUiThread(() -> {
                        viewModel.setResendContent("Resend");
                        viewModel.setResendContentStatus(true);
                    });
                }
            }
        }, 0, 1000);
    }
}