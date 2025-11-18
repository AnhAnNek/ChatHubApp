package com.example.friend.friendrequest;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.common.customcontrol.snackbar.SnackbarModel;
import com.example.common.repository.MediaRepos;
import com.example.friend.FriendRequest;
import com.example.friend.FriendRequestView;
import com.example.friend.friendrequest.adapter.FriendRequestListener;
import com.example.friend.repository.FriendRequestRepos;
import com.example.infrastructure.AppExecutors;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.repository.AuthRepos;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsViewModel extends BaseViewModel implements FriendRequestListener {

    /**
     * ViewModel quản lý dữ liệu và logic liên quan đến danh sách yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    private static final String TAG = FriendRequestsViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateToFriends = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSentRequests = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToFriendSuggestions = new MutableLiveData<>();
    private final MutableLiveData<Bundle> navigateToProfileViewer = new MutableLiveData<>();
    private final MutableLiveData<List<FriendRequestView>> friendRequests = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isRequestsLoading = new MutableLiveData<>();
    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    /**
     * Lấy dữ liệu điều hướng đến màn hình bạn bè.
     *
     * @return LiveData chứa boolean điều hướng.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateToFriends() {
        return navigateToFriends;
    }

    /**
     * Lấy dữ liệu điều hướng đến màn hình yêu cầu đã gửi.
     *
     * @return LiveData chứa boolean điều hướng.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateToSentRequests() {
        return navigateToSentRequests;
    }

    /**
     * Lấy dữ liệu điều hướng đến màn hình gợi ý kết bạn.
     *
     * @return LiveData chứa boolean điều hướng.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateToFriendSuggestions() {
        return navigateToFriendSuggestions;
    }

    /**
     * Lấy dữ liệu điều hướng đến màn hình xem hồ sơ.
     *
     * @return LiveData chứa Bundle dữ liệu điều hướng.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Bundle> getNavigateToProfileViewer() {
        return navigateToProfileViewer;
    }

    /**
     * Lấy danh sách yêu cầu kết bạn.
     *
     * @return LiveData chứa danh sách yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<List<FriendRequestView>> getFriendRequests() {
        return friendRequests;
    }

    /**
     * Lấy trạng thái tải yêu cầu kết bạn.
     *
     * @return LiveData chứa boolean trạng thái tải.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getIsRequestsLoading() {
        return isRequestsLoading;
    }

    /**
     * Khởi tạo ViewModel với các repository cần thiết.
     *
     * @param authRepos Repository xác thực người dùng.
     * @param friendRequestRepos Repository quản lý yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    public FriendRequestsViewModel(AuthRepos authRepos,
                                   FriendRequestRepos friendRequestRepos
    ) {
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    /**
     * Được gọi khi ViewModel bắt đầu.
     * Tải danh sách yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onStart() {
        super.onStart();

        loadFriendRequests();
    }

    /**
     * Xử lý sự kiện khi một mục trong danh sách yêu cầu kết bạn được nhấp.
     *
     * @param position Vị trí của mục được nhấp.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onItemClick(int position) {
        FriendRequestView requestView = this.friendRequests.getValue().get(position);
        FriendRequest friendRequest = requestView.getFriendRequest();
        Bundle data = new Bundle();
        data.putString(Utils.EXTRA_SELECTED_USER_ID, friendRequest.getSender().getId());
        data.putString(Utils.EXTRA_SELECTED_FRIEND_REQUEST_ID, friendRequest.getId());
        this.navigateToProfileViewer.postValue(data);
    }

    /**
     * Xử lý sự kiện khi nút chấp nhận yêu cầu kết bạn được nhấn.
     *
     * @param position Vị trí của mục được nhấp.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onAcceptClick(int position) {
        updateFriendRequestStatus(position, FriendRequest.EStatus.ACCEPTED,
                "Friend request accepted");
    }

    /**
     * Xử lý sự kiện khi nút từ chối yêu cầu kết bạn được nhấp.
     *
     * @param position Vị trí của mục được nhấp.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onRejectClick(int position) {
        updateFriendRequestStatus(position, FriendRequest.EStatus.REJECTED,
                "Friend request rejected");
    }

    /**
     * Cập nhật trạng thái của yêu cầu kết bạn và hiển thị thông báo snackbar.
     *
     * @param position Vị trí của yêu cầu kết bạn trong danh sách.
     * @param status Trạng thái mới của yêu cầu kết bạn.
     * @param message Thông báo hiển thị trong snackbar.
     *
     * Tác giả: Trần Văn An
     */
    private void updateFriendRequestStatus(int position, FriendRequest.EStatus status, String message) {
        // Lấy danh sách yêu cầu kết bạn hiện tại.
        List<FriendRequestView> friendRequestViews = this.friendRequests.getValue();
        // Lấy yêu cầu kết bạn tại vị trí đã chỉ định.
        FriendRequestView request = friendRequestViews.get(position);
        request.setLoading(true);
        // Cập nhật danh sách yêu cầu kết bạn để phản ánh trạng thái loading.
        this.friendRequests.postValue(friendRequestViews);

        // Sử dụng Handler để tạo một độ trễ trước khi cập nhật giao diện người dùng.
        new Handler().postDelayed(() -> {
            // Loại bỏ yêu cầu kết bạn khỏi danh sách tại vị trí đã chỉ định.
            request.setLoading(false);
            friendRequestViews.remove(position);
            this.friendRequests.postValue(friendRequestViews);
        }, 200);

        // Tạo một model snackbar để hiển thị thông báo và cho phép người dùng hoàn tác hành động.
        SnackbarModel model = new SnackbarModel.Builder()
                .duration(7000)
                .message(message)
                .actionText("Undo")
                .customAction(aSnackbarVoid -> {
                    friendRequestViews.add(position, request);
                    this.friendRequests.postValue(friendRequestViews);
                })
                .onDismissedAction(dismissEvent -> {
                    if (dismissEvent == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        return;
                    }

                    // Nếu người dùng không hoàn tác, cập nhật trạng thái yêu cầu kết bạn trong repository.
                    friendRequestRepos
                            .updateFriendRequestStatus(request.getFriendRequest().getId(), status)
                            .exceptionally(e -> {
                                Log.e(TAG, "Error: " + e.getMessage(), e);
                                return null;
                            });
                })
                .build();

        // Đăng model snackbar để hiển thị.
        snackbarModel.postValue(model);
    }

    /**
     * Tải danh sách yêu cầu kết bạn từ repository.
     *
     * Tác giả: Trần Văn An
     */
    private void loadFriendRequests() {
        this.isRequestsLoading.postValue(true);
        String uid = authRepos.getCurrentUid();
        friendRequestRepos.getPendingFriendRequestsByRecipientId(uid)
                .thenAccept(friendRequests -> {
                    this.isRequestsLoading.postValue(false);
                    this.friendRequests.postValue(friendRequests);
                })
                .exceptionally(e -> {
                    this.isRequestsLoading.postValue(false);
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                    return null;
                });
    }

    /**
     * Điều hướng đến màn hình gợi ý kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToFriendSuggestions() {
        this.navigateToFriendSuggestions.postValue(true);
    }

    /**
     * Điều hướng đến màn hình bạn bè.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToFriends() {
        this.navigateToFriends.postValue(true);
    }

    /**
     * Điều hướng đến màn hình yêu cầu đã gửi.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToSentRequests() {
        this.navigateToSentRequests.postValue(true);
    }
}
