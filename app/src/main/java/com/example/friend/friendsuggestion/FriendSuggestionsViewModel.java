package com.example.friend.friendsuggestion;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.common.customcontrol.snackbar.SnackbarModel;
import com.example.friend.FriendRequest;
import com.example.friend.FriendRequestView;
import com.example.friend.api.request.AddFriendRequest;
import com.example.friend.friendsuggestion.adapter.FriendSuggestionListener;
import com.example.friend.repository.FriendRequestRepos;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.repository.AuthRepos;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ViewModel cho màn hình hiển thị các đề xuất kết bạn.
 *
 * Tác giả: Trần Văn An
 */
public class FriendSuggestionsViewModel extends BaseViewModel implements FriendSuggestionListener {

    private static final String TAG = FriendSuggestionsViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Bundle> navigateToProfileViewer = new MutableLiveData<>();
    private final MutableLiveData<List<FriendRequestView>> friendSuggestions = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isSuggestionsLoading = new MutableLiveData<>();
    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;

    /**
     * Lấy LiveData điều hướng quay lại.
     *
     * @return LiveData điều hướng quay lại.
     */
    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    /**
     * Lấy LiveData điều hướng tới trang xem hồ sơ.
     *
     * @return LiveData điều hướng tới trang xem hồ sơ.
     */
    public LiveData<Bundle> getNavigateToProfileViewer() {
        return navigateToProfileViewer;
    }

    /**
     * Lấy LiveData chứa danh sách các đề xuất kết bạn.
     *
     * @return LiveData chứa danh sách các đề xuất kết bạn.
     */
    public LiveData<List<FriendRequestView>> getFriendSuggestions() {
        return friendSuggestions;
    }

    /**
     * Lấy LiveData cho biết danh sách đề xuất đang tải hay không.
     *
     * @return LiveData trạng thái tải danh sách đề xuất.
     */
    public LiveData<Boolean> getIsSuggestionsLoading() {
        return isSuggestionsLoading;
    }

    /**
     * Khởi tạo ViewModel với các thông tin cần thiết.
     *
     * @param authRepos          Repository xác thực người dùng.
     * @param friendRequestRepos Repository quản lý các yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    public FriendSuggestionsViewModel(AuthRepos authRepos, FriendRequestRepos friendRequestRepos) {
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Tải danh sách bạn bè được đề xuất khi ViewModel bắt đầu
        loadRecommendedFriends();
    }

    /**
     * Xử lý sự kiện khi một mục đề xuất bạn bè được nhấp.
     *
     * @param position Vị trí của mục được nhấp trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onItemClick(int position) {
        // Lấy đối tượng FriendRequestView tại vị trí được nhấp
        FriendRequestView requestView = this.friendSuggestions.getValue().get(position);
        // Lấy đối tượng FriendRequest từ FriendRequestView
        FriendRequest friendRequest = requestView.getFriendRequest();
        Bundle data = new Bundle();
        data.putString(Utils.EXTRA_SELECTED_USER_ID, friendRequest.getSender().getId());
        data.putString(Utils.EXTRA_SELECTED_FRIEND_REQUEST_ID, null);
        // Đăng giá trị Bundle vào LiveData để điều hướng tới trang xem hồ sơ
        this.navigateToProfileViewer.postValue(data);
    }

    /**
     * Xử lý sự kiện khi nút thêm bạn bè được nhấp.
     *
     * @param position Vị trí của mục đề xuất trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onAddFriendClick(int position) {
        // Lấy danh sách các đề xuất kết bạn hiện tại
        List<FriendRequestView> friendSuggestions = this.friendSuggestions.getValue();
        // Lấy đối tượng FriendRequestView tại vị trí được nhấp
        FriendRequestView suggestionView = friendSuggestions.get(position);
        suggestionView.setLoading(true);
        // Cập nhật lại danh sách đề xuất kết bạn
        this.friendSuggestions.postValue(friendSuggestions);

        // Sử dụng Handler để mô phỏng độ trễ khi gửi yêu cầu kết bạn
        new Handler().postDelayed(() -> {
            suggestionView.setLoading(false);
            friendSuggestions.remove(position);
            this.friendSuggestions.postValue(friendSuggestions);
        }, 200);

        // Tạo một đối tượng SnackbarModel để hiển thị thông báo
        SnackbarModel model = new SnackbarModel.Builder()
                .duration(7000)
                .message("Friend request sent")
                .actionText("Undo")
                .customAction(aSnackbarVoid -> {
                    // Xử lý khi người dùng nhấn nút "Hoàn tác"
                    friendSuggestions.add(position, suggestionView);
                    this.friendSuggestions.postValue(friendSuggestions);
                })
                .onDismissedAction(dismissEvent -> {
                    // Xử lý khi thông báo bị hủy
                    if (dismissEvent == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        return;
                    }

                    // Lấy thông tin yêu cầu kết bạn từ đối tượng đề xuất
                    FriendRequest friendRequest = suggestionView.getFriendRequest();
                    String senderId = friendRequest.getSender().getId();
                    String recipientId = friendRequest.getRecipient().getId();

                    AddFriendRequest addFriendRequest =
                            new AddFriendRequest(senderId, recipientId, new Date());
                    // Gửi yêu cầu kết bạn và xử lý ngoại lệ nếu có
                    friendRequestRepos.addFriendRequest(addFriendRequest)
                            .exceptionally(e -> {
                                Log.e(TAG, "Error: " + e.getMessage(), e);
                                return null;
                            });
                })
                .build();

        // Đăng giá trị model vào LiveData để hiển thị Snackbar
        snackbarModel.postValue(model);
    }

    /**
     * Xử lý sự kiện khi nút xóa đề xuất được nhấp.
     *
     * @param position Vị trí của mục đề xuất trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onRemoveClick(int position) {
        // Lấy danh sách các đề xuất kết bạn hiện tại
        List<FriendRequestView> friendSuggestions = this.friendSuggestions.getValue();
        // Lấy đối tượng FriendRequestView tại vị trí được nhấp
        FriendRequestView request = friendSuggestions.get(position);

        // Tạo một đối tượng SnackbarModel để hiển thị thông báo
        SnackbarModel model = new SnackbarModel.Builder()
                .duration(7000)
                .message("Suggestion removed")
                .actionText("Undo")
                .customAction(aSnackbarVoid -> {
                    // Xử lý khi người dùng nhấn nút "Hoàn tác"
                    friendSuggestions.add(position, request);
                    this.friendSuggestions.postValue(friendSuggestions);
                })
                .build();

        // Đăng giá trị model vào LiveData để hiển thị Snackbar
        snackbarModel.postValue(model);
    }

    /**
     * Đăng sự kiện điều hướng quay lại màn hình trước.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    /**
     * Tải danh sách các đề xuất kết bạn từ repository.
     *
     * Tác giả: Trần Văn An
     */
    private void loadRecommendedFriends() {
        this.isSuggestionsLoading.postValue(true);
        // Lấy ID người dùng hiện tại từ repository xác thực
        String curUserId = authRepos.getCurrentUid();
        friendRequestRepos.getRecommendedFriends(curUserId, 20)
                .thenAccept(friendRequests -> {
                    // Khi hoàn thành, đặt trạng thái không còn tải và cập nhật danh sách đề xuất
                    this.isSuggestionsLoading.postValue(false);
                    this.isSuggestionsLoading.postValue(false);
                    this.friendSuggestions.postValue(friendRequests);
                })
                .exceptionally(e -> {
                    // Nếu có lỗi xảy ra, đặt trạng thái không còn tải và ghi log lỗi
                    this.isSuggestionsLoading.postValue(false);
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                    return null;
                });
    }
}
