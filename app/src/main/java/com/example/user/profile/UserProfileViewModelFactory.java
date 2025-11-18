package com.example.user.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;

/**
 * Factory class để tạo ViewModel cho UserProfileActivity.
 * Cung cấp dependency injection (DI) cho UserProfileViewModel.
 *
 * Tác giả: Trần Văn An
 */
public class UserProfileViewModelFactory implements ViewModelProvider.Factory {

    private final UserRepos userRepos;
    private final AuthRepos authRepos;

    /**
     * Khởi tạo UserProfileViewModelFactory với UserRepos và AuthRepos cần thiết.
     *
     * @param userRepos Repository cho người dùng
     * @param authRepos Repository cho xác thực
     *
     * Tác giả: Trần Văn An
     */
    public UserProfileViewModelFactory(UserRepos userRepos, AuthRepos authRepos) {
        this.userRepos = userRepos;
        this.authRepos = authRepos;
    }

    /**
     * Tạo một instance của ViewModel cụ thể cho UserProfileActivity.
     *
     * @param modelClass Lớp ViewModel cần tạo
     * @param <T>        Kiểu của ViewModel
     * @return Một instance của UserProfileViewModel nếu modelClass là loại tương ứng, nếu không, ném một IllegalArgumentException
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserProfileViewModel.class)) {
            // Trả về một instance của UserProfileViewModel với dependency injection cho userRepos và authRepos
            return (T) new UserProfileViewModel(userRepos, authRepos);
        }
        // Nếu lớp không khớp, ném một ngoại lệ IllegalArgumentException
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
