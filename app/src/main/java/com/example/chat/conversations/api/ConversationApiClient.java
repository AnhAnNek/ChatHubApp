package com.example.chat.conversations.api;

import com.example.chat.conversations.Conversation;
import com.example.chat.message.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ConversationApiClient {
    /**
     * Interface định nghĩa các phương thức API để lấy danh sách cuộc trò chuyện.
     *
     * @param userId    :   Mã người dùng muốn lấy danh sách các cuộc trò chuyện.
     * @return          :   Một đối tượng Call đại diện cho yêu cầu HTTP lấy danh sách
     *                      trò chuyện, với đối tượng là một danh sách Conversation chứa
     *                      thông tin cuộc trò chuyện
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */
    @GET("/api/v1/conversations/fetch-conversations/{userId}")
    Call<List<Conversation>> fetchConversations(@Path("userId") String userId);

    /**
     * Interface định nghĩa các phương thức API để lưu trữ thông tin của cuộc trò chuyện
     *
     * @param message   :   Đối tượng Message chứa thông tin của tin nhắn đã gửi.
     * @return          :   Một đối tượng Call đại diện cho yêu cầu HTTP lưu trữ cuộc trò chuyện
     *                      với phản hồi là cuộc trò chuyện đã được lưu trữ.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */
    @POST("/api/v1/conversations/add-new-conversation")
    Call<Conversation> addConversation(@Body Message message);

    /**
     * Interface định nghĩa các phương thức API lấy thông tin 1 cuộc trò chuyện.
     *
     * @param senderId      :   Đối tượng senderId chứa thông tin Id của người gửi.
     * @param recipientId   :   Đối tượng recipientId chứa thông tin Id của người nhận.
     * @return              :   Một đối tượng Call đại diện cho yêu cầu HTTP tìm cuộc trò chuyện
     *                          cùng có sự tham gia của sender và recipient, phản hồi là cuộc
     *                          trò chuyện tìm được.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */
    @GET("/api/v1/conversations/get-conversation")
    Call<Conversation> getConversation(
            @Query("senderId") String senderId,
            @Query("recipientId") String recipientId);

    /**
     * Interface định nghĩa các phương thức API cập nhật thông tin cuộc trò chuyện.
     *
     * @param message   :   Đối tượng Message chứa thông tin của tin nhắn đã gửi.
     * @return          :   Một đối tượng Call đại diện cho yêu cầu HTTP cập nhật cuộc trò chuyện
     *                      với phản hồi là cuộc trò chuyện đã được cập nhật.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */
    @PUT("/api/v1/conversations/update-conversation")
    Call<Conversation> updateConversation(@Body Message message);
}
