package com.example.chat.message.api;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * Interface định nghĩa các phương thức API để giao tiếp với Firebase Cloud Messaging (FCM).
 * Sử dụng Retrofit để gửi các yêu cầu HTTP.
 *
 * Tác giả: Văn Hoàng
 */
public interface FirebaseApiClient {

    /**
     * Gửi tin nhắn FCM.
     *
     * @param headers     Các header cần thiết cho yêu cầu HTTP, bao gồm các thông tin xác thực.
     * @param messageBody Nội dung của tin nhắn FCM dưới dạng chuỗi JSON.
     * @return            Một đối tượng Call đại diện cho yêu cầu HTTP để gửi tin nhắn.
     * Tác giả: Văn Hoàng
     */
    @POST("send")
    Call<String> sendFCMMessage(
            @HeaderMap HashMap<String, String> headers,
            @Body String messageBody
    );
}

