package com.example.friend.myfriend;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.ActivityFriendsBinding;
import com.example.friend.myfriend.adapter.FriendAdapter;
import com.example.friend.repository.FriendRequestRepos;
import com.example.friend.repository.FriendRequestReposImpl;
import com.example.infrastructure.BaseActivity;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.ArrayList;


/**
 * Hoạt động hiển thị danh sách bạn bè.
 *
 * Tác giả: Trần Văn An
 */
public class FriendsActivity extends BaseActivity<FriendsViewModel, ActivityFriendsBinding> {

    private MediaRepos mediaRepos;
    private FriendAdapter friendsAdapter;

    /**
     * Trả về layout resource ID cho activity.
     *
     * @return Layout resource ID.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_friends;
    }

    /**
     * Trả về lớp ViewModel tương ứng với activity này.
     *
     * @return Lớp ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected Class<FriendsViewModel> getViewModelClass() {
        return FriendsViewModel.class;
    }

    /**
     * Tạo và trả về một ViewModelProvider.Factory để tạo ra ViewModel cho activity.
     * Trong phương thức này, chúng ta khởi tạo các repository cần thiết cho ViewModel.
     *
     * @return ViewModelProvider.Factory để tạo ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        mediaRepos = new MediaReposImpl();
        UserRepos userRepos = new UserReposImpl(mediaRepos);
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        FriendRequestRepos friendRequestRepos = new FriendRequestReposImpl();
        return new FriendsViewModelFactory(authRepos, friendRequestRepos);
    }

    /**
     * Ghi đè phương thức onCreate để thực hiện các thao tác cần thiết khi activity được tạo.
     * Trong phương thức này, chúng ta thiết lập adapter cho RecyclerView và quan sát dữ liệu.
     *
     * @param savedInstanceState State trước đó của activity, nếu có.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friendsAdapter = new FriendAdapter(this, new ArrayList<>(), viewModel, mediaRepos);
        binding.setFriendAdapter(friendsAdapter);

        // Thiết lập các quan sát viên để quan sát dữ liệu và cập nhật giao diện khi cần thiết
        setupObservers();
    }

    /**
     * Thiết lập các quan sát viên để quan sát dữ liệu từ ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    private void setupObservers() {
        viewModel.getNavigateToChat().observe(this, data -> {
            navigationManager.navigateToChat(data, EAnimationType.FADE_IN);
        });

        viewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateBack(null, EAnimationType.FADE_OUT);
            }
        });

        viewModel.getNavigateToProfileViewer().observe(this, data -> {
            navigationManager.navigateToProfileViewer(data, EAnimationType.FADE_IN);
        });

        viewModel.getFriendRequests().observe(this, newFriendRequests -> {
            if (newFriendRequests != null) {
                friendsAdapter.setItems(newFriendRequests);
            }
        });
    }
}