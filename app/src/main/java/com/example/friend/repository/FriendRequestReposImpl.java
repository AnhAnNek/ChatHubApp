package com.example.friend.repository;

import android.util.Log;

import com.example.friend.FriendRequest;
import com.example.friend.FriendRequestView;
import com.example.friend.api.FriendRequestApiClient;
import com.example.friend.api.FriendRequestApiClientFactory;
import com.example.friend.api.request.AddFriendRequest;
import com.example.friend.api.request.UpdateFriendRequest;
import com.example.infrastructure.BaseRepos;
import com.example.infrastructure.Utils;
import com.example.user.User;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Lớp `FriendRequestReposImpl` triển khai giao diện `FriendRequestRepos` và cung cấp các phương thức thực thi.
 *
 * Tác giả: Trần Văn An
 */
public class FriendRequestReposImpl extends BaseRepos implements FriendRequestRepos {

    private final FriendRequestApiClient friendRequestApiClient;

    public FriendRequestReposImpl() {
        friendRequestApiClient = FriendRequestApiClientFactory.create();
    }

    @Override
    protected String getTag() {
        return FriendRequestReposImpl.class.getSimpleName();
    }

    /**
     * Lấy danh sách các yêu cầu kết bạn đang chờ cho ID người gửi đã cho.
     *
     * @param senderId ID của người gửi để truy xuất các yêu cầu đang chờ.
     * @return CompletableFuture chứa danh sách các đối tượng FriendRequestView khi thành công,
     * hoặc ngoại lệ nếu cuộc gọi API thất bại.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<List<FriendRequestView>> getPendingFriendRequestsBySenderId(
            String senderId
    ) {
        CompletableFuture<List<FriendRequestView>> future = new CompletableFuture<>();

        // Gọi API lấy danh sách yêu cầu kết bạn đang chờ cho người gửi
        friendRequestApiClient
                .getPendingFriendRequestsBySenderId(senderId)
                .enqueue(new Callback<List<FriendRequest>>() {
                    @Override
                    public void onResponse(Call<List<FriendRequest>> call, Response<List<FriendRequest>> response) {
                        // Xử lý khi API trả về thành công
                        if (!response.isSuccessful()) {
                            future.completeExceptionally(new Exception("Failed operation: " + response.message()));
                            return;
                        }

                        List<FriendRequest> friendRequests = response.body();
                        List<FriendRequestView> friendRequestViews =
                                convertFriendRequestsToFriendRequestViews(friendRequests, senderId);
                        future.complete(friendRequestViews);
                    }

                    @Override
                    public void onFailure(Call<List<FriendRequest>> call, Throwable t) {
                        // Xử lý khi API thất bại
                        future.completeExceptionally(t);
                        Log.e(getTag(), "Failed to execute API call", t);
                    }
                });
        return future;
    }

    /**
     * Lấy danh sách các yêu cầu kết bạn đang chờ cho ID người nhận đã cho.
     *
     * @param recipientId ID của người nhận để truy xuất các yêu cầu đang chờ.
     * @return CompletableFuture chứa danh sách các đối tượng FriendRequestView khi thành công,
     * hoặc ngoại lệ nếu cuộc gọi API thất bại.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<List<FriendRequestView>> getPendingFriendRequestsByRecipientId(
            String recipientId
    ) {
        CompletableFuture<List<FriendRequestView>> future = new CompletableFuture<>();

        // Gọi API lấy danh sách yêu cầu kết bạn đang chờ cho người nhận
        friendRequestApiClient
                .getPendingFriendRequestsByRecipientId(recipientId)
                .enqueue(new Callback<List<FriendRequest>>() {
                    @Override
                    public void onResponse(Call<List<FriendRequest>> call, Response<List<FriendRequest>> response) {
                        // Xử lý khi API trả về thành công
                        if (!response.isSuccessful()) {
                            future.completeExceptionally(new Exception("Failed operation: " + response.message()));
                        }

                        List<FriendRequest> friendRequests = response.body();
                        List<FriendRequestView> friendRequestViews =
                                convertFriendRequestsToFriendRequestViews(friendRequests, recipientId);
                        future.complete(friendRequestViews);
                    }

                    @Override
                    public void onFailure(Call<List<FriendRequest>> call, Throwable t) {
                        // Xử lý khi API thất bại
                        future.completeExceptionally(t);
                        Log.e(getTag(), "Failed to execute API call", t);
                    }
                });
        return future;
    }


    /**
     * Thêm yêu cầu kết bạn mới.
     *
     * @param request Đối tượng AddFriendRequest chứa thông tin yêu cầu kết bạn mới.
     * @return CompletableFuture sẽ hoàn thành khi yêu cầu kết bạn được thêm thành công hoặc thất bại.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> addFriendRequest(AddFriendRequest request) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        friendRequestApiClient
                .addFriendRequest(request)
                .enqueue(handleResponseCallbackWithoutData(future));
        return future;
    }


    /**
     * Cập nhật trạng thái yêu cầu kết bạn.
     *
     * @param friendRequestId ID của yêu cầu kết bạn cần cập nhật.
     * @param status Trạng thái mới của yêu cầu kết bạn.
     * @return CompletableFuture sẽ hoàn thành khi trạng thái yêu cầu kết bạn được cập nhật thành công hoặc thất bại.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> updateFriendRequestStatus(
            String friendRequestId,
            FriendRequest.EStatus status
    ) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        UpdateFriendRequest updateFriendRequest =
                new UpdateFriendRequest(friendRequestId, status.toString());
        friendRequestApiClient
                .updateFriendRequestStatus(updateFriendRequest)
                .enqueue(handleResponseCallbackWithoutData(future));
        return future;
    }

    /**
     * Lấy thông tin yêu cầu kết bạn theo ID.
     *
     * @param friendRequestId ID của yêu cầu kết bạn cần lấy thông tin.
     * @return CompletableFuture chứa đối tượng FriendRequest khi thành công,
     * hoặc ngoại lệ nếu cuộc gọi API thất bại.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<FriendRequest> getFriendRequest(String friendRequestId) {
        if (Utils.isEmpty(friendRequestId)) {
            CompletableFuture<FriendRequest> future = new CompletableFuture<>();
            future.completeExceptionally(new IllegalArgumentException("Invalid friendRequestId"));
            return future;
        }

        CompletableFuture<FriendRequest> future = new CompletableFuture<>();
        friendRequestApiClient
                .getFriendRequest(friendRequestId)
                .enqueue(handleResponseCallback(future));
        return future;
    }

    /**
     * Lấy danh sách các yêu cầu kết bạn đã được chấp nhận cho ID người dùng đã cho.
     *
     * @param userId ID của người dùng để truy xuất các yêu cầu đã được chấp nhận.
     * @return CompletableFuture chứa danh sách các đối tượng FriendRequestView khi thành công,
     * hoặc ngoại lệ nếu cuộc gọi API thất bại.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<List<FriendRequestView>> getAcceptedFriendRequests(String userId) {
        CompletableFuture<List<FriendRequestView>> future = new CompletableFuture<>();
        friendRequestApiClient
                .getAcceptedFriendRequests(userId)
                .enqueue(new Callback<List<FriendRequest>>() {
                    @Override
                    public void onResponse(Call<List<FriendRequest>> call, Response<List<FriendRequest>> response) {
                        if (!response.isSuccessful()) {
                            future.completeExceptionally(new Exception("Failed operation: " + response.message()));
                            return;
                        }

                        List<FriendRequest> friendRequests = response.body();
                        List<FriendRequestView> friendRequestViews =
                                convertFriendRequestsToFriendRequestViews(friendRequests, userId);
                        future.complete(friendRequestViews);
                    }

                    @Override
                    public void onFailure(Call<List<FriendRequest>> call, Throwable t) {
                        future.completeExceptionally(t);
                        Log.e(getTag(), "Failed to execute API call", t);
                    }
                });
        return future;
    }

    /**
     * Lấy danh sách các bạn bè được đề xuất cho ID người dùng đã cho.
     *
     * @param userId ID của người dùng để truy xuất các bạn bè được đề xuất.
     * @param maxQty Số lượng bạn bè được đề xuất tối đa.
     * @return CompletableFuture chứa danh sách các đối tượng FriendRequestView khi thành công,
     * hoặc ngoại lệ nếu cuộc gọi API thất bại.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<List<FriendRequestView>> getRecommendedFriends(String userId, int maxQty) {
        CompletableFuture<List<FriendRequestView>> future = new CompletableFuture<>();
        friendRequestApiClient
                .getRecommendedFriends(userId, maxQty)
                .enqueue(new Callback<List<FriendRequest>>() {
                    @Override
                    public void onResponse(Call<List<FriendRequest>> call, Response<List<FriendRequest>> response) {
                        if (!response.isSuccessful()) {
                            handleErrorResponse(response, future);
                            return;
                        }

                        List<FriendRequest> friendRequests = response.body();
                        List<FriendRequestView> friendRequestViews =
                                convertFriendRequestsToFriendRequestViews(friendRequests, userId);
                        future.complete(friendRequestViews);
                    }

                    @Override
                    public void onFailure(Call<List<FriendRequest>> call, Throwable t) {
                        future.completeExceptionally(t);
                        Log.e(getTag(), "Failed to execute API call", t);
                    }
                });
        return future;
    }

    /**
     * Xóa yêu cầu kết bạn theo ID.
     *
     * @param friendRequestId ID của yêu cầu kết bạn cần xóa.
     * @return CompletableFuture sẽ hoàn thành khi yêu cầu kết bạn được xóa thành công hoặc thất bại.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> delete(String friendRequestId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        friendRequestApiClient
                .deleteFriendRequest(friendRequestId)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.isSuccessful()) {
                            handleErrorResponse(response, future);
                            return;
                        }

                        future.complete(null);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        future.completeExceptionally(t);
                        Log.e(getTag(), "Failed to execute API call", t);
                    }
                });
        return future;
    }

    /**
     * Chuyển đổi danh sách FriendRequest thành danh sách FriendRequestView.
     *
     * @param friendRequests Danh sách các yêu cầu kết bạn.
     * @param curUserId ID của người dùng hiện tại.
     * @return Danh sách các đối tượng FriendRequestView.
     */
    private List<FriendRequestView> convertFriendRequestsToFriendRequestViews(
            @Nullable List<FriendRequest> friendRequests,
            String curUserId
    ) {
        if (friendRequests == null) return new ArrayList<>();

        List<FriendRequestView> friendRequestViews = new ArrayList<>();
        for (FriendRequest friendRequest : friendRequests) {
            User sender = friendRequest.getSender();
            User recipient = friendRequest.getRecipient();
            FriendRequestView friendRequestView = new FriendRequestView();

            boolean isSender = sender.getId().equals(curUserId);
            if (isSender) {
                friendRequestView.setDisplayedUserId(recipient.getId());
                friendRequestView.setDisplayName(recipient.getFullName());
            } else {
                friendRequestView.setDisplayedUserId(sender.getId());
                friendRequestView.setDisplayName(sender.getFullName());
            }
            friendRequestView.setFriendRequest(friendRequest);

            friendRequestViews.add(friendRequestView);
        }

        return friendRequestViews;
    }
}
