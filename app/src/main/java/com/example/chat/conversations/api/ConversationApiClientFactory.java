package com.example.chat.conversations.api;

import com.example.infrastructure.api.RestApiClient;

import retrofit2.Retrofit;

/**
 * Factory class để tạo ra một instance của ConversationApiClient
 *
 * Tác giả: Nguyễn Hà Quỳnh Giao
 */

public class ConversationApiClientFactory {
    /**
     * Tạo instance cho ConversationApiClient
     * @return một Instance của ConversationApiClient
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */

    public static ConversationApiClient create() {
        Retrofit retrofit = RestApiClient.getIns();
        return retrofit.create(ConversationApiClient.class);
    }
}
