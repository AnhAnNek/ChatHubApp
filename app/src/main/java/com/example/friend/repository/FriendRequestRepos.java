package com.example.friend.repository;

import com.example.friend.FriendRequest;
import com.example.friend.FriendRequestView;
import com.example.friend.api.request.AddFriendRequest;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Giao diện `FriendRequestRepos` định nghĩa các phương thức
 * để thực hiện các hoạt động liên quan đến yêu cầu kết bạn.
 *
 * Tác giả: Trần Văn An
 */
public interface FriendRequestRepos {
    /**
     * Phương thức để nhận các yêu cầu kết bạn chưa được xác nhận dựa trên ID của người gửi.
     * @param senderId ID của người gửi.
     * @return CompletableFuture<List<FriendRequestView>> chứa danh sách các yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<List<FriendRequestView>> getPendingFriendRequestsBySenderId(String senderId);

    /**
     * Phương thức để nhận các yêu cầu kết bạn chưa được xác nhận dựa trên ID của người nhận.
     * @param recipientId ID của người nhận.
     * @return CompletableFuture<List<FriendRequestView>> chứa danh sách các yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<List<FriendRequestView>> getPendingFriendRequestsByRecipientId(String recipientId);

    /**
     * Phương thức để thêm yêu cầu kết bạn mới.
     * @param request Đối tượng AddFriendRequest chứa thông tin về yêu cầu kết bạn.
     * @return CompletableFuture<Void> để theo dõi trạng thái thực hiện của hoạt động.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> addFriendRequest(AddFriendRequest request);

    /**
     * Phương thức để cập nhật trạng thái của yêu cầu kết bạn.
     * @param friendRequestId ID của yêu cầu kết bạn cần cập nhật.
     * @param status Trạng thái mới của yêu cầu kết bạn.
     * @return CompletableFuture<Void> để theo dõi trạng thái thực hiện của hoạt động.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> updateFriendRequestStatus(String friendRequestId, FriendRequest.EStatus status);

    /**
     * Phương thức để nhận một yêu cầu kết bạn dựa trên ID của nó.
     * @param friendRequestId ID của yêu cầu kết bạn cần nhận.
     * @return CompletableFuture<FriendRequest> chứa thông tin về yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<FriendRequest> getFriendRequest(String friendRequestId);

    /**
     * Phương thức để nhận danh sách các yêu cầu kết bạn đã được chấp nhận dựa trên ID của người dùng.
     * @param userId ID của người dùng.
     * @return CompletableFuture<List<FriendRequestView>> chứa danh sách các yêu cầu kết bạn đã được chấp nhận.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<List<FriendRequestView>> getAcceptedFriendRequests(String userId);

    /**
     * Phương thức để đề xuất danh sách bạn bè dựa trên ID của người dùng và số lượng tối đa.
     * @param userId ID của người dùng.
     * @param maxQty Số lượng tối đa của các đề xuất bạn bè.
     * @return CompletableFuture<List<FriendRequestView>> chứa danh sách bạn bè đề xuất.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<List<FriendRequestView>> getRecommendedFriends(String userId, int maxQty);

    /**
     * Phương thức để xóa một yêu cầu kết bạn dựa trên ID của nó.
     * @param friendRequestId ID của yêu cầu kết bạn cần xóa.
     * @return CompletableFuture<Void> để theo dõi trạng thái thực hiện của hoạt động.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> delete(String friendRequestId);
}
