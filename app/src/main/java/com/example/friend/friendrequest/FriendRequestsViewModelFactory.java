package com.example.friend.friendrequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.common.repository.MediaRepos;
import com.example.friend.repository.FriendRequestRepos;
import com.example.user.repository.AuthRepos;

/**
 * Factory class cho ViewModel FriendRequestsViewModel.
 *
 * Tác giả: Trần Văn An
 */
public class FriendRequestsViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    /**
     * Khởi tạo Factory với các repository cần thiết.
     *
     * @param authRepos Repository xác thực người dùng.
     * @param friendRequestRepos Repository quản lý yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    public FriendRequestsViewModelFactory(AuthRepos authRepos,
                                          FriendRequestRepos friendRequestRepos
    ) {
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    /**
     * Tạo ViewModel cho lớp được chỉ định.
     *
     * @param modelClass Lớp của ViewModel cần tạo.
     * @return ViewModel được tạo.
     * @throws IllegalArgumentException Nếu lớp ViewModel không hợp lệ.
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FriendRequestsViewModel.class)) {
            return (T) new FriendRequestsViewModel(authRepos, friendRequestRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
