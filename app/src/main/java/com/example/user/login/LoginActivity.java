package com.example.user.login;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.ActivityLoginBinding;
import com.example.infrastructure.BaseActivity;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

/**
 * LoginActivity là Activity xử lý giao diện cho chức năng đăng nhập.
 *
 * Tác giả: Trần Văn An
 */
public class LoginActivity extends BaseActivity<LoginViewModel, ActivityLoginBinding> {

    /**
     * Phương thức trả về layout resource ID cho Activity.
     *
     * @return layout resource ID.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_login;
    }

    /**
     * Phương thức trả về class của ViewModel sử dụng trong Activity.
     *
     * @return class của ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    /**
     * Phương thức tạo ViewModelFactory sử dụng để khởi tạo ViewModel.
     * Khởi tạo các repository cần thiết và truyền vào ViewModelFactory.
     *
     * @return ViewModelProvider.Factory để tạo ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        MediaRepos mediaRepos = new MediaReposImpl();
        UserRepos userRepos = new UserReposImpl(mediaRepos);
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        return new LoginViewModelFactory(authRepos);
    }

    /**
     * Phương thức onCreate được gọi khi Activity được khởi tạo.
     * Thiết lập các observer để lắng nghe thay đổi từ ViewModel.
     *
     * @param savedInstanceState Bundle lưu trữ trạng thái trước đó của Activity.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel.navigateIfAuthenticated();
        setupObservers();
    }

    /**
     * Thiết lập observer để lắng nghe thay đổi của các navigate LiveData từ ViewModel.
     * Khi navigate LiveData thay đổi, điều hướng tới các màn hình tương ứng.
     *
     * Tác giả: Trần Văn An
     */
    private void setupObservers() {
        viewModel.getNavigateToForgotPassword().observe(this, navigate -> {
            if (navigate) {
                // Điều hướng tới màn hình quên mật khẩu với hiệu ứng fade in.
                navigationManager.navigateToForgotPassword(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToSignUp().observe(this, navigate -> {
            if (navigate) {
                // Điều hướng tới màn hình đăng ký với hiệu ứng fade in.
                navigationManager.navigateToSignUp(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                // Điều hướng tới màn hình chính với hiệu ứng fade in.
                navigationManager.navigateToHome(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToSendOtp().observe(this, navigate -> {
            if (navigate) {
                // Điều hướng tới màn hình nhập số điện thoại với hiệu ứng fade in.
                navigationManager.navigateToPhoneNumberInput(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToGoogleSignIn().observe(this, navigate -> {
            if (navigate) {
                // Điều hướng tới màn hình đăng nhập Google với hiệu ứng fade in.
                navigationManager.navigateToGoogleSignIn(EAnimationType.FADE_IN);
            }
        });
    }
}