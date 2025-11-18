package com.example.user.accountlink;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

/**
 * Factory cho AccountLinkingViewModel, tạo ViewModel với repository xác thực.
 *
 * Tác giả: Trần Văn An
 */
public class AccountLinkingViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;

    /**
     * Khởi tạo AccountLinkingViewModelFactory với repository xác thực.
     *
     * @param mAuthService Repository xác thực
     *
     * Tác giả: Trần Văn An
     */
    public AccountLinkingViewModelFactory(AuthRepos mAuthService) {
        this.authRepos = mAuthService;
    }

    /**
     * Tạo instance của ViewModel.
     *
     * @param modelClass Lớp của ViewModel cần tạo
     * @return Instance của ViewModel
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AccountLinkingViewModel.class)) {
            return (T) new AccountLinkingViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
