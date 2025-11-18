package com.example.user.api;

import com.example.infrastructure.api.RestApiClient;

import retrofit2.Retrofit;

/**
 * Factory class để tạo ra một instance của UserApiClient.
 *
 * Tác giả: Trần Văn An'
 */
public class UserApiClientFactory {

    /**
     * Tạo một instance của UserApiClient.
     *
     * @return UserApiClient một instance của UserApiClient.
     *
     * Tác giả: Trần Văn An
     */
    public static UserApiClient create() {
        // Lấy một instance của Retrofit từ RestApiClient để gửi và nhận dữ liệu qua mạng.
        Retrofit retrofit = RestApiClient.getIns();

        // Tạo một instance của UserApiClient sử dụng Retrofit đã cấu hình.
        return retrofit.create(UserApiClient.class);
    }
}
