package com.example.friend.sentrequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.friend.repository.FriendRequestRepos;
import com.example.user.repository.AuthRepos;

/**
 * Factory để tạo ViewModel cho SentRequestsActivity.
 *
 * Tác giả: Trần Văn An
 */
public class SentRequestsViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    /**
     * Constructor để khởi tạo Factory với các repository cần thiết.
     *
     * @param authRepos          Repository cho xác thực.
     * @param friendRequestRepos Repository cho yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    public SentRequestsViewModelFactory(AuthRepos authRepos, FriendRequestRepos friendRequestRepos) {
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
        if (modelClass.isAssignableFrom(SentRequestsViewModel.class)) {
            return (T) new SentRequestsViewModel(authRepos, friendRequestRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
