package com.example.friend.api;

import com.example.infrastructure.api.RestApiClient;

import retrofit2.Retrofit;

public class FriendRequestApiClientFactory {

    /**
     * Tạo một instance của FriendRequestApiClient.
     *
     * @return Instance của FriendRequestApiClient.
     */
    public static FriendRequestApiClient create() {
        // Lấy instance của Retrofit từ RestApiClient
        Retrofit retrofit = RestApiClient.getIns();
        // Tạo và trả về instance của FriendRequestApiClient
        return retrofit.create(FriendRequestApiClient.class);
    }
}
