package com.example.chat.conversations.repos;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chat.conversations.api.ConversationApiClient;
import com.example.chat.conversations.api.ConversationApiClientFactory;
import com.example.chat.conversations.Conversation;
import com.example.chat.message.Message;
import com.example.infrastructure.BaseRepos;
import com.example.infrastructure.Utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Interface định nghĩa các phương thức để thao tác với dữ liệu cuộc trò chuyện.
 *
 * Tác giả: Nguyễn Hà Quỳnh Giao
 */
public class ConversationReposImpl extends BaseRepos implements ConversationRepos {

    private final ConversationApiClient conversationApiClient;


    public ConversationReposImpl() {
        conversationApiClient = ConversationApiClientFactory.create();
    }

    @Override
    protected String getTag() {
        return ConversationReposImpl.class.getSimpleName();
    }


    /**
     * Thêm cuộc trò chuyện mới.
     *
     * @param message   : Tin nhắn mới được gửi.
     * @return          : Một CompletableFuture chứa cuộc hội thoại.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao.
     */
    @Override
    public CompletableFuture<Conversation> add(Message message) {
        CompletableFuture<Conversation> future = new CompletableFuture<>();
        //  Gửi yêu cầu API thêm cuộc trò chuyện
        conversationApiClient
                .addConversation(message)
                .enqueue(new Callback<Conversation>() {
                    @Override
                    public void onResponse(@NonNull Call<Conversation> call, @NonNull Response<Conversation> response) {
                        if (response.isSuccessful()) {
                            Conversation conversation = response.body();
                            assert conversation != null;
                            Log.d(getTag(), "Conversation added successfully");
                            future.complete(conversation);
                        } else {
                            Log.e(getTag(), "Failed to add conversation: " + response.message());
                            future.completeExceptionally(new Exception("Failed to add conversation"));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Conversation> call, @NonNull Throwable t) {
                        Log.e(getTag(), "Failed to add conversation: " + t.getMessage(), t);
                        future.completeExceptionally(t);
                    }
                });
        return future;
    }

    /**
     * Cập nhật cuộc trò chuyện.
     *
     * @param message   : Tin nhắn mới được gửi.
     * @return          : Một CompletableFuture chứa cuộc hội thoại.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao.
     */

    @Override
    public CompletableFuture<Conversation> update(Message message) {
        CompletableFuture<Conversation> future = new CompletableFuture<>();
        //  Gửi yêu cầu API cập nhật cuộc trò chuyện
        conversationApiClient
                .updateConversation(message)
                .enqueue(new Callback<Conversation>() {
                    @Override
                    public void onResponse(@NonNull Call<Conversation> call, @NonNull Response<Conversation> response) {
                        if (response.isSuccessful()) {
                            Conversation conversation = response.body();
                            assert conversation != null;
                            Log.d(getTag(), "Conversation updated successfully");
                            future.complete(conversation);
                        } else {
                            Log.e(getTag(), "Failed to update conversation: " + response.message());
                            future.completeExceptionally(new Exception("Failed to update conversation"));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Conversation> call, @NonNull Throwable t) {
                        Log.e(getTag(), "Failed to update conversation: " + t.getMessage(), t);
                        future.completeExceptionally(t);
                    }
                });
        return future;
    }


    /**
     * Tìm cuộc hội thoại có sender và recipient
     *
     * @param senderId      : Id người gửi
     * @param recipientId   : Id người nhận
     * @return              :Một CompletableFuture chứa cuộc hội thoại.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao.
     */
    @Override
    public CompletableFuture<Conversation> getConversationBySenderAndRecipient(String senderId, String recipientId) {
        if (Utils.isEmpty(senderId) || Utils.isEmpty(recipientId)) {
            CompletableFuture<Conversation> future = new CompletableFuture<>();
            future.completeExceptionally(new IllegalArgumentException("Invalid senderId or recipientId"));
            return future;
        }

        CompletableFuture<Conversation> future = new CompletableFuture<>();
        // Gửi yêu cầu API lấy cuộc trò chuyện
        conversationApiClient
                .getConversation(senderId, recipientId)
                .enqueue(new Callback<Conversation>() {
                    @Override
                    public void onResponse(Call<Conversation> call, Response<Conversation> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            future.complete(response.body());
                        } else {
                            // Không tìm thấy cuộc trò chuyện, trả về null
                            future.complete(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<Conversation> call, Throwable t) {
                        future.completeExceptionally(t);
                    }
                });

        return future;
    }


    /**
     * Lấy danh sách cuộc hội thoại của userId
     *
     * @param userId    : mã người dùng muốn lấy danh sách cuộc hội thoại
     * @return          : một CompletableFuture chứa danh sách cuộc hội thoại.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao.
     */
    @Override
    public CompletableFuture<List<Conversation>> getConversations(String userId) {
        CompletableFuture<List<Conversation>> future = new CompletableFuture<>();
        //  Gửi yêu cầu API lấy danh sách cuộc trò chuyện
        conversationApiClient
                .fetchConversations(userId)
                .enqueue(new Callback<List<Conversation>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Conversation>> call, @NonNull Response<List<Conversation>> response) {
                        if (!response.isSuccessful()) {
                            future.completeExceptionally(new Exception("Failed operation: " + response.message()));
                            return;
                        }
                        List<Conversation> conversations = response.body();
                        future.complete(conversations);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Conversation>> call, @NonNull Throwable t) {
                        future.completeExceptionally(t);
                        Log.e(getTag(), "Failed to execute API call", t);
                    }
                });
        return future;
    }
}
