package com.example.user.forgotpassword;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.ActivityForgotPasswordBinding;
import com.example.infrastructure.BaseActivity;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

/**
 * ForgotPasswordActivity là Activity xử lý giao diện cho chức năng quên mật khẩu.
 *
 * Tác giả: Trần Văn An
 */
public class ForgotPasswordActivity extends BaseActivity<ForgotPasswordViewModel, ActivityForgotPasswordBinding> {

    /**
     * Phương thức trả về layout resource ID cho Activity.
     *
     * @return layout resource ID.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_forgot_password;
    }

    /**
     * Phương thức trả về class của ViewModel sử dụng trong Activity.
     *
     * @return class của ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected Class<ForgotPasswordViewModel> getViewModelClass() {
        return ForgotPasswordViewModel.class;
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
        return new ForgotPasswordViewModelFactory(authRepos);
    }

    /**
     * Phương thức onCreate được gọi khi Activity được khởi tạo.
     *
     * @param savedInstanceState Bundle lưu trữ trạng thái trước đó của Activity.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupObservers();
    }

    /**
     * Thiết lập các observer để lắng nghe thay đổi từ ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    private void setupObservers() {
        // Thiết lập observer để lắng nghe thay đổi của navigateToLogin từ ViewModel.
        viewModel.getNavigateToLogin().observe(this, navigate -> {
            if (navigate) {
                // Điều hướng tới màn hình đăng nhập với hiệu ứng fade out.
                navigationManager.navigateToLogin(EAnimationType.FADE_OUT);
            }
        });
    }
}