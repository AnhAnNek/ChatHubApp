package com.example.friend.myfriend;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.friend.repository.FriendRequestRepos;
import com.example.user.repository.AuthRepos;

/**
 * Factory để tạo ra ViewModel của FriendsActivity.
 *
 * Tác giả: Trần Văn An
 */
public class FriendsViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    // Constructor để truyền vào AuthRepos và FriendRequestRepos
    public FriendsViewModelFactory(AuthRepos authRepos, FriendRequestRepos friendRequestRepos) {
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        // Kiểm tra nếu modelClass là assignable từ FriendsViewModel
        if (modelClass.isAssignableFrom(FriendsViewModel.class)) {
            // Nếu là, tạo một đối tượng FriendsViewModel mới với AuthRepos và FriendRequestRepos đã được truyền vào
            return (T) new FriendsViewModel(authRepos, friendRequestRepos);
        }
        // Nếu không phải là FriendsViewModel, ném ra một ngoại lệ
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
