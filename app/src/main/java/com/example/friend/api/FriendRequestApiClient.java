package com.example.friend.api;

import com.example.friend.FriendRequest;
import com.example.friend.api.request.AddFriendRequest;
import com.example.friend.api.request.UpdateFriendRequest;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Giao diện `FriendRequestApiClient` định nghĩa các phương thức để tương tác với API yêu cầu kết bạn.
 *
 * Tác giả: Trần Văn An
 */
public interface FriendRequestApiClient {

    /**
     * Gửi yêu cầu thêm bạn bè.
     *
     * @param addRequest Đối tượng AddFriendRequest chứa thông tin yêu cầu thêm bạn bè.
     * @return Cuộc gọi Retrofit để thực hiện yêu cầu.
     *
     * Tác giả: Trần Văn An
     */
    @POST("/api/v1/friend-requests/add-friend-request")
    Call<String> addFriendRequest(@Body AddFriendRequest addRequest);

    /**
     * Cập nhật trạng thái của yêu cầu kết bạn.
     *
     * @param updateRequest Đối tượng UpdateFriendRequest chứa thông tin cập nhật trạng thái yêu cầu.
     * @return Cuộc gọi Retrofit để thực hiện yêu cầu.
     *
     * Tác giả: Trần Văn An
     */
    @PUT("/api/v1/friend-requests/update-friend-request-status")
    Call<String> updateFriendRequestStatus(@Body UpdateFriendRequest updateRequest);

    /**
     * Xóa yêu cầu kết bạn theo ID.
     *
     * @param friendRequestId ID của yêu cầu kết bạn cần xóa.
     * @return Cuộc gọi Retrofit để thực hiện yêu cầu.
     *
     * Tác giả: Trần Văn An
     */
    @DELETE("/api/v1/friend-requests/delete-friend-request/{friendRequestId}")
    Call<String> deleteFriendRequest(@Path("friendRequestId") String friendRequestId);

    /**
     * Lấy danh sách các yêu cầu kết bạn đang chờ theo ID người gửi.
     *
     * @param senderId ID của người gửi.
     * @return Cuộc gọi Retrofit để thực hiện yêu cầu, trả về danh sách các yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    @GET("/api/v1/friend-requests/get-pending-friend-requests-by-sender-id/{senderId}")
    Call<List<FriendRequest>> getPendingFriendRequestsBySenderId(@Path("senderId") String senderId);

    /**
     * Lấy danh sách các yêu cầu kết bạn đang chờ theo ID người nhận.
     *
     * @param recipientId ID của người nhận.
     * @return Cuộc gọi Retrofit để thực hiện yêu cầu, trả về danh sách các yêu cầu kết bạn.
     *
     * Tác giả: Trần Văn An
     */
    @GET("/api/v1/friend-requests/get-pending-friend-requests-by-recipient-id/{recipientId}")
    Call<List<FriendRequest>> getPendingFriendRequestsByRecipientId(@Path("recipientId") String recipientId);

    /**
     * Lấy thông tin yêu cầu kết bạn theo ID.
     *
     * @param friendRequestId ID của yêu cầu kết bạn.
     * @return Cuộc gọi Retrofit để thực hiện yêu cầu, trả về đối tượng FriendRequest.
     *
     * Tác giả: Trần Văn An
     */
    @GET("/api/v1/friend-requests/get-friend-request-by-id/{friendRequestId}")
    Call<FriendRequest> getFriendRequest(@Path("friendRequestId") String friendRequestId);

    /**
     * Lấy danh sách các yêu cầu kết bạn đã được chấp nhận cho người dùng.
     *
     * @param userId ID của người dùng.
     * @return Cuộc gọi Retrofit để thực hiện yêu cầu, trả về danh sách các yêu cầu kết bạn đã chấp nhận.
     *
     * Tác giả: Trần Văn An
     */
    @GET("/api/v1/friend-requests/get-accepted-friend-requests/{userId}")
    Call<List<FriendRequest>> getAcceptedFriendRequests(@Path("userId") String userId);

    /**
     * Lấy danh sách các bạn bè được đề xuất cho người dùng.
     *
     * @param userId ID của người dùng.
     * @param maxQty Số lượng bạn bè đề xuất tối đa.
     * @return Cuộc gọi Retrofit để thực hiện yêu cầu, trả về danh sách các bạn bè được đề xuất.
     *
     * Tác giả: Trần Văn An
     */
    @GET("/api/v1/friend-requests/get-recommended-friend-requests/{userId}")
    Call<List<FriendRequest>> getRecommendedFriends(@Path("userId") String userId,
                                                    @Query("maxQuantity") int maxQty);
}
