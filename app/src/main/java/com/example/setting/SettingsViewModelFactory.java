package com.example.setting;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

/**
 * Factory tạo SettingsViewModel.
 *
 * Tác giả: Trần Văn An
 */
public class SettingsViewModelFactory implements ViewModelProvider.Factory {

    private AuthRepos authRepos;

    /**
     * Khởi tạo SettingsViewModelFactory với AuthRepos.
     *
     * @param authRepos AuthRepos để tương tác với dữ liệu xác thực
     *
     * Tác giả: Trần Văn An
     */
    public SettingsViewModelFactory(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    /**
     * Tạo ViewModel mới.
     *
     * @param modelClass Class của ViewModel cần tạo
     * @return T instance của ViewModel
     *
     * Tác giả: Trần Văn An
     * @throws IllegalArgumentException nếu modelClass không phải là SettingsViewModel
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
