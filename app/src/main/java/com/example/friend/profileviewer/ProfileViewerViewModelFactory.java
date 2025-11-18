package com.example.friend.profileviewer;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.friend.repository.FriendRequestRepos;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;

/**
 * Factory để tạo ViewModel cho ProfileViewerActivity.
 * Nhận các repository cần thiết để tạo ViewModel.
 *
 * Tác giả: Trần Văn An
 */
public class ProfileViewerViewModelFactory implements ViewModelProvider.Factory {

    private final UserRepos userRepos;
    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    /**
     * Constructor để khởi tạo Factory với các repository cần thiết.
     *
     * @param userRepos          Repository cho người dùng.
     * @param authRepos          Repository cho xác thực.
     * @param friendRequestRepos Repository cho yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    public ProfileViewerViewModelFactory(UserRepos userRepos,
                                         AuthRepos authRepos,
                                         FriendRequestRepos friendRequestRepos) {
        this.userRepos = userRepos;
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    /**
     * Phương thức được gọi để tạo một ViewModel.
     *
     * @param modelClass Lớp ViewModel cần tạo.
     * @return ViewModel đã được tạo.
     * @throws IllegalArgumentException Nếu lớp ViewModel không được hỗ trợ.
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProfileViewerViewModel.class)) {
            // Nếu modelClass là ProfileViewerViewModel, tạo một đối tượng của lớp đó
            return (T) new ProfileViewerViewModel(userRepos, authRepos, friendRequestRepos);
        }
        // Nếu không, ném một ngoại lệ IllegalArgumentException
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
