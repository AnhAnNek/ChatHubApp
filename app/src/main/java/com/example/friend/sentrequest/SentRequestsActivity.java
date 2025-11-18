package com.example.friend.sentrequest;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.ActivitySentRequestsBinding;
import com.example.friend.repository.FriendRequestRepos;
import com.example.friend.repository.FriendRequestReposImpl;
import com.example.friend.sentrequest.adapter.SentRequestAdapter;
import com.example.infrastructure.BaseActivity;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.ArrayList;

/**
 * Hoạt động hiển thị danh sách yêu cầu kết bạn đã gửi.
 *
 * Tác giả: Trần Văn An
 */
public class SentRequestsActivity extends BaseActivity<SentRequestsViewModel, ActivitySentRequestsBinding> {

    private MediaRepos mediaRepos;
    private SentRequestAdapter sentRequestAdapter;

    /**
     * Trả về layout cho hoạt động này.
     *
     * @return ID của layout.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_sent_requests;
    }

    /**
     * Trả về lớp ViewModel được sử dụng cho hoạt động này.
     *
     * @return Lớp ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected Class<SentRequestsViewModel> getViewModelClass() {
        return SentRequestsViewModel.class;
    }

    /**
     * Tạo một đối tượng Factory để tạo ViewModel cho hoạt động này.
     *
     * @return Đối tượng Factory.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        mediaRepos = new MediaReposImpl();
        UserRepos userRepos = new UserReposImpl(mediaRepos);
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        FriendRequestRepos friendRequestRepos = new FriendRequestReposImpl();
        return new SentRequestsViewModelFactory(authRepos, friendRequestRepos);
    }

    /**
     * Ghi đè phương thức onCreate để thiết lập các thành phần khi hoạt động được tạo.
     *
     * @param savedInstanceState Dữ liệu trạng thái trước đó của hoạt động.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo adapter và thiết lập cho RecyclerView
        sentRequestAdapter =
                new SentRequestAdapter(this, new ArrayList<>(), viewModel, mediaRepos);
        binding.setRequestAdapter(sentRequestAdapter);

        // Thiết lập các quan sát viên
        observers();
    }

    /**
     * Thiết lập các quan sát viên để theo dõi các thay đổi trong ViewModel
     * và cập nhật giao diện người dùng tương ứng.
     *
     * Tác giả: Trần Văn An
     */
    private void observers() {
        viewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateBack(null, EAnimationType.FADE_OUT);
            }
        });

        viewModel.getNavigateToProfileViewer().observe(this, data -> {
            navigationManager.navigateToProfileViewer(data, EAnimationType.FADE_IN);
        });

        viewModel.getSentFriendRequests().observe(this, newSentRequests -> {
            if (newSentRequests != null) {
                sentRequestAdapter.setItems(newSentRequests);
            }
        });
    }
}