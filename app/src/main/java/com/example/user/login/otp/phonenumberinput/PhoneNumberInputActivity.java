package com.example.user.login.otp.phonenumberinput;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.ActivityPhoneNumberInputBinding;
import com.example.infrastructure.BaseActivity;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

/**
 * Lớp hoạt động để nhập số điện thoại để xác minh OTP.
 *
 * Tác giả: Trần Văn An
 */
public class PhoneNumberInputActivity extends BaseActivity<PhoneNumberInputViewModel, ActivityPhoneNumberInputBinding> {

    private AuthRepos authRepos;

    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_phone_number_input;
    }

    @Override
    protected Class<PhoneNumberInputViewModel> getViewModelClass() {
        return PhoneNumberInputViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        MediaRepos mediaRepos = new MediaReposImpl();
        UserRepos userRepos = new UserReposImpl(mediaRepos);
        authRepos = new AuthReposImpl(userRepos);
        return new PhoneNumberInputViewModelFactory(authRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupObservers();
    }

    /**
     * Thiết lập các trình quan sát để theo dõi các sự kiện từ ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    private void setupObservers() {
        viewModel.getNavigateToVerifyOtpWithPhoneNumber().observe(this, data -> {
            navigationManager.navigateToVerifyOtp(data, EAnimationType.FADE_IN);
        });

        viewModel.getNavigateToSignUp().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToSignUp(EAnimationType.FADE_IN);
            }
        });
    }
}