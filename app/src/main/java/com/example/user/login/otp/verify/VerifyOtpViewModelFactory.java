package com.example.user.login.otp.verify;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

/**
 * Factory để tạo ra ViewModel cho việc xác minh OTP.
 *
 * Tác giả: Trần Văn An
 */
public class VerifyOtpViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;

    /**
     * Constructor để khởi tạo ViewModelFactory với AuthRepos.
     *
     * @param authRepos AuthRepos để thực hiện các thao tác liên quan đến xác thực.
     *
     * Tác giả: Trần Văn An
     */
    public VerifyOtpViewModelFactory(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    /**
     * Phương thức để tạo ra ViewModel.
     *
     * @param modelClass Lớp của ViewModel cần tạo.
     * @param <T>        Kiểu của ViewModel.
     * @return ViewModel đã tạo.
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(VerifyOtpViewModel.class)) {
            return (T) new VerifyOtpViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
