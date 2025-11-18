package com.example.friend.profileviewer;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.common.customcontrol.customalertdialog.AlertDialogModel;
import com.example.friend.FriendRequest;
import com.example.friend.api.request.AddFriendRequest;
import com.example.friend.repository.FriendRequestRepos;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.EGender;
import com.example.user.User;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;

import java.util.Date;

/**
 * ViewModel cho ProfileViewerActivity, quản lý dữ liệu hiển thị và tương tác người dùng.
 *
 * Tác giả: Trần Văn An
 */
public class ProfileViewerViewModel extends BaseViewModel {

    private static final String TAG = ProfileViewerViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Uri> userImageUri = new MutableLiveData<>();
    private final MutableLiveData<String> fullName = new MutableLiveData<>();
    private final MutableLiveData<EGender> gender = new MutableLiveData<>();
    private final MutableLiveData<String> birthdayStr = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUserInitializing = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isButtonLoading = new MutableLiveData<>();
    private final MutableLiveData<EFriendshipStatus> friendshipStatus = new MutableLiveData<>(EFriendshipStatus.NOT_FOUND);
    private final UserRepos userRepos;
    private final AuthRepos authRepos;
    private final FriendRequestRepos friendRequestRepos;
    private String loggedUserId = "";
    private String displayedUserId = "";
    private String friendRequestId = "";

    /**
     * Trả về LiveData để quan sát sự kiện chuyển hướng trở lại.
     *
     * @return LiveData<Boolean> cho sự kiện chuyển hướng trở lại.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    /**
     * Trả về LiveData để quan sát URI của hình ảnh người dùng.
     *
     * @return LiveData<Uri> cho URI của hình ảnh người dùng.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Uri> getUserImageUri() {
        return userImageUri;
    }

    /**
     * Trả về LiveData để quan sát tên đầy đủ của người dùng.
     *
     * @return LiveData<String> cho tên đầy đủ của người dùng.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<String> getFullName() {
        return fullName;
    }

    /**
     * Trả về LiveData để quan sát giới tính của người dùng.
     *
     * @return LiveData<EGender> cho giới tính của người dùng.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<EGender> getGender() {
        return gender;
    }

    /**
     * Trả về LiveData để quan sát chuỗi ngày sinh của người dùng.
     *
     * @return LiveData<String> cho chuỗi ngày sinh của người dùng.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<String> getBirthdayStr() {
        return birthdayStr;
    }

    /**
     * Trả về LiveData để quan sát trạng thái khởi tạo người dùng.
     *
     * @return LiveData<Boolean> cho trạng thái khởi tạo người dùng.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getIsUserInitializing() {
        return isUserInitializing;
    }

    /**
     * Trả về LiveData để quan sát trạng thái loading của nút.
     *
     * @return LiveData<Boolean> cho trạng thái loading của nút.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getIsButtonLoading() {
        return isButtonLoading;
    }

    /**
     * Trả về LiveData để quan sát trạng thái của yêu cầu kết bạn.
     *
     * @return LiveData<EFriendshipStatus> cho trạng thái của yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<EFriendshipStatus> getFriendshipStatus() {
        return friendshipStatus;
    }

    /**
     * Constructor để khởi tạo ViewModel với các repository cần thiết.
     *
     * @param userRepos          Repository cho người dùng.
     * @param authRepos          Repository cho xác thực.
     * @param friendRequestRepos Repository cho yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    public ProfileViewerViewModel(UserRepos userRepos,
                                  AuthRepos authRepos,
                                  FriendRequestRepos friendRequestRepos) {
        this.userRepos = userRepos;
        this.authRepos = authRepos;
        this.friendRequestRepos = friendRequestRepos;
    }

    /**
     * Lấy ID của người dùng đăng nhập và lưu vào biến loggedUserId.
     *
     * Tác giả: Trần Văn An
     */
    public void fetchLoggedUserId() {
        this.loggedUserId = authRepos.getCurrentUid();
    }

    /**
     * Tải hồ sơ người dùng từ dịch vụ và cập nhật LiveData tương ứng khi hoàn thành.
     * Nếu có lỗi, hiển thị thông báo và xử lý tương ứng.
     *
     * @param userId ID của người dùng cần tải hồ sơ.
     *
     * Tác giả: Trần Văn An
     */
    public void fetchUserProfile(String userId) {
        isUserInitializing.postValue(true);
        userRepos.getUserByUid(userId)
                .thenAccept(user -> {
                    setUser(user);
                    // Sau khi cập nhật dữ liệu, đặt isUserInitializing về false sau 200ms.
                    new Handler().postDelayed(() -> {
                        isUserInitializing.postValue(false);
                    }, 200);
                })
                .exceptionally(e -> {
                    Log.e(TAG, e.getMessage(), e);
                    openUserNotFoundDialog();
                    return null;
                });
    }

    /**
     * Tải trạng thái của yêu cầu kết bạn và cập nhật LiveData tương ứng khi hoàn thành.
     * Nếu không có yêu cầu kết bạn, cập nhật trạng thái và dừng.
     * Nếu có lỗi, ghi log và dừng.
     *
     * Tác giả: Trần Văn An
     */
    public void fetchFriendRequestStatus() {
        this.isButtonLoading.postValue(true);
        if (Utils.isEmpty(friendRequestId)) {
            this.isButtonLoading.postValue(false);
            this.friendshipStatus.postValue(EFriendshipStatus.NOT_FOUND);
            return;
        }

        friendRequestRepos
                .getFriendRequest(friendRequestId)
                .thenAccept(friendRequest -> {
                    String senderId = friendRequest.getSender().getId();
                    FriendRequest.EStatus status = friendRequest.getStatus();
                    handleFriendRequestStatus(loggedUserId, senderId, status);
                })
                .exceptionally(e -> {
                    this.isButtonLoading.postValue(false);
                    Log.e(TAG, e.getMessage(), e);
                    return null;
                });
    }

    /**
     * Đặt biến navigateBack thành true để chuyển hướng trở lại.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    /**
     * Đặt ID của người dùng được hiển thị.
     *
     * @param displayedUserId ID của người dùng được hiển thị.
     *
     * Tác giả: Trần Văn An
     */
    public void setDisplayedUserId(String displayedUserId) {
        this.displayedUserId = displayedUserId;
    }

    /**
     * Đặt ID của yêu cầu kết bạn.
     *
     * @param friendRequestId ID của yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    public void setFriendRequestId(String friendRequestId) {
        this.friendRequestId = friendRequestId;
    }

    /**
     * Phương thức này hiện tại chưa được triển khai.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToChat() {
        errorToastMessage.postValue("Not implement navigateToChat()");
    }

    /**
     * Gửi yêu cầu kết bạn từ người dùng hiện tại đến người dùng được hiển thị.
     * Hiển thị hộp thoại xác nhận trước khi gửi yêu cầu.
     *
     * Tác giả: Trần Văn An
     */
    public void sendFriendRequest() {
        AlertDialogModel model = new AlertDialogModel.Builder()
                .setTitle("Send Friend Request")
                .setMessage("Are you sure you want to send a friend request?")
                .setPositiveButton("Ok", aVoid -> {
                    String loggedUserId = authRepos.getCurrentUid();
                    AddFriendRequest addFriendRequest =
                            new AddFriendRequest(loggedUserId, displayedUserId, new Date());
                    friendRequestRepos.addFriendRequest(addFriendRequest);
                })
                .setNegativeButton("Cancel", null)
                .build();
        alertDialogModel.postValue(model);
    }

    /**
     * Thu hồi yêu cầu kết bạn đã gửi.
     *
     * Tác giả: Trần Văn An
     */
    public void recallRequest() {
        isButtonLoading.postValue(true);
        AlertDialogModel model = new AlertDialogModel.Builder()
                .setTitle("Recall Friend Request")
                .setMessage("Are you sure you want to recall this friend request?")
                .setPositiveButton("Recall", aVoid -> {
                    friendRequestRepos
                            .delete(friendRequestId)
                            .thenAccept(aVoidDeletion -> {
                                successToastMessage.postValue("Recall successfully");
                                isButtonLoading.postValue(false);
                            })
                            .exceptionally(e -> {
                                Log.e(TAG, "Error: " + e.getMessage(), e);
                                return null;
                            });
                })
                .setNegativeButton("Cancel", null)
                .build();
        alertDialogModel.postValue(model);
    }

    /**
     * Chấp nhận yêu cầu kết bạn từ người dùng khác.
     *
     * Tác giả: Trần Văn An
     */
    public void acceptFriendRequest() {
        isButtonLoading.postValue(true);
        friendRequestRepos
                .updateFriendRequestStatus(friendRequestId, FriendRequest.EStatus.ACCEPTED)
                .thenAccept(aVoid -> {
                    successToastMessage.postValue("Accept successfully");
                    isButtonLoading.postValue(false);
                })
                .exceptionally(e -> {
                    errorToastMessage.postValue("Accept unsuccessfully");
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                    isButtonLoading.postValue(false);
                    return null;
                });
    }

    /**
     * Từ chối yêu cầu kết bạn từ người dùng khác.
     *
     * Tác giả: Trần Văn An
     */
    public void rejectFriendRequest() {
        isButtonLoading.postValue(true);
        friendRequestRepos
                .updateFriendRequestStatus(friendRequestId, FriendRequest.EStatus.REJECTED)
                .thenAccept(aVoid -> {
                    successToastMessage.postValue("Reject successfully");
                    isButtonLoading.postValue(false);
                })
                .exceptionally(e -> {
                    errorToastMessage.postValue("Reject unsuccessfully");
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                    isButtonLoading.postValue(false);
                    return null;
                });
    }

    /**
     * Hủy kết bạn với người dùng khác.
     *
     * Tác giả: Trần Văn An
     */
    public void unfriend() {
        AlertDialogModel model = new AlertDialogModel.Builder()
                .setTitle("Unfriend")
                .setMessage("Are you sure you want to unfriend?")
                .setPositiveButton("Ok", aVoid -> {
                    isButtonLoading.postValue(true);
                    friendRequestRepos
                            .delete(friendRequestId)
                            .thenAccept(aUpdateVoid -> {
                                friendRequestId = "";
                                friendshipStatus.postValue(EFriendshipStatus.NOT_FRIEND);
                                successToastMessage.postValue("Unfriend successfully");
                                isButtonLoading.postValue(false);
                            })
                            .exceptionally(e -> {
                                errorToastMessage.postValue("Unfriend unsuccessfully");
                                Log.e(TAG, "Error: " + e.getMessage(), e);
                                isButtonLoading.postValue(false);
                                return null;
                            });
                })
                .setNegativeButton("Cancel", null)
                .build();
        alertDialogModel.postValue(model);
    }

    /**
     * Cập nhật dữ liệu người dùng sau khi tải hồ sơ thành công.
     *
     * @param user Đối tượng User được tải.
     *
     * Tác giả: Trần Văn An
     */
    private void setUser(User user) {
        this.userImageUri.postValue(user.getUri());
        fullName.postValue(user.getFullName());
        gender.postValue(user.getGender());
        Date birthday = user.getBirthday();
        birthdayStr.postValue(Utils.dateToString(birthday));
    }

    /**
     * Hiển thị hộp thoại thông báo khi người dùng không tồn tại.
     *
     * Tác giả: Trần Văn An
     */
    private void openUserNotFoundDialog() {
        AlertDialogModel model = new AlertDialogModel.Builder()
                .setTitle("User Not Found")
                .setMessage("The user you are trying to access was not found! Click OK to quit!")
                .setPositiveButton("Ok", aVoid -> {
                    navigateBack();
                })
                .build();
        alertDialogModel.postValue(model);
    }

    /**
     * Xử lý trạng thái yêu cầu kết bạn và cập nhật LiveData tương ứng.
     *
     * @param currentUserId ID của người dùng hiện tại.
     * @param senderId      ID của người gửi yêu cầu.
     * @param status        Trạng thái của yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    private void handleFriendRequestStatus(String currentUserId,
                                           String senderId,
                                           FriendRequest.EStatus status) {
        EFriendshipStatus friendshipStatus = Utils
                .convertFriendRequestStatusToFriendshipStatus(currentUserId, senderId, status);
        this.friendshipStatus.postValue(friendshipStatus);
        this.isButtonLoading.postValue(false);
    }
}