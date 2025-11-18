package com.example.user.signup;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.ActivitySignUpBinding;
import com.example.infrastructure.BaseActivity;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

/**
 * Activity đăng ký mới cho người dùng.
 * Đây là màn hình chứa giao diện đăng ký mới và xử lý logic tương ứng.
 *
 * Tác giả: Trần Văn An
 */
public class SignUpActivity extends BaseActivity<SignUpViewModel, ActivitySignUpBinding> {

    /**
     * Phương thức trả về layout của activity đăng ký.
     *
     * @return ID của layout activity đăng ký.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_sign_up;
    }

    /**
     * Phương thức trả về lớp ViewModel cho activity đăng ký.
     *
     * @return Lớp ViewModel của activity đăng ký.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected Class<SignUpViewModel> getViewModelClass() {
        return SignUpViewModel.class;
    }

    /**
     * Phương thức tạo và trả về factory để tạo ViewModel cho activity đăng ký.
     *
     * @return Factory để tạo ViewModel của activity đăng ký.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        MediaRepos mediaRepos = new MediaReposImpl();
        UserRepos userRepos = new UserReposImpl(mediaRepos);
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        return new SignUpViewModelFactory(authRepos);
    }

    /**
     * Phương thức được gọi khi activity được tạo.
     *
     * @param savedInstanceState Dữ liệu của activity trước khi bị hủy (nếu có).
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupObservers();
    }

    /**
     * Thiết lập các observer để theo dõi sự kiện điều hướng từ ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    private void setupObservers() {
        // Observer cho sự kiện điều hướng đến màn hình đăng nhập
        viewModel.getNavigateToLogin().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToLogin(EAnimationType.FADE_OUT);
            }
        });

        // Observer cho sự kiện điều hướng đến màn hình chính sau khi đăng ký thành công
        viewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateToHome(EAnimationType.FADE_IN);
            }
        });
    }
}
