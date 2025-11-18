package com.example.friend.sentrequest;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.common.customcontrol.snackbar.SnackbarModel;
import com.example.friend.FriendRequest;
import com.example.friend.FriendRequestView;
import com.example.friend.repository.FriendRequestRepos;
import com.example.friend.sentrequest.adapter.SentRequestListener;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.repository.AuthRepos;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel cho hoạt động SentRequestsActivity, chịu trách nhiệm quản lý dữ liệu
 * và xử lý logic cho giao diện người dùng.
 *
 * Tác giả: Trần Văn An
 */
public class SentRequestsViewModel extends BaseViewModel implements SentRequestListener {

    private static final String TAG = SentRequestsViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Bundle> navigateToProfileViewer = new MutableLiveData<>();
    private final MutableLiveData<List<FriendRequestView>> sentFriendRequests = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isSentRequestsLoading = new MutableLiveData<>(true);
    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    /**
     * LiveData để lắng nghe sự kiện điều hướng quay lại.
     *
     * @return LiveData<Boolean> true nếu có yêu cầu điều hướng quay lại, ngược lại là false.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    /**
     * LiveData để lắng nghe sự kiện điều hướng tới trang xem hồ sơ.
     *
     * @return LiveData<Bundle> chứa thông tin cần thiết để điều hướng tới trang xem hồ sơ.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Bundle> getNavigateToProfileViewer() {
        return navigateToProfileViewer;
    }

    /**
     * LiveData để lắng nghe danh sách yêu cầu kết bạn đã gửi.
     *
     * @return LiveData<List<FriendRequestView>> chứa danh sách yêu cầu kết bạn đã gửi.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<List<FriendRequestView>> getSentFriendRequests() {
        return sentFriendRequests;
    }

    /**
     * LiveData để lắng nghe trạng thái tải dữ liệu của danh sách yêu cầu đã gửi.
     *
     * @return LiveData<Boolean> true nếu đang tải dữ liệu, ngược lại là false.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getIsSentRequestsLoading() {
        return isSentRequestsLoading;
    }

    /**
     * Constructor để khởi tạo ViewModel với các repository cần thiết.
     *
     * @param authRepos          Repository cho xác thực.
     * @param friendRequestRepos Repository cho yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    public SentRequestsViewModel(AuthRepos authRepos, FriendRequestRepos friendRequestRepos) {
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    /**
     * Phương thức được gọi khi Activity được khởi đầu.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onStart() {
        super.onStart();

        // tải danh sách yêu cầu đã gửi
        loadSentFriendRequests();
    }

    /**
     * Xử lý sự kiện khi một mục trong danh sách được nhấp.
     * Phương thức này sẽ tạo một Bundle chứa thông tin về yêu cầu kết bạn được chọn và gửi tín hiệu điều hướng tới trang xem hồ sơ.
     *
     * @param position Vị trí của mục trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onItemClick(int position) {
        FriendRequestView requestView = this.sentFriendRequests.getValue().get(position);
        FriendRequest friendRequest = requestView.getFriendRequest();
        Bundle data = new Bundle();
        data.putString(Utils.EXTRA_SELECTED_USER_ID, friendRequest.getSender().getId());
        data.putString(Utils.EXTRA_SELECTED_FRIEND_REQUEST_ID, friendRequest.getId());
        this.navigateToProfileViewer.postValue(data);
    }

    /**
     * Xử lý sự kiện khi nút Recall được nhấp.
     * Phương thức này sẽ gửi một yêu cầu gỡ lại yêu cầu kết bạn đã gửi,
     * cập nhật giao diện và hiển thị một Snackbar để hoàn tác.
     *
     * @param position Vị trí của mục trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onRecallClick(int position) {
        List<FriendRequestView> sentFriendRequests = this.sentFriendRequests.getValue();
        FriendRequestView request = sentFriendRequests.get(position);
        request.setLoading(true);
        this.sentFriendRequests.postValue(sentFriendRequests);

        new Handler().postDelayed(() -> {
            request.setLoading(false);
            sentFriendRequests.remove(position);
            this.sentFriendRequests.postValue(sentFriendRequests);
        }, 200);

        // Tạo một snackbar để cho phép người dùng hoàn tác hành động đã thực hiện
        SnackbarModel model = new SnackbarModel.Builder()
                .duration(7000)
                .message("Friend request sent")
                .actionText("Undo")
                .customAction(aSnackbarVoid -> {
                    sentFriendRequests.add(position, request);
                    this.sentFriendRequests.postValue(sentFriendRequests);
                })
                .onDismissedAction(dismissEvent -> {
                    // Nếu không hoàn tác, xóa yêu cầu kết bạn khỏi cơ sở dữ liệu
                    if (dismissEvent == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        return;
                    }

                    String friendRequestId = request.getFriendRequest().getId();
                    friendRequestRepos
                            .delete(friendRequestId)
                            .thenAccept(aVoid -> {
                            })
                            .exceptionally(e -> {
                                // Ghi log lỗi nếu có lỗi xảy ra trong quá trình xóa
                                Log.e(TAG, "Error: " + e.getMessage(), e);
                                return null;
                            });
                })
                .build();

        // Hiển thị snackbar
        snackbarModel.postValue(model);
    }

    /**
     * Gửi một tín hiệu điều hướng để quay lại màn hình trước đó.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    /**
     * Phương thức này tải danh sách yêu cầu kết bạn đã gửi từ máy chủ và cập nhật giao diện tương ứng.
     * Nếu quá trình tải xảy ra lỗi, nó sẽ gửi một tín hiệu để dừng hiệu ứng tải và ghi log lỗi.
     *
     * Tác giả: Trần Văn An
     */
    private void loadSentFriendRequests() {
        this.isSentRequestsLoading.postValue(true);
        String curUserId = authRepos.getCurrentUid();
        friendRequestRepos.getPendingFriendRequestsBySenderId(curUserId)
                .thenAccept(friendRequests -> {
                    this.isSentRequestsLoading.postValue(false);
                    this.sentFriendRequests.postValue(friendRequests);
                })
                .exceptionally(e -> {
                    this.isSentRequestsLoading.postValue(false);
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                    return null;
                });
    }
}
