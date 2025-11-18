package com.example.user.forgotpassword;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

/**
 * ForgotPasswordViewModelFactory là factory để khởi tạo ForgotPasswordViewModel.
 *
 * Tác giả: Trần Văn An
 */
public class ForgotPasswordViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;

    /**
     * Constructor khởi tạo ForgotPasswordViewModelFactory với một đối tượng AuthRepos.
     *
     * @param mAuthService đối tượng AuthRepos để thực hiện các thao tác liên quan tới xác thực.
     *
     * Tác giả: Trần Văn An
     */
    public ForgotPasswordViewModelFactory(AuthRepos mAuthService) {
        this.authRepos = mAuthService;
    }

    /**
     * Phương thức create sẽ được gọi khi ViewModelProvider cần một instance của ForgotPasswordViewModel.
     *
     * @param modelClass class của ViewModel cần được tạo.
     * @param <T> kiểu dữ liệu của ViewModel.
     * @return một instance mới của ForgotPasswordViewModel.
     * @throws IllegalArgumentException nếu modelClass không phải là ForgotPasswordViewModel.
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel.class)) {
            return (T) new ForgotPasswordViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
