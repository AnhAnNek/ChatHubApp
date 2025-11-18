package com.example.friend.friendsuggestion;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.friend.repository.FriendRequestRepos;
import com.example.user.repository.AuthRepos;

/**
 * Factory để tạo ViewModel cho FriendSuggestionsActivity.
 * Tạo ra ViewModel với các tham số cần thiết như AuthRepos và FriendRequestRepos.
 *
 * Tác giả: Trần Văn An
 */
public class FriendSuggestionsViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    /**
     * Khởi tạo một đối tượng FriendSuggestionsViewModelFactory với các tham số cần thiết.
     *
     * Tác giả: Trần Văn An
     */
    public FriendSuggestionsViewModelFactory(AuthRepos authRepos, FriendRequestRepos friendRequestRepos) {
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    /**
     * Tạo ra ViewModel dựa trên lớp ViewModel được cung cấp.
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FriendSuggestionsViewModel.class)) {
            return (T) new FriendSuggestionsViewModel(authRepos, friendRequestRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
