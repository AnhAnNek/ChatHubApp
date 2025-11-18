package com.example.chat.message.api;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Factory class để tạo và cung cấp một đối tượng Retrofit để giao tiếp với Firebase Cloud Messaging (FCM).
 *
 * Tác giả: Văn Hoàng
 */
public class FirebaseApiClientFactory {
    // Biến Retrofit tĩnh để giữ instance duy nhất của Retrofit.
    private static Retrofit retrofit = null;

    /**
     * Trả về instance duy nhất của Retrofit để giao tiếp với FCM.
     * Nếu instance chưa được tạo, nó sẽ được tạo mới với cấu hình base URL và converter.
     *
     * @return instance của Retrofit.
     * Tác giả: Văn Hoàng
     */
    public static Retrofit getIns() {
        if (retrofit == null) {
            // Tạo mới instance của Retrofit nếu chưa tồn tại.
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com/fcm/")  // Đặt base URL cho các yêu cầu FCM.
                    .addConverterFactory(ScalarsConverterFactory.create())  // Thêm converter để xử lý các phản hồi dưới dạng chuỗi.
                    .build();
        }
        return retrofit;
    }
}

