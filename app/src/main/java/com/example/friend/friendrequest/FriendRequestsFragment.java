package com.example.friend.friendrequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.FragmentFriendRequestsBinding;
import com.example.friend.friendrequest.adapter.FriendRequestAdapter;
import com.example.friend.repository.FriendRequestRepos;
import com.example.friend.repository.FriendRequestReposImpl;
import com.example.infrastructure.BaseFragment;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.ArrayList;

/**
 * Fragment quản lý giao diện người dùng cho danh sách yêu cầu kết bạn.
 *
 * Tác giả: Trần Văn An
 */
public class FriendRequestsFragment extends BaseFragment<FriendRequestsViewModel, FragmentFriendRequestsBinding> {

    private FriendRequestAdapter friendRequestsAdapter;
    private MediaRepos mediaRepos;

    /**
     * Khởi tạo và trả về binding cho layout fragment_friend_requests.xml
     *
     * @param inflater LayoutInflater để sử dụng.
     * @param container ViewGroup cha mà fragment thuộc về.
     * @return FragmentFriendRequestsBinding đối tượng binding cho layout.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected FragmentFriendRequestsBinding getViewDataBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentFriendRequestsBinding.inflate(inflater, container, false);
    }

    /**
     * Trả về lớp ViewModel liên kết với Fragment này.
     *
     * @return Class của ViewModel liên kết.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected Class<FriendRequestsViewModel> getViewModelClass() {
        return FriendRequestsViewModel.class;
    }

    /**
     * Tạo và trả về ViewModelFactory để tạo ViewModel.
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
        return new FriendRequestsViewModelFactory(authRepos, friendRequestRepos);
    }

    /**
     * Được gọi khi view đã được tạo.
     *
     * @param view View được tạo bởi Fragment.
     * @param savedInstanceState Bundle lưu trữ trạng thái trước đó của Fragment.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupFriendRequestAdapter();
        setupObservers();
    }

    /**
     * Thiết lập các quan sát viên để lắng nghe và xử lý các thay đổi dữ liệu.
     *
     * Tác giả: Trần Văn An
     */
    private void setupObservers() {
        viewModel.getNavigateToFriendSuggestions().observe(requireActivity(), navigate -> {
            if (navigate) {
                navigationManager.navigateToFriendSuggestions(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToFriends().observe(requireActivity(), navigate -> {
            if (navigate) {
                navigationManager.navigateToFriends(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToSentRequests().observe(requireActivity(), navigate -> {
            if (navigate) {
                navigationManager.getNavigateToSentRequests(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToProfileViewer().observe(requireActivity(), data -> {
            navigationManager.navigateToProfileViewer(data, EAnimationType.FADE_OUT);
        });

        viewModel.getFriendRequests().observe(requireActivity(), newFriendRequests -> {
            if (newFriendRequests != null) {
                friendRequestsAdapter.setFriendRequests(newFriendRequests);
            }
        });
    }

    /**
     * Tạo và thiết lập adapter cho RecyclerView hiển thị danh sách yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    private void setupFriendRequestAdapter() {
        friendRequestsAdapter =
                new FriendRequestAdapter(requireActivity(), new ArrayList<>(), viewModel, mediaRepos);
        binding.setFriendRequestAdapter(friendRequestsAdapter);
    }
}