package com.example.friend.myfriend;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.friend.FriendRequest;
import com.example.friend.FriendRequestView;
import com.example.friend.myfriend.adapter.FriendListener;
import com.example.friend.repository.FriendRequestRepos;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.repository.AuthRepos;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel cho màn hình danh sách bạn bè.
 *
 * Tác giả: Trần Văn An
 */
public class FriendsViewModel extends BaseViewModel implements FriendListener {

    private static final String TAG = FriendsViewModel.class.getSimpleName();
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Bundle> navigateToProfileViewer = new MutableLiveData<>();
    private final MutableLiveData<Bundle> navigateToChat = new MutableLiveData<>();
    private final MutableLiveData<List<FriendRequestView>> friendRequests = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isFriendsLoading = new MutableLiveData<>(true);
    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    /**
     * Trả về LiveData để quan sát sự kiện chuyển hướng trở lại màn hình trước đó.
     *
     * @return LiveData<Boolean> để quan sát sự kiện chuyển hướng trở lại.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    /**
     * Trả về LiveData để quan sát sự kiện chuyển hướng đến màn hình xem hồ sơ người dùng.
     *
     * @return LiveData<Bundle> để quan sát sự kiện chuyển hướng đến màn hình xem hồ sơ.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Bundle> getNavigateToProfileViewer() {
        return navigateToProfileViewer;
    }

    /**
     * Trả về LiveData để quan sát sự kiện chuyển hướng đến màn hình trò chuyện.
     *
     * @return LiveData<Bundle> để quan sát sự kiện chuyển hướng đến màn hình trò chuyện.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Bundle> getNavigateToChat() {
        return navigateToChat;
    }

    /**
     * Trả về LiveData để quan sát danh sách các yêu cầu kết bạn.
     *
     * @return LiveData<List<FriendRequestView>> để quan sát danh sách yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<List<FriendRequestView>> getFriendRequests() {
        return friendRequests;
    }

    /**
     * Trả về LiveData để quan sát trạng thái của việc tải danh sách bạn bè.
     *
     * @return LiveData<Boolean> để quan sát trạng thái của việc tải danh sách bạn bè.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getIsFriendsLoading() {
        return isFriendsLoading;
    }

    /**
     * Khởi tạo ViewModel với các repository cần thiết.
     *
     * @param authRepos          Repository cho xác thực người dùng.
     * @param friendRequestRepos Repository cho yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    public FriendsViewModel(AuthRepos authRepos, FriendRequestRepos friendRequestRepos) {
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    /**
     * Ghi đè phương thức onStart để load danh sách yêu cầu kết bạn đã chấp nhận khi activity bắt đầu.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onStart() {
        super.onStart();

        loadAcceptedFriendRequests();
    }

    /**
     * Xử lý sự kiện khi một mục trong danh sách yêu cầu kết bạn được nhấp.
     *
     * @param position Vị trí của mục được nhấp trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onItemClick(int position) {
        // Lấy yêu cầu kết bạn tương ứng với vị trí được nhấn
        FriendRequestView requestView = this.friendRequests.getValue().get(position);
        FriendRequest friendRequest = requestView.getFriendRequest();

        // Tạo Bundle để chứa dữ liệu cần truyền đến màn hình xem hồ sơ
        Bundle data = new Bundle();
        String senderId = friendRequest.getSender().getId();
        String recipientId = friendRequest.getRecipient().getId();
        String currentUserId = authRepos.getCurrentUid();
        String selectedUserId = senderId.equals(currentUserId) ? recipientId : senderId;
        data.putString(Utils.EXTRA_SELECTED_USER_ID, selectedUserId);
        data.putString(Utils.EXTRA_SELECTED_FRIEND_REQUEST_ID, friendRequest.getId());

        // Gửi dữ liệu thông qua LiveData để chuyển hướng đến màn hình xem hồ sơ
        this.navigateToProfileViewer.postValue(data);
    }

    /**
     * Xử lý sự kiện khi người dùng muốn mở cuộc trò chuyện với một người trong danh sách yêu cầu kết bạn.
     *
     * @param position Vị trí của mục được chọn trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onOpenChatClick(int position) {
        // Lấy yêu cầu kết bạn tương ứng với vị trí được chọn
        FriendRequestView requestView = this.friendRequests.getValue().get(position);
        FriendRequest friendRequest = requestView.getFriendRequest();

        // Tạo Bundle để chứa dữ liệu cần truyền đến màn hình trò chuyện
        Bundle data = new Bundle();
        String senderId = authRepos.getCurrentUid();
        String recipientId = senderId.equals(friendRequest.getSender().getId())?
                friendRequest.getRecipient().getId():
                friendRequest.getSender().getId();

        data.putString(Utils.KEY_SENDER_ID, senderId);
        data.putString(Utils.KEY_RECIPIENT_ID, recipientId);

        // Gửi dữ liệu thông qua LiveData để chuyển hướng đến màn hình trò chuyện
        this.navigateToChat.postValue(data);
    }

    /**
     * Phương thức để chuyển hướng trở lại màn hình trước đó.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    /**
     * Phương thức để tải danh sách yêu cầu kết bạn đã chấp nhận từ repository.
     *
     * Tác giả: Trần Văn An
     */
    private void loadAcceptedFriendRequests() {
        this.isFriendsLoading.postValue(true);
        String uid = authRepos.getCurrentUid();
        friendRequestRepos.getAcceptedFriendRequests(uid)
                .thenAccept(friendRequests -> {
                    this.isFriendsLoading.postValue(false);
                    this.friendRequests.postValue(friendRequests);
                })
                .exceptionally(e -> {
                    this.isFriendsLoading.postValue(false);
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                    return null;
                });
    }
}
