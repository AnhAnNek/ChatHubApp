package com.example.friend.profileviewer;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.ActivityProfileViewerBinding;
import com.example.friend.repository.FriendRequestRepos;
import com.example.friend.repository.FriendRequestReposImpl;
import com.example.infrastructure.BaseActivity;
import com.example.infrastructure.Utils;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

/**
 * Activity hiển thị thông tin người dùng.
 *
 * Tác giả: Trần Văn An
 */
public class ProfileViewerActivity extends BaseActivity<ProfileViewerViewModel, ActivityProfileViewerBinding> {

    /**
     * Trả về layout cho activity này.
     *
     * @return ID của layout resource.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_profile_viewer;
    }

    /**
     * Trả về lớp ViewModel được sử dụng cho activity này.
     *
     * @return Lớp ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected Class<ProfileViewerViewModel> getViewModelClass() {
        return ProfileViewerViewModel.class;
    }

    /**
     * Trả về Factory để tạo ViewModel cho activity này.
     *
     * @return Factory để tạo ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        MediaRepos mediaRepos = new MediaReposImpl();
        UserRepos userRepos = new UserReposImpl(mediaRepos);
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        FriendRequestRepos friendRequestRepos = new FriendRequestReposImpl();
        return new ProfileViewerViewModelFactory(userRepos, authRepos, friendRequestRepos);
    }

    /**
     * Được gọi khi activity được tạo.
     * Thiết lập các quan sát và gọi phương thức để chuẩn bị dữ liệu.
     *
     * @param savedInstanceState Bundle chứa trạng thái của activity trước khi bị hủy.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);

        setupObservers();
    }

    /**
     * Được gọi khi activity bắt đầu.
     * Gọi các phương thức để lấy dữ liệu và xử lý Intent.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Lấy ID của người dùng đã đăng nhập và xử lý Intent
        viewModel.fetchLoggedUserId();
        extractIntentData();
    }

    /**
     * Trích xuất dữ liệu từ Intent và cập nhật ViewModel tương ứng.
     * Nếu không có dữ liệu, không thực hiện gì.
     *
     * Tác giả: Trần Văn An
     */
    private void extractIntentData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String selectedUserId = extras.getString(Utils.EXTRA_SELECTED_USER_ID, "");
            String selectedFriendRequestId = extras.getString(Utils.EXTRA_SELECTED_FRIEND_REQUEST_ID, "");

            // Cập nhật ViewModel với các ID đã lấy được
            viewModel.setDisplayedUserId(selectedUserId);
            viewModel.setFriendRequestId(selectedFriendRequestId);

            // Gọi các phương thức để lấy và cập nhật dữ liệu từ repository
            viewModel.fetchUserProfile(selectedUserId);
            viewModel.fetchFriendRequestStatus();
        }
    }

    /**
     * Thiết lập quan sát để xử lý sự kiện chuyển hướng trở lại màn hình trước đó.
     *
     * Tác giả: Trần Văn An
     */
    private void setupObservers() {
        viewModel.getNavigateBack().observe(this, navigate -> {
            navigationManager.navigateBack(null, EAnimationType.FADE_OUT);
        });
    }
}