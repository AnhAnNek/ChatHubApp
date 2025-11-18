package com.example.user.login.google;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.user.login.LoginViewModelFactory;
import com.example.user.repository.AuthRepos;

/**
 * GoogleSignInViewModelFactory là một Factory để tạo ra GoogleSignInViewModel.
 *
 * Tác giả: Trần Văn An
 */
public class GoogleSignInViewModelFactory extends LoginViewModelFactory {

    /**
     * Constructor để khởi tạo Factory với AuthRepos.
     *
     * @param authRepos AuthRepos để thực hiện các thao tác liên quan đến xác thực.
     *
     * Tác giả: Trần Văn An
     */
    public GoogleSignInViewModelFactory(AuthRepos authRepos) {
        super(authRepos);
    }

    /**
     * Phương thức create để tạo ra ViewModel theo yêu cầu.
     *
     * @param modelClass lớp của ViewModel cần tạo.
     * @param <T>        kiểu của ViewModel.
     * @return ViewModel đã được tạo.
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GoogleSignInViewModel.class)) {
            return (T) new GoogleSignInViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
