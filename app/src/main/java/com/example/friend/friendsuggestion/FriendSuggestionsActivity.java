package com.example.friend.friendsuggestion;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.ActivityFriendSuggestionsBinding;
import com.example.friend.friendsuggestion.adapter.FriendSuggestionAdapter;
import com.example.friend.repository.FriendRequestRepos;
import com.example.friend.repository.FriendRequestReposImpl;
import com.example.infrastructure.BaseActivity;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.ArrayList;

/**
 * Hoạt động hiển thị danh sách gợi ý bạn bè.
 * Cung cấp giao diện để người dùng xem và thao tác với danh sách gợi ý bạn bè.
 *
 * Tác giả: Trần Văn An
 */
public class FriendSuggestionsActivity extends BaseActivity<FriendSuggestionsViewModel, ActivityFriendSuggestionsBinding> {

    private MediaRepos mediaRepos;
    private FriendSuggestionAdapter suggestionAdapter;


    /**
     * Trả về layout cho hoạt động này.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_friend_suggestions;
    }

    /**
     * Trả về lớp ViewModel tương ứng cho hoạt động này.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected Class<FriendSuggestionsViewModel> getViewModelClass() {
        return FriendSuggestionsViewModel.class;
    }

    /**
     * Tạo ra một ViewModelFactory để cung cấp ViewModel cho hoạt động.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        mediaRepos = new MediaReposImpl();
        UserRepos userRepos = new UserReposImpl(mediaRepos);
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        FriendRequestRepos friendRequestRepos = new FriendRequestReposImpl();
        return new FriendSuggestionsViewModelFactory(authRepos, friendRequestRepos);
    }

    /**
     * Hàm này được gọi khi hoạt động được tạo ra.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        suggestionAdapter =
                new FriendSuggestionAdapter(this, new ArrayList<>(), viewModel, mediaRepos);
        binding.setSuggestionAdapter(suggestionAdapter);

        setupObservers();
    }

    /**
     * Thiết lập các trình quan sát cho ViewModel để cập nhật giao diện người dùng.
     *
     * Tác giả: Trần Văn An
     */
    private void setupObservers() {
        viewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateBack(null, EAnimationType.FADE_OUT);
            }
        });

        viewModel.getNavigateToProfileViewer().observe(this, data -> {
            navigationManager.navigateToProfileViewer(data, EAnimationType.FADE_IN);
        });

        viewModel.getFriendSuggestions().observe(this, friendRequestViews -> {
            if (friendRequestViews != null) {
                suggestionAdapter.setItems(friendRequestViews);
            }
        });
    }
}