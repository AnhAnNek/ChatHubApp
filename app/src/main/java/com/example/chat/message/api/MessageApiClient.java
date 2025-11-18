package com.example.chat.message.api;

import com.example.chat.message.MessageDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Interface định nghĩa các phương thức API để gửi và nhận tin nhắn.
 * Sử dụng Retrofit để gửi các yêu cầu HTTP.
 *
 * Tác giả: Văn Hoàng
 */
public interface MessageApiClient {

    /**
     * Gửi một tin nhắn đến người khác.
     *
     * @param message Đối tượng MessageDTO chứa thông tin của tin nhắn cần gửi.
     * @return        Một đối tượng Call đại diện cho yêu cầu HTTP để gửi tin nhắn,
     *                với phản hồi là một đối tượng MessageDTO chứa thông tin tin nhắn đã gửi.
     *
     * Tác giả: Văn Hoàng
     */
    @POST("/api/v1/messages/send-message")
    Call<MessageDTO> sendMessage(@Body MessageDTO message);

    /**
     * Lấy danh sách các tin nhắn cho người dùng cụ thể bằng uid của họ.
     *
     * @param UserUid ID của người dùng mà tin nhắn cần lấy.
     * @return        Một đối tượng Call đại diện cho yêu cầu HTTP để lấy tin nhắn,
     *                với phản hồi là danh sách các đối tượng MessageDTO.
     * Tác giả: Văn Hoàng
     */
    @GET("/api/v1/messages/fetch-messages")
    Call<List<MessageDTO>> getMessages(@Query("userUid") String UserUid);
}
