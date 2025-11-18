package com.example.user.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

/**
 * LoginViewModelFactory là factory để khởi tạo LoginViewModel.
 *
 * Tác giả: Trần Văn An
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    protected final AuthRepos authRepos;

    /**
     * Constructor khởi tạo factory với AuthRepos.
     *
     * @param authRepos đối tượng AuthRepos để thực hiện các thao tác liên quan tới xác thực.
     *
     * Tác giả: Trần Văn An
     */
    public LoginViewModelFactory(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    /**
     * Phương thức tạo ViewModel.
     *
     * @param modelClass class của ViewModel.
     * @param <T> kiểu của ViewModel.
     * @return đối tượng ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
